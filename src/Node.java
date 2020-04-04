import java.util.ArrayList;

/**
 * Created by Alex on 4/2/20.
 */
public class Node {
    private ArrayList<Node> children = new ArrayList<Node>();
    private ArrayList<Node> parents = new ArrayList<Node>();
    private double[] conditionalProbabilityTable;
    private String[] possibleValues;
    private String name;
    private int numPossibleValues;

    public Node(String name, String[] possibleValues) {
        this.name = name;
        this.possibleValues = possibleValues;
        this.numPossibleValues = possibleValues.length;
    }

    public void updateConditionalProbabilityTable(double[] cpt, String nodeName){
        this.conditionalProbabilityTable = cpt;
        this.name = nodeName;
        printConditionalProbabilityTable();
    }

    private void printConditionalProbabilityTable() {
        for(int i = 0; i < conditionalProbabilityTable.length; i++){
            System.out.println(conditionalProbabilityTable[i]);
        }
    }

    private void addNewChild(Node child){

    }

    public String getNodeName(){
        return name;
    }

    public int getNumPossibleValues(){
        return numPossibleValues;
    }

    public void addChildNode(Node child){
        if(!(isNodeInList(child.getNodeName(), children))) {
            parents.add(child);
            System.out.println(name + " added new child: "+child.getNodeName());
        }
    }

    public void addParentNode(Node parent){
        if(!(isNodeInList(parent.getNodeName(), parents))) {
            parents.add(parent);
            System.out.println(name + " added new parent: "+parent.getNodeName());
        }
    }

    private boolean isNodeInList(String nodeName, ArrayList<Node> nodes){
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).getNodeName().equals(nodeName)){
                return true;
            }
        }
        return false;
    }
}
