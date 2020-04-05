import java.util.ArrayList;

/**
 * Created by Alex on 4/2/20.
 */
public class VariableElimination {
    private Graph currentGraph;
    private ArrayList<Node> nodes;

    public void loadNewGraph(Graph g){
        this.currentGraph = g;
        nodes = currentGraph.getNodeList();
    }

    public void loadEvidence(ArrayList<String> evidence){
        for(int i = 0; i < evidence.size(); i++){
            String currentEvidence = evidence.get(i);
            String[] currentEvidenceSplit = currentEvidence.split("=");
            int indexOfNodeToUpdateEvidence = getNodeByName(currentEvidenceSplit[0], nodes);
            ArrayList<Integer> indexesOfAllNodesToUpdate = new ArrayList<Integer>();
            indexesOfAllNodesToUpdate.add(indexOfNodeToUpdateEvidence);
            ArrayList<Node> currentChildren = nodes.get(indexOfNodeToUpdateEvidence).getChildren();
            int indexOfEvidenceValue = 0;
            String[] possibleValuesForEvidence = nodes.get(indexOfNodeToUpdateEvidence).getPossibleValues();
            for(int j = 0; j < possibleValuesForEvidence.length; j++){
                if(possibleValuesForEvidence[j].equals(currentEvidenceSplit[1])){
                    indexOfEvidenceValue = j;
                }
            }
            for(int j = 0; j < currentChildren.size(); j++){
                indexesOfAllNodesToUpdate.add(getNodeByName(currentChildren.get(j).getNodeName(), nodes));
                System.out.println("Adding "+ currentChildren.get(j).getNodeName()+" to the list of nodes to be updated");
            }

            for(int j = 0; j < indexesOfAllNodesToUpdate.size(); j++){
                double[] factor = nodes.get(indexesOfAllNodesToUpdate.get(j)).
                        getConditionalProbabilityTable();
                System.out.println("updating node with name: " + nodes.get(indexesOfAllNodesToUpdate.get(j)).getNodeName());
                if(nodes.get(indexesOfAllNodesToUpdate.get(j)).getNodeName().equals(currentEvidenceSplit[0])){
                    for(int c = 0; c < factor.length; c++){
                        if(c%possibleValuesForEvidence.length == indexOfEvidenceValue){
                            factor[c] = 1;
                        } else {
                            factor[c] = 0;
                        }
                    }
                    nodes.get(indexesOfAllNodesToUpdate.get(j)).updateConditionalProbabilityTable(factor, nodes.get(indexesOfAllNodesToUpdate.get(j)).getNodeName());
                } else{
//                    System.out.println("getting parents of "+nodes.get(indexesOfAllNodesToUpdate.get(j)).getNodeName());
//                    ArrayList<Node> currentParents = nodes.get(indexesOfAllNodesToUpdate.get(j)).getParents();
//                    int indexOfParent = 0;
//                    int numberOfRows = 0;
//                    for(int p = 0; p < currentParents.size(); p++){
////                        System.out.println(nodes.get(indexesOfAllNodesToUpdate.get(j)).getNodeName()+ " has parents "+currentParents.get(p).getNodeName());
//                        numberOfRows += currentParents.get(p).getNumPossibleValues();
//                        if(currentParents.get(p).getNodeName().equals(currentEvidenceSplit[0])){
//                            indexOfParent = p;
//                            System.out.println("Evidence is at index "+ p+" in parents");
//                        }
//                    }
//                    System.out.println("Number of rows "+ numberOfRows);
////                  use indexOfEvidenceValue, numberOfRows, and indexOfParent to find which rows to change
//                    String[] conditionalProbabilityTableString = nodes.get(indexesOfAllNodesToUpdate.get(j)).
//                            getConditionalProbabilityTableString();
//                    if(numberOfRows !=2 ){
//                        numberOfRows *= 2;
//                    }
//                    for(int r = 0; r < numberOfRows; r++){
//
//                        System.out.println(conditionalProbabilityTableString[r]);
//                        System.out.println(currentEvidenceSplit[1]);
//                        if(conditionalProbabilityTableString[r].equals(currentEvidenceSplit[1])){
//                            System.out.println(r+" -> " +(r-currentParents.size()));
//                        }
//
//
//                    }

                }
            }

        }
    }

    public int runVariableElimination(String query){
        int marginalizedProbability = 0;
        ArrayList<String> ordereing = findMaxCardinality();
        ordereing = removeQueryFromEliminationOrder(ordereing, query);
        for(int i = 0; i < ordereing.size(); i++){
            eliminateVariable(ordereing.get(i));
        }

        return marginalizedProbability;
    }

    private void eliminateVariable(String nameOfNodeToBeEliminated){

    }

    private ArrayList<String> removeQueryFromEliminationOrder(ArrayList<String> order, String query){
        for(int i = 0; i < order.size(); i++){
            if(order.get(i).equals(query)){
                order.remove(i);
                return order;
            }
        }
        return order;
    }

    private ArrayList<String> findMaxCardinality(){
        setNodesUnmarked();
        ArrayList<String> ordereing = new ArrayList<String>();
        ArrayList<Node> remainingNodes = nodes;
        while(remainingNodes.size() != 0){
            int currentMax = 0;
            String currentName = "";
            int currentTempIndex = 0;
            for(int j = 0; j < remainingNodes.size(); j++){
                int nodeIndex = getNodeByName(remainingNodes.get(j).getNodeName(), nodes);
                int tempNumMarked = calculateNumberOfMarkedNeigbors(nodes.get(nodeIndex));
                if(currentMax < tempNumMarked){
                    currentMax = tempNumMarked;
                    currentName = remainingNodes.get(j).getNodeName();
                }
            }
            if(currentName.equals("")){
                currentName = remainingNodes.get(0).getNodeName();
            }
            nodes.get(getNodeByName(currentName, nodes)).setMarked(true);
            ordereing.add(nodes.get(getNodeByName(currentName, nodes)).getNodeName());
            remainingNodes.remove(getNodeByName(currentName, remainingNodes));
        }
        return ordereing;
    }

    private Graph triangulateGraph(){
       Graph triangulatedGraph = currentGraph;

        return triangulatedGraph;
    }

    private int calculateNumberOfMarkedNeigbors(Node n){
        ArrayList<Node> children = n.getChildren();
        ArrayList<Node> parents = n.getParents();
        int numberOfMarkedNeigbors = 0;
        for(int i = 0; i < children.size(); i++){
            if(children.get(i).getMarked()){
                numberOfMarkedNeigbors++;
            }
        }

        for(int i = 0; i < parents.size(); i++){
            if(parents.get(i).getMarked()){
                numberOfMarkedNeigbors++;
            }
        }
        return numberOfMarkedNeigbors;
    }

    private int getNodeByName(String nodeName,ArrayList<Node> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getNodeName().equals(nodeName)){
                return i;
            }
        }
        return 0;
    }

    private void setNodesUnmarked(){
        for(int i = 0; i < nodes.size(); i++){
            nodes.get(i).setMarked(false);
        }
    }
}
