import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Graph class is a data structure that represents a bays net graph
 */
public class Graph {
    private ArrayList<Node> nodes = new ArrayList<>();
    private File file;
    /**
     * Parameters:
     * String filePath: a String representing the path to the input file.
     *
     * Graph: Constructor for the Graph class. Calls createGraphFromBif method
     *
     * Returns:
     * Graph: a Graph object representing the bays net
     *
     */
    public Graph(String filePath){
        this.file = new File(filePath);
        createGraphFromBif();
    }
    public ArrayList<Node> getNodeList(){
        return nodes;
    }
    /**
     * Parameters:
     * None
     *
     * createGraphFromBif: populates the nodes in the nodes instance field. Updates each nodes conditional
     * probability table. Updates each nodes children list. Updates each nodes parents list.
     *
     * Return:
     * True: if graph was created
     * False: if graph was not created
     */
    private boolean createGraphFromBif(){
        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()){
                String currentLine = s.nextLine();
                // If the line we are parsing contains 'variable' then we want to create a new node
                if(currentLine.contains("variable")){
                    // Create nodes
                    String[] variableLine = currentLine.split(" ");
                    String variableName = variableLine[1];
                    currentLine = s.nextLine();
                    String[] possibleValuesLine = currentLine.split(" ");
                    int numberOfPossibleValues = Integer.parseInt(String.valueOf(currentLine.charAt(18)));
                    String[] possibleValues = new String[numberOfPossibleValues];
                    for(int i = 0; i < numberOfPossibleValues; i++){
                        String currentPossibleValue = possibleValuesLine[8+i];
                        if(currentPossibleValue.contains(",")){
                            currentPossibleValue = currentPossibleValue.substring(0, currentPossibleValue.length() - 1);
                        }

                        possibleValues[i] = currentPossibleValue;
                    }
                  nodes.add(new Node(variableName, possibleValues));
                    // If the line we are parsing contains 'probability' then we want to create a conditional
                    // probability table for the corresponding node. We must also update the children and parents list
                    // to reflect any new child or parent nodes.
                } else if(currentLine.contains("probability")){
                    // Update nodes with there corresponding conditional probability tables
                    String[] probabilityLine = currentLine.split(" ");
                    String nodeNames[] = new String[probabilityLine.length-4];
                    if(probabilityLine.length > 5){
                        int j = 0;
                        for(int i = 2; i < probabilityLine.length-2; i++){
                            if(i != 3){
                                if(probabilityLine[i].contains(",")){
                                    nodeNames[j] = probabilityLine[i].substring(0, probabilityLine[i].length() - 1);
                                } else {
                                    nodeNames[j] = probabilityLine[i];
                                }
                                j++;
                            }
                        }
                        int numberOfProbabilites = nodes.get(getNodeByName(nodeNames[0])).getNumPossibleValues();
                        int numberOfProbabilityLines = 1;
                        for(int i = 1; i < nodeNames.length-1; i++){
                            numberOfProbabilityLines *= nodes.get(getNodeByName(nodeNames[i])).getNumPossibleValues();
                        }
                        double[] conditionalProbabilityTable = new double[numberOfProbabilites*numberOfProbabilityLines];
                        String[] conditionalProbabilityTableString = new String[numberOfProbabilites*numberOfProbabilityLines];
                        int count = 0;
                        int stringCount = 0;
                        for(int i = 0; i < numberOfProbabilityLines; i++){
                            currentLine = s.nextLine();
                            // Uncomment if you want to view original CPT as read in by the file
//                            System.out.println(currentLine);
                            String[] currentProbabilityLineSplit = currentLine.split(" ");

                            for (int m = 0; m < currentProbabilityLineSplit.length; m++){
                                String stringToParse = currentProbabilityLineSplit[m];
                                if(stringToParse.length() != 0){
                                    stringToParse = stringToParse.substring(0, stringToParse.length() - 1);
                                }
                                try{
                                    conditionalProbabilityTable[count] = Double.parseDouble(stringToParse);
                                    count++;
                                }catch (Exception e){
                                    if(!(stringToParse.equals(""))){
                                        if(stringToParse.contains("(")){
                                            stringToParse = stringToParse.substring(1, stringToParse.length());
                                        }
                                        conditionalProbabilityTableString[stringCount] = stringToParse;
                                        stringCount++;
                                    }
                                    // Catch any of the exceptions thrown when the string we are trying to parse
                                    // is not a number.
                                }
                            }
                        }
                        // Update each nodes conditional probability table
                        nodes.get(getNodeByName(nodeNames[0])).
                                updateConditionalProbabilityTable(conditionalProbabilityTable, nodeNames[0]);
                        nodes.get(getNodeByName(nodeNames[0])).
                                updateConditionalProbabilityTableString(conditionalProbabilityTableString, nodeNames[0]);

                        // add parents for each Node
                        for(int i = 1; i < nodeNames.length-1; i++){
                            nodes.get((getNodeByName(nodeNames[0]))).
                                    addParentNode(nodes.get(getNodeByName(nodeNames[i])));
                        }

                        // add children for each Node
                        for(int i = 1; i < nodeNames.length-1; i++){
                            nodes.get((getNodeByName(nodeNames[i]))).
                                    addChildNode(nodes.get(getNodeByName(nodeNames[0])));
                        }
                    } else {
                        //Handle table formatted probabilites
                        nodeNames[0] = probabilityLine[2];
                        int numberOfProbabilites = nodes.get(getNodeByName(nodeNames[0])).getNumPossibleValues();
                        currentLine = s.nextLine();
                        String[] tableLine = currentLine.split(" ");
                        double[] conditionalProbabilityTable = new double[numberOfProbabilites];
                        for(int i = 3; i < numberOfProbabilites+3; i++){
                            conditionalProbabilityTable[i-3] =
                                    Double.parseDouble(tableLine[i].substring(0, tableLine[i].length() - 1));
                        }
                        nodes.get(getNodeByName(nodeNames[0])).
                                updateConditionalProbabilityTable(conditionalProbabilityTable, nodeNames[0]);
                    }
                }
            }
        } catch(FileNotFoundException e) {
            // Handle any FileNotFoundExceptions that occur when attempting to read in files
            System.out.println("Exception Handled: File Was Not Found. Graph was not created");
            return false;
        }
        return true;
    }

    /**
     * Parameters:
     * String nodeName: representing the name of the node to be found
     *
     * getNodeByName: grabs the index of the node with the matching name
     *
     * Return:
     * Int: index of the node in the nodes list
     */
    private int getNodeByName(String nodeName){
        for(int i = 0; i < nodes.size(); i++){
            if(nodes.get(i).getNodeName().equals(nodeName)){
                return i;
            }
        }
        return 0;
    }

    private Node getRootNode(){
        return nodes.get(0);
    }
}
