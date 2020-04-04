public class Main {

    public static void main(String[] args) {
        VariableElimination variableElimination = new VariableElimination();
        Graph graph = new Graph("/Users/Alex/Documents/School/AI/Artificial-Intelligence-Bayesian-Networks/inputs/cancer.bif");
        variableElimination.loadNewGraph(graph);


    }

    public void printResults(Object o){

    }
}
