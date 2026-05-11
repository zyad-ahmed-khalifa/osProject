# Test Cases — CPU Scheduling Simulator

## Scenario A: Basic Mixed Workload

| Process | Arrival | Burst | Priority |
|---------|---------|-------|----------|
| P1      | 0       | 8     | 2        |
| P2      | 1       | 4     | 1        |
| P3      | 2       | 9     | 3        |
| P4      | 3       | 5     | 2        |
| P5      | 4       | 2     | 1        |

**Purpose:** Tests normal behavior with varied arrival times, burst times, and priorities.

### Expected Priority Results:
- P1 starts at 0, preempted at t=1 when P2 arrives (priority 1 > priority 2)
- P2 runs until done, then P5 (also priority 1), then P1 resumes, then P4, then P3

### Expected SRTF Results:
- P1 starts at 0, preempted at t=1 when P2 arrives (BT=4 < remaining=7)
- P5 preempts when it arrives (BT=2 is shortest)

---

## Scenario B: Priority vs Burst Conflict

| Process | Arrival | Burst | Priority |
|---------|---------|-------|----------|
| P1      | 0       | 12    | 1        |
| P2      | 2       | 2     | 3        |
| P3      | 3       | 4     | 3        |
| P4      | 5       | 1     | 4        |

**Purpose:** P1 has highest priority but longest burst. Shows how the two algorithms diverge:
- Priority: P1 runs exclusively until done (never preempted — it's always highest priority)
- SRTF: P2, P3, P4 preempt P1 because they have shorter remaining time

### Key Observation:
Priority Scheduling is unfair to P2/P3/P4 (they wait a long time despite being short).
SRTF is unfair to P1 (it keeps getting preempted despite being "most important").

---

## Scenario C: Starvation-Sensitive Case

| Process | Arrival | Burst | Priority |
|---------|---------|-------|----------|
| P1      | 0       | 10    | 3        |
| P2      | 1       | 1     | 1        |
| P3      | 2       | 1     | 1        |
| P4      | 3       | 1     | 1        |
| P5      | 4       | 1     | 1        |
| P6      | 5       | 1     | 1        |

**Purpose:** P1 has low priority (3) and long burst (10). P2-P6 keep arriving with high priority (1).

- Priority Scheduling: P1 gets preempted every time a new P* arrives. P1 waits a VERY long time → **starvation demonstrated**
- SRTF: P1 gets preempted by short-burst arrivals → similar starvation for different reason

---

## Scenario D: Validation Case (Invalid Input)

| Process | Arrival | Burst | Priority |
|---------|---------|-------|----------|
| (empty) | 0       | 5     | 1        |
| P2      | -1      | 4     | 2        |
| P3      | 0       | 0     | 3        |

**Expected Validation Errors:**
- Row 1: Process ID cannot be empty
- Row 2: Arrival time must be ≥ 0  
- Row 3: Burst time must be > 0

The simulator catches these errors before simulation and displays descriptive messages.

---

## How to Verify Results Manually

For each process after simulation:
- **CT** = time when process finishes (read from Gantt)
- **TAT** = CT − Arrival Time
- **WT** = TAT − Burst Time  
- **RT** = First time on CPU − Arrival Time

Averages = sum / count of processes
