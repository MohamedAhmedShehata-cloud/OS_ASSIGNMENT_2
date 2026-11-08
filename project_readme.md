# Cloud-Cluster CPU Scheduling Simulator

> **Faculty of Computers and Artificial Intelligence, Cairo University (FCAI-CU)**
>
> **Course:** CS241: Operating Systems - 1 (Fall 2026)
>
> **Assignment:** Assignment 2: Cloud-Cluster CPU Scheduling

## 📌 Project Overview

This project is a Java-based discrete-event operating system simulator designed to manage a cloud-cluster node running heterogeneous containerized workloads. The system balances latency-sensitive, interactive jobs (such as web servers) against CPU-heavy batch workloads (such as PyTorch model training) through advanced scheduling algorithms and context switching management.

## ⚙️ Implemented Scheduling Algorithms

### 1. Preemptive Shortest-Job First (SJF / SRTF)

* The process with the shortest remaining execution time always preempts the running process.

* Context Switching ($CS$) overhead is rigorously tracked on every preemption event.

### 2. Adaptive Container Scheduler (ACS)

A multi-tier adaptive scheduler tailored for mixed workloads:

* **Tier 1 (Initial Round Robin):** All incoming processes enter a Round Robin queue configured with a static Time Quantum ($Q$) to guarantee minimal Response Time for interactive tasks.

* **Tier 2 (Demotion to Non-Preemptive Priority):** If a process consumes its full quantum $Q$ without finishing, it is flagged as a CPU-bound batch job and demoted to Tier 2. Once scheduled in Tier 2, the process runs non-preemptively until completion.

* **Tier 3 (Aging Policy):** Mitigates starvation for low-priority processes waiting in Tier 2. The effective priority is dynamically adjusted using:
  

  $$
  \text{Effective Priority} = \text{Initial Priority} - \left\lfloor \frac{\text{Wait Time}}{4} \right\rfloor
  $$

  
  *(Lower numerical value signifies higher execution priority).*

## 👥 Team Members & Responsibilities Matrix

| Member | Role | Assigned Files & Modules | Core Responsibilities | 
| ----- | ----- | ----- | ----- | 
| **Mamdouh** | System Architect & ACS Engine | • `os/scheduler/SchedulingStrategy.java`  • `os/scheduler/acs/ACSScheduler.java`  • `os/scheduler/acs/Tier1Queue.java`  • `os/scheduler/acs/Tier2Queue.java`  • `os/scheduler/acs/AgingPolicy.java`  • `os/engine/SimulationEngine.java` | System architecture, Strategy pattern blueprint, multi-tier queue coordination, aging logic, simulation loop. | 
| **Mohamed** | SJF & Context Switch Engine | • `os/scheduler/SJFScheduler.java`  • `os/engine/ContextSwitchManager.java`  • `os/engine/SimulationClock.java`  • `os/Main.java` | Preemptive SJF logic, state-saving CPU suspension during $CS$, discrete clock management, CLI entry point. | 
| **Omar** | GUI & Visualization Specialist *(Bonus)* | • `os/gui/SimulatorApp.java`  • `os/gui/controller/DashboardController.java`  • `os/gui/view/GanttChartPanel.java`  • `os/gui/view/QueueStatePanel.java`  • `resources/views/dashboard.fxml` | JavaFX / Swing interactive UI, live Gantt chart with color-coded $CS$ slots, queue monitors, playback controls. | 
| **Youssef** | Data Models, Metrics & IO | • `os/model/Process.java`  • `os/model/ProcessState.java`  • `os/model/SchedulerTier.java`  • `os/observer/SimulationObserver.java`  • `os/observer/ConsoleLogger.java`  • `os/observer/MetricsCollector.java`  • `os/metrics/ProcessMetrics.java`  • `os/metrics/SystemMetricsReport.java`  • `os/input/TestCase.java`  • `os/input/TestCaseFactory.java` | Process lifecycle model, Observer pattern infrastructure, terminal demotion matrix logging, WT/TAT/RT metrics computation. | 
| **Ahmed** | Quality Assurance, Analysis & Reporting | • `resources/testcases/testcase1.json`  • `resources/testcases/testcase2.json`  • `Report.pdf` | Benchmark testing across varying $Q$ and $CS$ values, trade-off analysis graphs (Excel/Python), report writing, archive packaging. | 

## 🏗️ Project Architecture & File Hierarchy

```
src/
└── main/
    ├── java/
    │   └── os/
    │       ├── Main.java
    │       │
    │       ├── model/
    │       │   ├── Process.java
    │       │   ├── ProcessState.java
    │       │   └── SchedulerTier.java
    │       │
    │       ├── scheduler/
    │       │   ├── SchedulingStrategy.java
    │       │   ├── SJFScheduler.java
    │       │   └── acs/
    │       │       ├── ACSScheduler.java
    │       │       ├── Tier1Queue.java
    │       │       ├── Tier2Queue.java
    │       │       └── AgingPolicy.java
    │       │
    │       ├── engine/
    │       │   ├── SimulationEngine.java
    │       │   ├── SimulationClock.java
    │       │   └── ContextSwitchManager.java
    │       │
    │       ├── observer/
    │       │   ├── SimulationObserver.java
    │       │   ├── ConsoleLogger.java
    │       │   └── MetricsCollector.java
    │       │
    │       ├── metrics/
    │       │   ├── ProcessMetrics.java
    │       │   └── SystemMetricsReport.java
    │       │
    │       ├── input/
    │       │   ├── TestCase.java
    │       │   └── TestCaseFactory.java
    │       │
    │       └── gui/                     // Bonus Application
    │           ├── SimulatorApp.java
    │           ├── controller/
    │           │   └── DashboardController.java
    │           └── view/
    │               ├── GanttChartPanel.java
    │               └── QueueStatePanel.java
    │
    └── resources/
        ├── testcases/
        │   ├── testcase1.json
        │   └── testcase2.json
        └── views/
            └── dashboard.fxml

```

## 🚀 Getting Started & Execution

### Prerequisites

* **Java Development Kit (JDK):** Version 17 or higher

* **Build Tool:** Maven (or pure `javac` compilation)

* **JavaFX SDK:** (Optional, required only for GUI execution)

### 1. Build the Project

```
# Using Maven
mvn clean compile

# Or compiling manually with javac
javac -d bin $(find src/main/java -name "*.java")

```

### 2. Run the CLI Simulation (Standard Output)

Executes `Test Case 1` and `Test Case 2` with full per-tick logging and performance summary tables:

```
# Using Maven
mvn exec:java -Dexec.mainClass="os.Main"

# Or using plain Java
java -cp bin os.Main

```

### 3. Run the GUI Dashboard (Bonus)

```
# Using Maven JavaFX plugin
mvn javafx:run

# Or running the GUI class directly
java -cp bin os.gui.SimulatorApp

```

## 📊 Sample Output & Verification

### Per-Tick Heuristic Matrix Logging (Test Case 2 Sample):

```
Time | Running | Tier 1 Queue              | Tier 2 Queue
----------------------------------------------------------------------------------------
0    | P1      | [P1 (rem: 5, prio: 4)]     | [Empty]
1    | P1      | [P2 (rem: 6, prio: 2)]     | [Empty]
3    | [CS]    | Context switch saves P1 and loads P2
4    | P2      | [P3 (rem: 4, prio: 3)]     | [P1 (rem: 2, prio: 4)]
...
10   | [AGING] | P1 waited 8 units -> Effective Priority = 4 - 2 = 2
     |         | P2 waited 4 units -> Effective Priority = 2 - 1 = 1

```

### Final Performance Metrics Summary:

$$
\text{Turnaround Time (TAT)} = \text{Finish Time} - \text{Arrival Time}
$$

$$
\text{Waiting Time (WT)} = \text{Turnaround Time} - \text{Burst Time}
$$

$$
\text{Response Time (RT)} = \text{First Run Time} - \text{Arrival Time}
$$

| Process | Arrival Time | Finish Time | Turnaround Time | Waiting Time | Response Time | 
| ----- | ----- | ----- | ----- | ----- | ----- | 
| **P1** | 0 | 18 | 18 | 13 | 0 | 
| **P2** | 1 | 15 | 14 | 8 | 3 | 
| **P3** | 4 | 20 | 16 | 12 | 7 | 
| **Average** | \- | \- | **16.00** | **11.00** | **3.33** | 

## 📦 Submission Compliance

* Submission archive follows the mandated naming convention:
  `A2_TAname_Group_ID1_ID2_ID3_ID4.zip`

* Contains clean source code under `src/`.

* Includes `Report.pdf` with analytical discussion of Tier 2 demotion trade-offs ($Q$ vs $CS$ sensitivity curves).