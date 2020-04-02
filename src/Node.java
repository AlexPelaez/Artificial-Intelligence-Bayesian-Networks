import java.util.ArrayList;

/**
 * Created by Alex on 4/2/20.
 */
public class Node {
    private ArrayList<Node> children;
    private double[] conditionalProbabilityTable;
    private String[] possibleValues;
    private String name;
    private int numPossibleValues;

    public Node(String name, String[] possibleValues) {
        this.name = name;
        this.possibleValues = possibleValues;
    }

    public void updateConditionalProbabilityTable(double[][] cpt){

    }

    private void addNewChild(Node child){

    }
}
