import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        VariableElimination variableElimination = new VariableElimination();
        Graph graph = new Graph("/Users/Alex/Documents/School/AI/Artificial-Intelligence-Bayesian-Networks/inputs/cancer.bif");
        variableElimination.loadNewGraph(graph);
        ArrayList<String> evidence = new ArrayList<String>();
        evidence.add("Smoker=True");
        evidence.add("Cancer=True");
        variableElimination.loadEvidence(evidence);
        variableElimination.runVariableElimination("Smoker");
    }

    public void printResults(Object o){

    }
}
