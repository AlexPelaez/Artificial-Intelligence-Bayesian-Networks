import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Alex on 4/2/20.
 */
public class Graph {
    private ArrayList<Node> nodes = new ArrayList<>();
    private File file;

    public Graph(String filePath){
        this.file = new File(filePath);
        createGraphFromBif();
    }

    private boolean createGraphFromBif(){
        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()){
                String currentLine = s.nextLine();
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
                        String[][] currentProbabilityLines = new String[numberOfProbabilityLines][];
                        for(int i = 0; i < numberOfProbabilityLines; i++){
                            currentProbabilityLines[i] = s.nextLine().split(" ");
                        }
                        int count = 0;
                        double[] conditionalProbabilityTable = new double[numberOfProbabilites*numberOfProbabilityLines];
                        for(int b = 0; b < numberOfProbabilites; b++){
                            for(int i = 0; i < numberOfProbabilityLines; i++){
                                conditionalProbabilityTable[count] =
                                        Double.parseDouble(currentProbabilityLines[i][b+2+numberOfProbabilityLines/2].
                                                substring(0, currentProbabilityLines[i][b+2+numberOfProbabilityLines/2].
                                                        length() - 1));
                                count++;
                            }
                        }
                        nodes.get(getNodeByName(nodeNames[0])).
                                updateConditionalProbabilityTable(conditionalProbabilityTable, nodeNames[0]);

                        // add parents for each Node
                        for(int i = 1; i < nodeNames.length-1; i++){
                            nodes.get((getNodeByName(nodeNames[0]))).addParentNode(nodes.get(getNodeByName(nodeNames[i])));
                        }

                        // add children for each Node
                        for(int i = 1; i < nodeNames.length-1; i++){
                            nodes.get((getNodeByName(nodeNames[i]))).addChildNode(nodes.get(getNodeByName(nodeNames[0])));
                        }
                    } else {
                        //deal with table formatted probabilites
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
            System.out.println("Exception: File Not Found. Graph was not created");
            return false;
        }
        return true;
    }


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
