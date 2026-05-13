# CPU Scheduling Simulator
## Priority Scheduling vs. SRTF — Operating Systems Project

---

## Team Information
| # | Name | submition commits |
|---|------|------|
| 1 | [zyad ahmed khalifa ahmed] | Team Lead + controller + main + testcases + screenshots |
| 2 | [amr mohamed fathy] | scheduler |
| 3 | [abdallah mohamed faroq] | model |
| 4 | [rami magdy tawadros] | utils|
| 5 | [khaled ahmed saleh] | metrics |
| 6 | [shaza mohamed] | analysis |
| 7 | [lakaa ahmed] | view |

---

## Project Description

This application implements and visually compares two CPU scheduling algorithms:

1. **Priority Scheduling (Preemptive)** — Always runs the highest-priority process. Lower priority number = higher priority. Ties are broken by earlier arrival time.
2. **SRTF (Shortest Remaining Time First)** — Always runs the process with the least remaining burst time. Preempts immediately when a shorter job arrives.

The simulator accepts a dynamic number of processes, validates all input, runs both algorithms on the same workload, and displays:
- Side-by-side color-coded Gantt charts
- Per-process metrics tables (CT, TAT, WT, RT)
- Average metrics comparison
- Automated analysis and conclusion

---

## Requirements

- **Java 17** or higher
- **JavaFX 21** (included via Maven)
- **Maven 3.8+**

---

## How to Build and Run

### Option 1: Using Maven (Recommended)
```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/scheduling-simulator.git
cd scheduling-simulator

# Build
mvn clean package

# Run
mvn javafx:run
```

### Option 2: IntelliJ IDEA
1. Open the project folder in IntelliJ IDEA
2. Maven will auto-import dependencies
3. Run `MainApp.java` (in `src/main/java/com/scheduler/gui/`)

### Option 3: VS Code
1. Install "Extension Pack for Java" and "JavaFX Support"
2. Open the folder, let Maven import
3. Run `MainApp.java`

---

## Package Structure

```
src/main/java/com/scheduler/
├── analysis
│   └── comparisonAnalyzer.java
├── controller
│   └── controller.java
├── metrics
│   ├── MetricsCalculator.java
│   └── processMetrics.java
├── model
│   ├── ganttEntry.java
│   └── process.java
├── scheduler
│   ├── SRTFScheduler.java
│   └── priorityScheduler.java
├── style
│   └── style.css
├── util
│   └── inputValidator.java
├── view
│   └── Main.fxml
└── Main.java
```

---

## Algorithms

### Priority Scheduling (Preemptive)
- **Priority Rule:** Lower value = higher priority
- **Tie-breaking:** Earlier arrival time; if equal, lexicographic process ID
- **Preemption:** Occurs when a higher-priority process enters the ready queue
- **Starvation risk:** Low-priority processes may wait indefinitely

### SRTF
- **Selection Rule:** Process with minimum remaining burst time
- **Tie-breaking:** Earlier arrival time; if equal, lexicographic process ID  
- **Preemption:** Occurs when a newly arrived process has a shorter burst than the current remaining time
- **Optimality:** Minimizes average waiting time among all preemptive algorithms

---

## Input Format

| Field | Type | Constraints |
|---|---|---|
| Process ID | String | Non-empty, unique |
| Arrival Time | Integer | ≥ 0 |
| Burst Time | Integer | > 0 |
| Priority | Integer | ≥ 0 (lower = higher priority) |

---

## Metrics

| Metric | Formula |
|---|---|
| Completion Time (CT) | Time when process finishes |
| Turnaround Time (TAT) | CT − Arrival Time |
| Waiting Time (WT) | TAT − Burst Time |
| Response Time (RT) | First CPU time − Arrival Time |

---

## Test Scenarios

See `test-cases/test-scenarios.md` for:
- **Scenario A:** Basic mixed workload
- **Scenario B:** High-priority long process vs low-priority short processes
- **Scenario C:** Starvation-sensitive case
- **Scenario D:** Validation error case

---

## Assumptions

1. Time unit = 1 (unit-step simulation)
2. Context switch overhead = 0
3. Preemption is immediate (same time unit)
4. All processes are CPU-bound (no I/O)
5. Priority 0 = highest possible priority

---

## Limitations

1. No aging mechanism (starvation is intentionally demonstrable)
2. No I/O bursts or multi-level queues
3. Unit-step simulation (not event-driven, so large burst times take proportional computation)

