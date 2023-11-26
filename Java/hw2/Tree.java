package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// import com.fasterxml.jackson.core.JsonParseException;
// import com.fasterxml.jackson.databind.JsonMappingException;
// import com.fasterxml.jackson.databind.ObjectMapper;


public class Tree {
    
    private TreeNode root;
    
    // JSON format and creates tree
    public Tree(String input) throws IOException {
        try {
            String jsonString = input;
            JSONObject jsonobj = new JSONObject(jsonString);
            root = parseJson(jsonobj);        
            
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    // Reads JSON format from file 
    public Tree(File file) throws IOException,FileNotFoundException,IllegalAccessException {
        
        
        if (!validJson(file)) {
            root = null;
            System.out.println();
            System.out.println("Invalid format");
        } else {
            String jsonString = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            JSONObject jsonobj = new JSONObject(jsonString);
            root = parseJson(jsonobj);
        }

    }

    public boolean validJson(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            new JSONObject(content);
        } catch(IOException | JSONException e) {
            return false;
        }
        return true;
    }
    // Get Root
    public TreeNode getRoot () {
        return root;
    }
    
    // MinMax algo and returns value
    public double MinMax() {
        double value = Double.NaN;
        double score_min = -Double.MAX_VALUE;
        double score_max = Double.MAX_VALUE;
        if(sizeofTree((InternalNode) root) > 1)
            value = MinMaxAlgo((InternalNode) root, score_min, score_max);
        
        return value;
    }
    
    public double MinMaxAlgo(InternalNode node,double score_min,double score_max) {
        double score = node.getValue();
        if(node.getChildren() == null || node.getChildernSize() == 0) {
            //System.out.println("Value for MinMax: "+node.getValue());
            return node.getValue();            
        } 
        
        ArrayList<InternalNode> children = node.getChildren();
       //System.out.println("The type is: "+node.getType());
        if (node.getType().equals("max")) {
            //System.out.println("Im in max");
            for (InternalNode child : children) {
                if (child != null) {
                    score = MinMaxAlgo(child, score_min, score_max);
                    if (score > score_min) score_min = score;
                }
            }
            return score_min;
        } else {
            //System.out.println("Im in min");
            for (InternalNode child : children) {
                if (child != null) {
                    score = MinMaxAlgo(child, score_min, score_max);
                    if (score < score_max) score_max = score;
                }
            }
            return score_max;
        }

    }

    public int size() {
        int res;
        res = sizeofTree((InternalNode) root);
        return res;
    }

    public int sizeofTree(InternalNode node) {
        if(node == null) {
            return 0;
        } else {
            int count = 1;
            if (node.getChildren() != null) {
                for (InternalNode child : node.getChildren()) {
                    if (child != null) {
                        count += sizeofTree(child);
                    }
                }
                
            }
            return count;
        }
        
    }

    

    public ArrayList<Integer> optimalPath() {
        ArrayList<Integer> path = new ArrayList<Integer>();
        InternalNode node = (InternalNode) this.root;

        while(!node.getType().equals("leaf")) {
            ArrayList<InternalNode> children = node.getChildren();
            int index = 0;
            double value;

            if(node.getType().equals("max")) {
                value = -Double.MAX_VALUE;
                for (InternalNode child : children) {
                    double child_value = MinMaxAlgo(child,-Double.MAX_VALUE,Double.MAX_VALUE);
                    if(child_value > value) {
                        index = children.indexOf(child);
                        value = child_value;
                    }
                }
            } else if(node.getType().equals("min")) {
                value = Double.MAX_VALUE;
                for (InternalNode child : children) {
                    double child_value = MinMaxAlgo(child,-Double.MAX_VALUE,Double.MAX_VALUE);
                    if(child_value < value) {
                        index = children.indexOf(child);
                        value = child_value;
                    }
                }
            } else {
                return path;
            }
            path.add(index);
            node = children.get(index);
        }
        return path;
    }

    // String in JSON format
    public String toString() {
        return toStringhelper((InternalNode)root,0);
    }
    public String toStringhelper(InternalNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(depth);

        if (node == null)
            return "";
        
        sb.append(indent).append("{\n");
        sb.append(indent).append("  \"type\": \"").append(node.getType()).append("\",\n");
        if (!Double.isNaN(node.getValue()) && !node.getType().equals("leaf")) {
            sb.append(indent).append("  \"value\": ").append(String.format("%.2f", node.getValue())).append(",\n");
        }
        if (!Double.isNaN(node.getValue()) && node.getType().equals("leaf")) {
            sb.append(indent).append("  \"value\": ").append(String.format("%.2f", node.getValue()));
        }

        if (node.getChildren() != null) {
            sb.append(indent).append("  \"children\": [\n");
            for (int i=0; i < node.getChildernSize(); i++) {
                sb.append(toStringhelper(node.getChildren().get(i), depth + 1));
                if (i != node.getChildernSize() - 1) {
                    sb.append(",\n");
                }
            }
            sb.append("\n").append(indent).append("  ]");
        }
        sb.append("\n").append(indent).append("}");

        return sb.toString();
    }

    public void TraverseTree () {
        getMinMax((InternalNode) root);
    }

    public void getMinMax (InternalNode node) {
        ArrayList<InternalNode> children = node.getChildren();
        if(children == null)
            return;
        for (InternalNode child : children) {
            if(child.getType().equals("min") || child.getType().equals("max")) {
                getMinMax(child);
            }
        }
        assignValue(node);
    }

    public void assignValue (InternalNode node) {
        /*if(node.getPruned() == true)
            return;
*/
        String type = node.getType();
        ArrayList<InternalNode> children = node.getChildren();
        double value;

        value = children.get(0).getValue();
        
        for (int i = 1; i < node.getChildernSize(); i++) {
           if (children.get(i).getPruned() == false) {
                double childvalue = children.get(i).getValue();
                if (type.equals("max")) {
                    value = Math.max(value,childvalue);
                } else if (type.equals("min")) {
                    value = Math.min(value, childvalue);
                }
            }
                
        }

        node.setValue(value);
    }
    // String in dot format
    public String toDotString() {
        return toDotStringGuide((InternalNode) root);
    }
    
    public String toDotStringGuide(InternalNode root) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        sb.append("node [style=filled, color=lightblue];\n");

        // Traverse the tree using depth-first search
        Stack<InternalNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            InternalNode node = stack.pop();
            if (node == null) continue;

            sb.append(node.hashCode()).append(" [label=\"").append(node.getValue()).append("\"];\n");
            for (InternalNode child : node.getChildren()) {
                if (child != null) {
                    sb.append(node.hashCode()).append(" -> ").append(child.hashCode());
                    if (node.getType().equals("max")) {
                        sb.append(" [color=blue]");
                    } else {
                        sb.append(" [color=green]");
                    }
                    sb.append(";\n");
                    stack.push(child);
                }
            }
        }

        sb.append("}\n");
        return sb.toString();
    }
    // Writes toString in a file
    public void toFile(File file) throws IOException {
        try {
            if(file.exists()){}
        } catch (Exception e) {
            System.err.println("File exists" + e.getMessage());
            // TODO: handle exception
            
        }

        FileWriter fw = new FileWriter(file.getAbsolutePath(),true);
        fw.write(toString());
        fw.close();
        
    }

    // Writes todotString in a file
    public void toDotFile(File file) throws IOException {
        try {
            if(file.exists()){}
        } catch (Exception e) {
            System.err.println("File exists" + e.getMessage());
            // TODO: handle exception
            
        }

        FileWriter fw = new FileWriter(file.getAbsolutePath(),true);
        fw.write(toDotString());
        fw.close();
        
    }


    // Parser of the data
    public static InternalNode parseJson(JSONObject node) throws JSONException {
        String type = node.getString("type");
        if (type.equals("leaf")) {
            double value = node.getDouble("value");
            // System.out.println(value);
            
            return new InternalNode(value, type, false);
        } else {
            ArrayList<InternalNode> children = new ArrayList<>();
            JSONArray childrenJsonArray = node.getJSONArray("children");
            for (int i = 0; i < childrenJsonArray.length(); i++) {
                JSONObject childJson = childrenJsonArray.getJSONObject(i);
                InternalNode child = parseJson(childJson);
                //System.out.println(childJson);
                children.add(child);
                
            }
            double alpha = -Double.MAX_VALUE;
            double beta = Double.MAX_VALUE;

            return new InternalNode(Double.NaN,type, children,alpha,beta,false);
        }
    }

}


    