package model;

public class ganttEntry {
    private String id;
    private int startTime;
    private int endTime; 

    public ganttEntry(String id, int startTime, int endTime){
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public String getId() {
        return id;
    }
    public int getStartTime() {
        return startTime;
    }
    public int getEndTime() {
        return endTime;
    }

    public int getDuration(){
        return endTime - startTime;
    }

    @Override
    public String toString(){
        return String.format("ganttEntry:[id=%s, startTime=%d, endTime=%d]", id, startTime, endTime);
    }
}
