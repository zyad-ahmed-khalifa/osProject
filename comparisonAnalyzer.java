package analysis;

public class comparisonAnalyzer {

    public static StringBuilder analysis(double pwt,double swt, double ptat, double stat, double prt, double srt){
        StringBuilder sb = new StringBuilder();
        sb.append("metric\t\t\tPriority\t\t\tSRTF\n");
        sb.append("----------------------------------------------------------------------------\n");
        sb.append("avg(WT)\t\t\t"+ pwt + "\t\t\t\t" + swt + "\t\t\t" + "-> " + ((pwt<swt)?"priority wins":(pwt == swt)?"Tie ":"srtf wins")+"\n");
        sb.append("avg(tat)\t\t\t"+ ptat + "\t\t\t\t" + stat + "\t\t\t" + "-> " + ((pwt<swt)?"priority wins":(pwt == swt)?"Tie ":"srtf wins")+"\n");
        sb.append("avg(rt)\t\t\t"+ prt + "\t\t\t\t" + srt + "\t\t\t" + "-> " + ((pwt<swt)?"priority wins":(pwt == swt)?"Tie ":"srtf wins")+"\n");
        return sb;
    }
    public static StringBuilder q1(double pwt,double swt){
        StringBuilder sb = new StringBuilder();
        sb.append("Q1.Which algorithm produced the lower average waiting time?\n");
        sb.append("\t->"+((pwt<swt)?"priority ":(pwt == swt)?"Tie ":"srtf ") + "("+ pwt + " vs " + swt + ")\n\n");
        return sb;
    }
    public static StringBuilder q2(double prt,double srt){
        StringBuilder sb = new StringBuilder();
        sb.append("Q2.Which algorithm produced the lower average response time?\n");
        sb.append("\t->"+((prt<srt)?"priority ":(prt == srt)?"Tie ":"srtf ") + "("+ prt + " vs " + srt + ")\n\n");
        return sb;
    }
}
