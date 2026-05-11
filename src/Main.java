
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import metrics.MetricsCalculator;
import metrics.processMetrics;
import model.*;
import scheduler.SRTFScheduler;
import util.inputValidator;
import scheduler.priorityScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1400, 960);
        scene.getStylesheets().add(getClass().getResource("style/style.css").toExternalForm());

        stage.setTitle("CPU Scheduling Simulator — Priority vs SRTF");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

    }
}