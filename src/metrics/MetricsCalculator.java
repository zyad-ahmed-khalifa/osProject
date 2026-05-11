package metrics;

import model.ganttEntry;
import model.process;

import java.util.ArrayList;
import java.util.List;

public class MetricsCalculator {

    public static List<processMetrics> calculate(List<process> processes, List<ganttEntry> ganttChart){
        List<processMetrics> results = new ArrayList<>();
        for (process p : processes){
            int firstCpuTime = findFirstCpuTime(p.getId(), ganttChart);
            int responseTime = firstCpuTime - p.getArrivalTime();
            processMetrics m = new processMetrics(
                    p.getId(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getPriority(),
                    p.getCompletionTIme(),
                    responseTime);
            results.add(m);
        }
        return results;
    }

    private static int findFirstCpuTime(String id, List<ganttEntry> ganttChart){
        for(ganttEntry g: ganttChart){
            if(g.getId().equals(id)){
                return g.getStartTime();
            }
        }
        return -1;
    }

    public static double averageTAT(List<processMetrics> metrics){
        return metrics.stream().mapToInt(processMetrics::getTurnaroundTime).average().orElse(0);
    }
    public static double averageWT(List<processMetrics> metrics){
        return metrics.stream().mapToInt(processMetrics::getWaitingTime).average().orElse(0);
    }
    public static double averageRT(List<processMetrics> metrics){
        return metrics.stream().mapToInt(processMetrics::getResponseTime).average().orElse(0);
    }


}
