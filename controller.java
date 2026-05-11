package controller;

import analysis.comparisonAnalyzer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import metrics.MetricsCalculator;
import metrics.processMetrics;
import model.ganttEntry;
import model.process;
import scheduler.SRTFScheduler;
import scheduler.priorityScheduler;
import util.inputValidator;

import java.util.ArrayList;
import java.util.List;

public class controller {

    @FXML private VBox processRowsContainer;

    @FXML private Canvas priorityCanvas;
    @FXML private Canvas srtfCanvas;

    @FXML private TableView<processMetrics>        priorityTable;
    @FXML private TableColumn<processMetrics, String>  priorityColId;
    @FXML private TableColumn<processMetrics, Integer> priorityColArrival;
    @FXML private TableColumn<processMetrics, Integer> priorityColBurst;
    @FXML private TableColumn<processMetrics, Integer> priorityColPriority;
    @FXML private TableColumn<processMetrics, Integer> priorityColCT;
    @FXML private TableColumn<processMetrics, Integer> priorityColWT;
    @FXML private TableColumn<processMetrics, Integer> priorityColTAT;
    @FXML private TableColumn<processMetrics, Integer> priorityColRT;

    @FXML private TableView<processMetrics>        srtfTable;
    @FXML private TableColumn<processMetrics, String>  srtfColId;
    @FXML private TableColumn<processMetrics, Integer> srtfColArrival;
    @FXML private TableColumn<processMetrics, Integer> srtfColBurst;
    @FXML private TableColumn<processMetrics, Integer> srtfColPriority;
    @FXML private TableColumn<processMetrics, Integer> srtfColCT;
    @FXML private TableColumn<processMetrics, Integer> srtfColWT;
    @FXML private TableColumn<processMetrics, Integer> srtfColTAT;
    @FXML private TableColumn<processMetrics, Integer> srtfColRT;

    @FXML private Label priorityAvgWT;
    @FXML private Label priorityAvgTAT;
    @FXML private Label priorityAvgRT;
    @FXML private Label srtfAvgWT;
    @FXML private Label srtfAvgTAT;
    @FXML private Label srtfAvgRT;

    @FXML private TextArea summaryArea;
    @FXML private HBox errorbox;
    @FXML private Label errormsg;

    private static final Color[] PALETTE = {
            Color.web("#4fc3f7"), Color.web("#f5a623"), Color.web("#69ff47"),
            Color.web("#ff6b6b"), Color.web("#b388ff"), Color.web("#ff80ab"),
            Color.web("#80cbc4"), Color.web("#ffcc02"), Color.web("#a5d6a7"),
            Color.web("#ef9a9a"), Color.web("#ce93d8"), Color.web("#80deea")
    };

    @FXML
    public void initialize() {
        bindTableColumns();
        styleExistingRows();
    }

    private void bindTableColumns() {
        priorityColId      .setCellValueFactory(new PropertyValueFactory<>("id"));
        priorityColArrival .setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        priorityColBurst   .setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        priorityColPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityColCT      .setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        priorityColWT      .setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        priorityColTAT     .setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        priorityColRT      .setCellValueFactory(new PropertyValueFactory<>("responseTime"));

        srtfColId      .setCellValueFactory(new PropertyValueFactory<>("id"));
        srtfColArrival .setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        srtfColBurst   .setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        srtfColPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        srtfColCT      .setCellValueFactory(new PropertyValueFactory<>("completionTime"));
        srtfColWT      .setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        srtfColTAT     .setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        srtfColRT      .setCellValueFactory(new PropertyValueFactory<>("responseTime"));
    }

    private void styleExistingRows() {
        String[] prompts = {"P1", "0", "1", "1"};
        int rowCount = dataRowCount();
        for (int i = 0; i < rowCount; i++) {
            HBox row = dataRow(i);
            for (int j = 0; j < row.getChildren().size(); j++) {
                if (row.getChildren().get(j) instanceof TextField tf) {
                    tf.getStyleClass().add("input-field");
                    tf.setPromptText(prompts[j]);
                }
            }
        }
    }

    private int dataRowCount() {
        return processRowsContainer.getChildren().size() - 1;
    }

    private HBox dataRow(int index) {
        return (HBox) processRowsContainer.getChildren().get(index);
    }

    private HBox buildRow() {
        HBox row = new HBox();
        row.setPrefHeight(27);
        row.setPrefWidth(1114);

        String[] prompts = {"P1", "0", "1", "1"};
        for (int i = 0; i < 4; i++) {
            TextField tf = new TextField();
            tf.setPrefWidth(105);
            tf.setPrefHeight(26);
            tf.setPromptText(prompts[i]);
            tf.getStyleClass().add("input-field");
            if (i < 3) HBox.setMargin(tf, new Insets(0, 10, 0, 0));
            row.getChildren().add(tf);
        }
        VBox.setMargin(row, new Insets(0, 0, 10, 0));
        return row;
    }

    private void insertRow() {
        int insertAt = processRowsContainer.getChildren().size() - 1;
        processRowsContainer.getChildren().add(insertAt, buildRow());
    }

    @FXML
    private void onAddProcess() {
        insertRow();
    }

    @FXML
    private void onRemoveLast() {
        if (dataRowCount() > 1) {
            processRowsContainer.getChildren().remove(dataRowCount() - 1);
        }
    }

    @FXML
    private void onClearAll() {
        processRowsContainer.getChildren().remove(0, dataRowCount());
        insertRow();
        resetResults();
    }

    @FXML
    private void onRunSimulation() {
        List<process> input = collectProcesses();
        if (input == null) return;

        inputValidator.validationResult vr = inputValidator.validate(input);
        if (!vr.valid) {
            showError(vr.message);
            return;
        }else {
            errorbox.setStyle("-fx-background-color: #0a1a0f;");
            errormsg.setText("✓ Simulated successfully!!");
            errormsg.setStyle("-fx-text-fill: #69ff47");
        }

        priorityScheduler pSched = new priorityScheduler(input);
        List<process>    pResult = pSched.simulate();
        List<ganttEntry> pGantt  = pSched.getGanttChart();
        List<processMetrics> pMetrics = MetricsCalculator.calculate(pResult, pGantt);

        SRTFScheduler    sSched  = new SRTFScheduler(input);
        List<process>    sResult = sSched.simulate();
        List<ganttEntry> sGantt  = sSched.getGanttChart();
        List<processMetrics> sMetrics = MetricsCalculator.calculate(sResult, sGantt);

        priorityTable.setItems(FXCollections.observableArrayList(pMetrics));
        srtfTable    .setItems(FXCollections.observableArrayList(sMetrics));

        priorityAvgWT .setText(fmt(MetricsCalculator.averageWT (pMetrics)));
        priorityAvgTAT.setText(fmt(MetricsCalculator.averageTAT(pMetrics)));
        priorityAvgRT .setText(fmt(MetricsCalculator.averageRT (pMetrics)));
        srtfAvgWT     .setText(fmt(MetricsCalculator.averageWT (sMetrics)));
        srtfAvgTAT    .setText(fmt(MetricsCalculator.averageTAT(sMetrics)));
        srtfAvgRT     .setText(fmt(MetricsCalculator.averageRT (sMetrics)));

        drawGantt(priorityCanvas, pGantt);
        drawGantt(srtfCanvas,     sGantt);

        buildSummary(pMetrics, sMetrics);
    }

    @FXML private void onScenarioA() {
        loadScenario(new String[][]{
                {"P1", "0", "8", "2"},
                {"P2", "1", "4", "1"},
                {"P3", "2", "9", "3"},
                {"P4", "3", "5", "2"},
                {"P5", "4", "2", "1"}
        });
    }

    @FXML private void onScenarioB() {
        loadScenario(new String[][]{
                {"P1", "0", "12", "1"},
                {"P2", "2", "2", "3"},
                {"P3", "3", "4", "3"},
                {"P4", "5", "1", "4"}
        });
    }

    @FXML private void onScenarioC() {
        loadScenario(new String[][]{
                {"P1","0","10","3"},
                {"P2","1","1","1"},
                {"P3","2","1","1"},
                {"P4","3","1","1"},
                {"P5","4","1","1"},
                {"P6","5","1","1"},
        });
    }

    @FXML private void onScenarioD() {
        loadScenario(new String[][]{
                {"", "0", "5", "1"},
                {"P2", "-1",  "4", "1"},
                {"P3", "0",  "0", "3"}
        });
    }

    private void loadScenario(String[][] data) {
        processRowsContainer.getChildren().remove(0, dataRowCount());
        resetResults();

        int insertAt = processRowsContainer.getChildren().size() - 1;
        for (String[] cols : data) {
            HBox row = buildRow();
            for (int i = 0; i < cols.length && i < 4; i++) {
                ((TextField) row.getChildren().get(i)).setText(cols[i]);
            }
            processRowsContainer.getChildren().add(insertAt++, row);
        }
    }

    private List<process> collectProcesses() {
        List<process> list = new ArrayList<>();
        for (int i = 0; i < dataRowCount(); i++) {
            HBox row = dataRow(i);
            if (row.getChildren().size() < 4) continue;

            String id      = text(row, 0);
            String arrival = text(row, 1);
            String burst   = text(row, 2);
            String prio    = text(row, 3);

            if (id.isEmpty() && arrival.isEmpty() && burst.isEmpty() && prio.isEmpty()) continue;

            try {
                int at = arrival.isEmpty() ? 0 : Integer.parseInt(arrival);
                int bt = burst  .isEmpty() ? 0 : Integer.parseInt(burst);
                int pr = prio   .isEmpty() ? 1 : Integer.parseInt(prio);
                list.add(new process(id, at, bt, pr));
            } catch (NumberFormatException e) {
                showError("Row " + (i + 1) + ": Arrival, Burst and Priority must be integers.");
                return null;
            }
        }
        return list;
    }

    private String text(HBox row, int col) {
        if (row.getChildren().get(col) instanceof TextField tf)
            return tf.getText().trim();
        return "";
    }

    private void drawGantt(Canvas canvas, List<ganttEntry> chart) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w  = canvas.getWidth();
        double h  = canvas.getHeight();
        gc.clearRect(0, 0, w, h);

        if (chart == null || chart.isEmpty()) return;

        int totalTime = chart.get(chart.size() - 1).getEndTime();

        if (totalTime == 0) return;

        List<String> ids = new ArrayList<>();
        for (ganttEntry e : chart) {
            if (!e.getId().equals("idle") && !ids.contains(e.getId()))
                ids.add(e.getId());
        }

        double padding   = 14;
        double barTop    = 8;
        double barHeight = 52;
        double timeY     = barTop + barHeight + 12;
        double scale     = ((w - 2 * padding) / totalTime);

        for (ganttEntry e : chart) {
            double x    = padding + e.getStartTime() * scale;
            double bw   = e.getDuration() * scale - 2;

            if (e.getId().equals("idle")) {
                gc.setFill(Color.web("#1a1a24"));
            } else {
                gc.setFill(PALETTE[ids.indexOf(e.getId()) % PALETTE.length]);
            }
            gc.fillRoundRect(x, barTop, bw, barHeight, 14, 14);

            gc.setStroke(Color.web("#0c0c0f"));
            gc.setLineWidth(1.5);
            gc.strokeRoundRect(x, barTop, bw, barHeight, 4, 4);
            gc.setStroke(Color.rgb(255,255,255,0.35));

            if (bw >= 18) {
                gc.setFill(e.getId().equals("idle") ? Color.web("#7a7a8c") : Color.web("#0c0c0f"));
                gc.setFont(Font.font("JetBrains Mono", FontWeight.BOLD, 11));
                String label = e.getId();
                double tw    = label.length() * 6.5;
                gc.fillText(label, x + (bw - tw) / 2, barTop + barHeight / 2.0 + 4);
            }

            gc.setFill(Color.web("#7a7a8c"));
            gc.setFont(Font.font("JetBrains Mono", 9));
            gc.fillText(String.valueOf(e.getStartTime()), x, timeY);
        }

        ganttEntry last = chart.get(chart.size() - 1);
        double lastX = padding + last.getEndTime() * scale;
        gc.setFill(Color.web("#7a7a8c"));
        gc.setFont(Font.font("JetBrains Mono", 9));
        gc.fillText(String.valueOf(last.getEndTime()), lastX - 4, timeY);
    }

    private void buildSummary(List<processMetrics> pM, List<processMetrics> sM) {
        double pWT  = MetricsCalculator.averageWT (pM);
        double pTAT = MetricsCalculator.averageTAT(pM);
        double pRT  = MetricsCalculator.averageRT (pM);
        double sWT  = MetricsCalculator.averageWT (sM);
        double sTAT = MetricsCalculator.averageTAT(sM);
        double sRT  = MetricsCalculator.averageRT (sM);

        int srtfWins = 0;
        if (sWT  <= pWT)  srtfWins++;
        if (sTAT <= pTAT) srtfWins++;
        if (sRT  <= pRT)  srtfWins++;
        String winner = srtfWins >= 2 ?
                "SRTF Scheduling is recommended for throughput-oriented workloads. It\n" +
                "minimizes average waiting and response time. However,\n" +
                "Priority Scheduling should be used when urgency/criticality\n" +
                "must be respected." :
                "Priority Scheduling performs better for this dataset. The\n" +
                "priority assignments align well with burst times, providing\n" +
                "good turnaround.\n";

        StringBuilder sb = new StringBuilder();
        sb.append("╔═════════════════════════════════════════════╗\n");
        sb.append("║                      ALGORITHM COMPARISON SUMMARY                         ║\n");
        sb.append("╚═════════════════════════════════════════════╝\n\n");
        sb.append(comparisonAnalyzer.analysis(pWT, sWT, pTAT, sTAT, pRT, sRT));
        sb.append("\n\n-------ANALYSIS-----------------------------------------------------------------\n\n");
        sb.append(comparisonAnalyzer.q1(pWT, sWT));
        sb.append(comparisonAnalyzer.q2(pRT, sRT));
        sb.append("Q3. Did priority values improve treatment of urgent processes?\n");
        sb.append("\t→ Yes. Priority Scheduling ensures high-priority processes always\n");
        sb.append("\t get CPU first, regardless of burst length. This benefits urgent\n");
        sb.append("\t or critical tasks.\n\n");
        sb.append("Q4. Did SRTF favor short jobs more aggressively?\n");
        sb.append("\t→ Yes. SRTF exclusively selects the shortest remaining job, giving\n");
        sb.append("\t short processes near-immediate service while potentially\n");
        sb.append("\t starving long ones.\n\n");
        sb.append("Q5.Which algorithm would you recommend for the tested workload, and why?\n");
        sb.append(winner);
        sb.append("\n\n-------CONCLUSION-----------------------------------------------------------------\n\n");
        sb.append("  • SRTF is optimal for minimizing average waiting and turnaround time.\n");
        sb.append("  • Priority Scheduling is better when process urgency must be respected.\n");
        sb.append("  • Main Trade-off: SRTF efficiency vs. Priority policy-driven fairness.\n");
        sb.append("  • Starvation Risk: Both algorithms can starve processes — SRTF starves\n");
        sb.append("    long processes; Priority Scheduling starves low-priority processes.\n");
        sb.append("  • Fairer in practice: Priority Scheduling (if priorities are well-assigned).\n");

        summaryArea.setText(sb.toString());
    }

    private static String fmt(double v) { return String.format("%.2f", v); }


    private void showError(String msg) {
        errorbox.setStyle("-fx-background-color: #1a0a0a;");
        errormsg.setText("⚠  Error: " + msg);
        errormsg.setStyle("-fx-text-fill: #ff6b6b");
    }

    private void resetResults() {
        priorityTable.getItems().clear();
        srtfTable    .getItems().clear();
        priorityAvgWT .setText("--");
        priorityAvgTAT.setText("--");
        priorityAvgRT .setText("--");
        srtfAvgWT     .setText("--");
        srtfAvgTAT    .setText("--");
        srtfAvgRT     .setText("--");
        summaryArea   .setText("Run simulation to see results");
        clearCanvas(priorityCanvas);
        clearCanvas(srtfCanvas);
    }

    private void clearCanvas(Canvas c) {
        c.getGraphicsContext2D().clearRect(0, 0, c.getWidth(), c.getHeight());
    }
}