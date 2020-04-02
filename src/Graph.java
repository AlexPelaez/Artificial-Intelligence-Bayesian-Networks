import java.util.ArrayList;

/**
 * Created by Alex on 4/2/20.
 */
public class Graph {
    private ArrayList<Node> nodes;
    private String filePath;

    public Graph(String filePath){
        this.filePath = filePath;
        createGraphFromBif();
    }

    private boolean createGraphFromBif(){
        
        return true;
    }

    private Node getRootNode(){
        return nodes.get(0);
    }
}
