package scheduler;

import model.ganttEntry;
import model.process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SRTFScheduler {
    private List<process> processes;
    private List<ganttEntry> ganttChart;

    public SRTFScheduler(List<process> processes){
        this.processes = new ArrayList<>();
        for (process p: processes){
            this.processes.add(new process(p));
        }
        this.ganttChart = new ArrayList<>();
    }

    public List<process> simulate(){

        int time = 0;
        int completed = 0;
        int n = processes.size();
        process currentProcess = null;
        String lastProcessId = null;
        int sliceStart = 0;

        int totalBurst = processes.stream().mapToInt(process::getBurstTime).sum();
        int maxArrival = processes.stream().mapToInt(process::getArrivalTime).max().orElse(0);
        int maxTime = totalBurst + maxArrival +1;

        while(completed < n && time <= maxTime){
            final int t = time ;
            List<process> readyQueue = new ArrayList<>();
            for (process p: processes){
                if (p.getArrivalTime() <= t && p.getRemainingTime() > 0){
                    readyQueue.add(p);
                }
            }

            if (readyQueue.isEmpty()){
                if (currentProcess!= null){
                    ganttChart.add((new ganttEntry(lastProcessId, sliceStart, time)));
                    currentProcess= null;
                    lastProcessId = null;
                }

                int idleStart = time;
                time++;
                ganttChart.add(new ganttEntry("idle", idleStart, time));
                continue;
            }

            process selected = readyQueue.stream().min(
                    Comparator.comparingInt(process::getRemainingTime)
                            .thenComparingInt(process::getArrivalTime)
                            .thenComparing(process::getId)
            ).orElse(null);

            if(currentProcess != selected){
                if (currentProcess != null && lastProcessId != null){
                    ganttChart.add(new ganttEntry(lastProcessId, sliceStart, time));
                }
                if(selected != null && !selected.isStarted()){
                    selected.setStartTime(time);
                }
                currentProcess = selected;
                lastProcessId = selected != null ? selected.getId() : null;
                sliceStart = time;
            }

            if(selected != null){
                selected.setRemainingTime(selected.getRemainingTime()-1);
                time++;
                if(selected.getRemainingTime() == 0){
                    selected.setCompletionTIme(time);
                    ganttChart.add(new ganttEntry(selected.getId(), sliceStart, time));
                    completed++;
                    currentProcess =null;
                    lastProcessId = null;
                    sliceStart = time;

                }
            }else{
                time++;
            }
        }
        ganttChart = mergeGanttChart(ganttChart);
        return processes;
    }

    public List<ganttEntry> mergeGanttChart(List<ganttEntry> raw){
        ArrayList<ganttEntry> merged = new ArrayList<>();
        for(ganttEntry gantt: raw){
            if(!merged.isEmpty()){
                ganttEntry last = merged.get(merged.size() -1);
                if(gantt.getId().equals(last.getId()) && last.getEndTime() == gantt.getStartTime()){
                    merged.set(merged.size()-1, new ganttEntry(last.getId(), last.getStartTime(), gantt.getEndTime()));
                    continue;
                }
            }
            merged.add(gantt);
        }
        return merged;
    }

    public List<ganttEntry> getGanttChart(){
        return ganttChart;
    }
    public List<process> getProcesses(){
        return processes;
    }

}
