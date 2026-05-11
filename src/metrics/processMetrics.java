package metrics;

public class processMetrics {
    private String id;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int completionTime;
    private int waitingTime; // tat - burst
    private int turnaroundTime; // completion time - arrival time
    private int responseTime; // first time on cpu - arrival time

    public processMetrics(String id, int arrivalTime, int burstTime, int priority, int completionTime, int responseTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.completionTime = completionTime;
        this.responseTime = responseTime;
        this.turnaroundTime = completionTime - arrivalTime;
        this.waitingTime = this.turnaroundTime - burstTime;
    }

    public String getId() {
        return id;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public int getPriority() {
        return priority;
    }
    public int getCompletionTime() {
        return completionTime;
    }
    public int getWaitingTime() {
        return waitingTime;
    }
    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    public int getResponseTime() {
        return responseTime;
    }

    @Override
    public String toString(){
        return String.format("Process:[ id:%s" + " arrivalTime:%d" + " burstTime:%d" + " priority:%d" + " completionTime:%d" + " waitingTime:%d" + " turnaroundTime:%d" + " responseTime:%d]", id,
                arrivalTime,
                burstTime,
                priority,
                completionTime,
                waitingTime,
                turnaroundTime,
                responseTime);
    }
}
