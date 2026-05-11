package util;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class inputValidator {

    public static class validationResult{
        public boolean valid;
        public String message;
        public validationResult(boolean valid, String message){
            this.valid =valid;
            this.message = message;
        }
    }


    public static validationResult validate(List<process> processes){
        if (processes.isEmpty()){
            return new validationResult(false, "no processes!");
        }

        for (process p : processes){
            if(p.getId().equals("")){
                return new validationResult(false, "id of process "+(processes.indexOf(p)+1)+" is not found");
            }
        }

        ArrayList<String> ids = new ArrayList<>();
        for (process p : processes){
            if (ids.contains(p.getId().trim())){
                return new validationResult(false, "duplicated id " + p.getId() + " is found, id must be unique");
            }
            ids.add(p.getId().trim());
        }

        for (process p : processes){
            if(!(p.getArrivalTime() >= 0)){
                return new validationResult(false, "arrival time of process "+(processes.indexOf(p)+1) +" is invalid ");
            }
            if(!(p.getBurstTime() > 0)){
                return new validationResult(false, "burst time of process "+(processes.indexOf(p)+1)+" is invalid ");
            }
            if(!(p.getPriority() >= 1)){
                return new validationResult(false, " priority of process "+(processes.indexOf(p)+1)+" is invalid ");
            }
        }

        return new validationResult(true, "valid data");
    }
}
