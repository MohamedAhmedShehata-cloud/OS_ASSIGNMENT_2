package main.java.os.model;

/**
 * ============================================================================
 * OWNER: Youssef Samir (Data Models & Metrics)
 * RESPONSIBILITY: Core process entity representing immutable specs and mutable state
 * ============================================================================
 */
public class Process {
    // Immutable specifications
    private final String id;
    private final int arrivalTime;
    private final int burstTime;
    private final int initialPriority;

    // Dynamic state during simulation
    private int remainingTime;
    private int effectivePriority;
    private SchedulerTier currentTier;
    private ProcessState state;

    // Performance timing metrics
    private int firstExecutionTime = -1;
    private int finishTime = -1;
    private int tier2ArrivalTime = -1;

    /**
     * Initializes a new process with its immutable specifications and default initial state.
     *
     * @param id unique identifier of the process
     * @param arrivalTime the timestamp at which the process enters the system
     * @param burstTime total CPU execution time required to complete
     * @param initialPriority base priority assigned to the process
     */
    public Process(String id, int arrivalTime, int burstTime, int initialPriority) {
        if(id==null|| id.isBlank())
        {
             throw new IllegalArgumentException("ID cannot be null or empty");
        }
       
        if(arrivalTime<0)
        { 
            throw new IllegalArgumentException("Arrival time cannot be less than 0");
        }
        
        if(burstTime<=0)
        {
           throw new IllegalArgumentException("Burst time must be greater than 0");
        }

        this.id=id; 
        this.arrivalTime=arrivalTime;
        this.burstTime=burstTime;
        this.initialPriority=initialPriority;

        this.remainingTime = burstTime;
        this.effectivePriority = initialPriority;
        this.currentTier= SchedulerTier.NONE;
        this.state=ProcessState.READY;
    
    }

    /**
     * Executes one time unit on the CPU, decreasing remaining burst time and recording first execution time.
     *
     * @param currentTime the current simulation clock tick
     */
    public void executeOneUnit(int currentTime) {
    if (isCompleted()) {
        return;
    }

    if (firstExecutionTime == -1) {
        firstExecutionTime = currentTime;
    }

    remainingTime--;

    if (isCompleted()) {
        state = ProcessState.TERMINATED;
    }
}

    /**
     * Checks if the process has finished all of its required CPU burst time.
     *
     * @return true if remaining time is zero, false otherwise
     */
    public boolean isCompleted() {
        return remainingTime==0;
      
    }

    /**
     * Retrieves the process identifier.
     *
     * @return the unique process ID string
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the arrival time of the process.
     *
     * @return arrival time in clock ticks
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Retrieves the total CPU burst time required by the process.
     *
     * @return total burst time in clock ticks
     */
    public int getBurstTime() {
        return burstTime;
    }

    /**
     * Retrieves the initial priority of the process.
     *
     * @return initial priority value
     */
    public int getInitialPriority() {
        return initialPriority;
    }

    /**
     * Retrieves the remaining burst time needed to complete execution.
     *
     * @return remaining CPU time in clock ticks
     */
    public int getRemainingTime() {
       return remainingTime;
    }

    /**
     * Updates the remaining burst time of the process.
     *
     * @param remainingTime the updated remaining burst time
     */
    public void setRemainingTime(int remainingTime) {
    if (remainingTime < 0 || remainingTime > burstTime) {
        throw new IllegalArgumentException("Invalid remaining time");
    }

    this.remainingTime = remainingTime;
}

    /**
     * Retrieves the effective priority after applying aging policies.
     *
     * @return current effective priority value
     */
    public int getEffectivePriority() {
      return effectivePriority;
    }

    /**
     * Updates the effective priority of the process.
     *
     * @param effectivePriority the updated effective priority
     */
    public void setEffectivePriority(int effectivePriority) {
        this.effectivePriority=effectivePriority;
    }

    /**
     * Retrieves the current scheduling tier of the process.
     *
     * @return current SchedulerTier
     */
    public SchedulerTier getCurrentTier() {
       return currentTier;
    }

    /**
     * Sets the scheduling tier of the process (e.g., when demoting from Tier 1 to Tier 2).
     *
     * @param currentTier the new scheduling tier
     */
    public void setCurrentTier(SchedulerTier currentTier) {
       this.currentTier=currentTier;
    }

    /**
     * Retrieves the current execution lifecycle state.
     *
     * @return current ProcessState
     */
    public ProcessState getState() {
        return state;
    }

    /**
     * Updates the lifecycle state of the process.
     *
     * @param state the new ProcessState
     */
    public void setState(ProcessState state) {
       this.state=state;
    }

    /**
     * Retrieves the first timestamp at which the process was dispatched to the CPU.
     *
     * @return first execution timestamp, or -1 if never executed yet
     */
    public int getFirstExecutionTime() {
       return firstExecutionTime;
    }

    /**
     * Retrieves the completion timestamp of the process.
     *
     * @return finish timestamp, or -1 if not yet finished
     */
    public int getFinishTime() {
       return finishTime;
    }

    /**
     * Sets the timestamp at which the process completed execution.
     *
     * @param finishTime completion timestamp
     */
    public void setFinishTime(int finishTime) {
       this.finishTime=finishTime;
    }

    /**
     * Retrieves the timestamp at which the process was demoted and placed into Tier 2.
     *
     * @return timestamp of entry into Tier 2, or -1 if never demoted
     */
    public int getTier2ArrivalTime() {
      return tier2ArrivalTime;
    }

    /**
     * Sets the timestamp at which the process entered Tier 2 (used for aging calculation).
     *
     * @param tier2ArrivalTime entry timestamp into Tier 2
     */
    public void setTier2ArrivalTime(int tier2ArrivalTime) {
       this.tier2ArrivalTime=tier2ArrivalTime;
    }
}