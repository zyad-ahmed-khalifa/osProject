package model;

public class process {
    private String id;
    private int arrivalTime;
    private int burstTime;
    private int priority;

    private int remainingTime;
    private int startTime;
    private int completionTIme;
    private boolean started;

    public process(String id, int arrivalTime, int burstTime, int priority){
        this.id =id;
        this.arrivalTime= arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.startTime = -1;
        this.completionTIme = -1;
        this.started = false;
    }

    public process(process obj){
        this.id = obj.id;
        this.arrivalTime = obj.arrivalTime;
        this.burstTime = obj.burstTime;
        this.priority = obj.priority;
        this.remainingTime = obj.burstTime;
        this.startTime = -1;
        this.completionTIme = -1;
        this.started = false;
    }

    public void rest(){
        this.remainingTime =this.burstTime;
        this.startTime = -1;
        this.completionTIme =-1;
        this.started = false;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getRemainingTime() {
        return remainingTime;
    }
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
    public int getStartTime() {
        return startTime;
    }
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
    public int getCompletionTIme() {
        return completionTIme;
    }
    public void setCompletionTIme(int completionTIme) {
        this.completionTIme = completionTIme;
    }
    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public String toString(){
        return String.format("Process:[id=%s, ArrivalTime=%d, burstTime=%d, priority=%d]", id, arrivalTime, burstTime, priority);
    }
}
