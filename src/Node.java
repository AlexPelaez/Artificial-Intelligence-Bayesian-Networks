import java.util.ArrayList;

/**
 * The Node class is responsible for holding each nodes conditionalProbabilityTable, children list, and parents list.
 * Nodes are created by the Graph class.
 */
public class Node {
    private ArrayList<Node> children = new ArrayList<Node>();
    private ArrayList<Node> parents = new ArrayList<Node>();
    private ArrayList<Double> conditionalProbabilityTable;
    private ArrayList<String> conditionalProbabilityTableString;
    private ArrayList<String> possibleValues;
    private String name;
    private int numPossibleValues;
    private boolean marked;
    /**
     * Parameters:
     * String name: a String representing the name of the Node
     * String[] possibleValues: an array of strings that represent all the values that a Node could take on
     *
     * Node: Constructor for the Node class.
     *
     * Returns:
     * Node: a Node object representing a Node in the bays net.
     */
    public Node(String name, ArrayList<String> possibleValues) {
        this.name = name;
        this.possibleValues = possibleValues;
        this.numPossibleValues = possibleValues.size();
    }

    public void updateConditionalProbabilityTableString(ArrayList<String> cpt, String nodeName){
        this.conditionalProbabilityTableString = cpt;
        this.name = nodeName;
        printConditionalProbabilityTableString();
    }
    /**
     * Parameters:
     * double[] cpt: The conditionalProbabiltyTable to be added to the Node
     * String nodeName: A String representing the name of the Node
     *
     * updateConditionalProbabilityTable: adds the conditional probability table to the nodes instance feild
     *
     * Return:
     * None
     */
    public void updateConditionalProbabilityTable(ArrayList<Double> cpt, String nodeName){
        this.conditionalProbabilityTable = cpt;
        this.name = nodeName;
        printConditionalProbabilityTable();
    }
    /**
     * Parameters:
     * Node child: node to be added to the list of children nodes
     *
     * addChildNode: adds the node to the children list if it is not already listed as a child.
     *
     * Return:
     * None
     */
    public void addChildNode(Node child){
        if(!(isNodeInList(child.getNodeName(), children))) {
            children.add(child);
//            System.out.println(name + " added new child: "+child.getNodeName());
        }
    }
    /**
     * Parameters:
     * Node parent: node to be added to the list of parent nodes
     *
     * addParentNode: adds the node to the parents list if it is not already listed as a parent.
     *
     * Return:
     * None
     */
    public void addParentNode(Node parent){
        if(!(isNodeInList(parent.getNodeName(), parents))) {
            parents.add(parent);
//            System.out.println(name + " added new parent: "+parent.getNodeName());
        }
    }

    /**
     * Parameters:
     * none
     *
     * getNodeName: grab the name of the Node
     *
     * Return:
     * String: the name of the node
     */
    public String getNodeName(){
        return name;
    }
    /**
     * Parameters:
     * none
     *
     * getNumPossibleValues: get the number of possible values for the given node
     *
     * Return:
     * int: the number of possible values for that node
     */
    public int getNumPossibleValues(){
        return numPossibleValues;
    }

    public boolean getMarked(){
        return marked;
    }

    public void setMarked(boolean x){
        marked = x;
    }

    public ArrayList<Double> getConditionalProbabilityTable(){
        return conditionalProbabilityTable;
    }

    public ArrayList<String> getConditionalProbabilityTableString(){
        return conditionalProbabilityTableString;
    }


    public ArrayList<Node> getChildren(){
        return children;
    }

    public ArrayList<Node> getParents(){
        return parents;
    }

    public ArrayList<String> getPossibleValues(){
        return possibleValues;
    }
    /**
     * Parameters:
     * String nodeName: the name of the node to search for
     * ArrayList<Node> nodes: the list of nodes to query from
     *
     * updateConditionalProbabilityTable: adds the conditional probability table to the nodes instance field.
     *
     * Return:
     * boolean: true if the node is contained in the list and false otherwise.
     */
    private boolean isNodeInList(String nodeName, ArrayList<Node> nodes){
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).getNodeName().equals(nodeName)){
                return true;
            }
        }
        return false;
    }
    /**
     * Parameters:
     * none
     *
     * printConditionalProbabilityTable: prints the conditional probability table associated with the Node
     *
     * Return:
     * none
     */
    private void printConditionalProbabilityTable() {
        for(int i = 0; i < conditionalProbabilityTable.size(); i++){
            System.out.println(conditionalProbabilityTable.get(i));
        }
    }
    /**
     * Parameters:
     * none
     *
     * printConditionalProbabilityTableString: prints the String conditional probability table associated with the Node
     *
     * Return:
     * none
     */
    private void printConditionalProbabilityTableString() {
        for(int i = 0; i < conditionalProbabilityTableString.size(); i++){
            System.out.println(conditionalProbabilityTableString.get(i));
        }
    }
}
