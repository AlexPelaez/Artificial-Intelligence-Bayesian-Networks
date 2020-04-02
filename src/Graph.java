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
//                System.out.println(currentLine);
                if(currentLine.contains("variable")){
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

                }
            }



        } catch(FileNotFoundException e) {
            System.out.println("Exception: File Not Found. Graph was not created");
            return false;
        }
        return true;
    }

    private Node getRootNode(){
        return nodes.get(0);
    }
}
