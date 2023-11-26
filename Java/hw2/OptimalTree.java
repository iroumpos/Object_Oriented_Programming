package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

// import com.fasterxml.jackson.core.JsonParseException;
// import com.fasterxml.jackson.databind.JsonMappingException;

public class OptimalTree extends Tree{
    
    private TreeNode root; 
    private int pruned;

    // Constructor
    public OptimalTree(File file) throws IOException,FileNotFoundException,IllegalAccessException {
        super(file);
        //TODO Auto-generated constructor stub
       this.root = super.getRoot(); 
       this.pruned = 0;
    }


    
    // Percentage of pruned nodes
    public double prunedRatio() {
        int size = sizeofTree((InternalNode) root);
        return pruned/size;
    }

    // MinMax algo and returns value
    public double MinMax() {
        return MinMax_ab_guide((InternalNode) root, -Double.MAX_VALUE, Double.MAX_VALUE);
    }


    public double MinMax_ab_guide(InternalNode node, double alpha, double beta) {
        double value;
        int i=0;

        if(node.getChildren() == null || node.getChildernSize() == 0)
            return node.getValue();
        
        ArrayList<InternalNode> children = node.getChildren();
        if(node.getType().equals("max")) {
            value = -Double.MAX_VALUE;
            for (i=0; i < children.size(); i++) {                                                       
                InternalNode child = children.get(i);
                if(!child.getPruned()) {
                    value = Math.max(value,MinMax_ab_guide(child, alpha, beta));
                    alpha = Math.max(alpha, value);
                    if (beta <= alpha)
                        break;
                }
            }
            for (int j = i+1; j < children.size(); j++) {
                children.get(j).setPruned(true);
                if(children.get(j).getType().equals("max") || children.get(j).getType().equals("min")) {
                    // Check if all children are pruned
                    pruneChildren(children.get(j));
                }

            }
            return value; 
        } else {
            value = Double.MAX_VALUE;
            for (i=0; i < children.size(); i++) {
                InternalNode child = children.get(i);
                if(!child.getPruned()) {
                    value = Math.min(value,MinMax_ab_guide(child, alpha, beta));
                    beta = Math.min(beta, value);
                    if (beta <= alpha)
                        break;
                }
            }
            for (int j = i+1; j < children.size(); j++) {
                children.get(j).setPruned(true);

                if(children.get(j).getType().equals("max") || children.get(j).getType().equals("min")) {
                    // Check if all children are pruned
                    pruneChildren(children.get(j));
                }

            }
            return value; 
        }
    }
    
    public void pruneChildren(InternalNode node) {
        if(node.getType().equals("leaf"))
            return;
        for(InternalNode child : node.getChildren()) {
            child.setPruned(true);
            pruneChildren(child);
        }
            
    }
    // Optimal path
    public ArrayList<Integer> optimalPath() {
        double target = MinMax();
        //System.out.println("Value: "+target);
        return optimalPathguiude_ab((InternalNode) root, target, new ArrayList<Integer>());      
    }

    // Guide to optimal path
    public ArrayList<Integer> optimalPathguiude_ab(InternalNode node,double value,ArrayList<Integer> path) {
        if (node == null) {
            return null;
        }

        if(node.getValue() == value) {
            return path;
        }

        if(node.getChildren() != null) {
            for(int i=0; i < node.getChildernSize(); i++) {
                ArrayList<Integer> childpath = new ArrayList<>(path);
                if(!node.getPruned()) {
                    childpath.add(i);
                    ArrayList<Integer> result = optimalPathguiude_ab(node.getChildren().get(i), value, childpath);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }
  
    // String in JSON format
    public String toString() {
        return toStringhelper_ab((InternalNode) root,0);

    }
    
    public String toStringhelper_ab(InternalNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(depth);
        
        if (node == null)
            return "";
        
        sb.append(indent).append("{\n");
        sb.append(indent).append("  \"type\": \"").append(node.getType()).append("\",\n");
        if (!Double.isNaN(node.getValue()) && !node.getType().equals("leaf")) {
            if(!node.getPruned())
                sb.append(indent).append("  \"value\": ").append(String.format("%.2f", node.getValue())).append(",\n");
            if(node.getPruned()) {
                sb.append(indent).append("  \"pruned\": ").append(node.getPruned()).append(",\n");
            }
        }
        if (!Double.isNaN(node.getValue()) && node.getType().equals("leaf")) {
            sb.append(indent).append("  \"value\": ").append(String.format("%.2f", node.getValue()));
            if(node.getPruned()) {
                sb.append(",\n").append(indent).append("  \"pruned\": ").append(node.getPruned());
            }
        }

        if (node.getChildren() != null) {
            sb.append(indent).append("  \"children\": [\n");
            for (int i=0; i < node.getChildernSize(); i++) {
                sb.append(toStringhelper_ab(node.getChildren().get(i), depth + 1));
                if (i != node.getChildernSize() - 1) {
                    sb.append(",\n");
                }
            }
            sb.append("\n").append(indent).append("  ]");
        }
        sb.append("\n").append(indent).append("}");

        return sb.toString();
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
                        if(node.getPruned() == true)
                            sb.append(" [color=red]");
                        sb.append(" [color=blue]");
                    } else {
                        if(node.getPruned() == true)
                            sb.append(" [color=red]");
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

    public int CountPrunedNodes() {
        prunednodes((InternalNode) root);
        return this.pruned;
    }

    public void prunednodes(InternalNode node) {
        if(node.getType().equals("leaf")) {
            return;
        }

        for(InternalNode child : node.getChildren()) {
            if(child.getPruned())
                this.pruned++;
            prunednodes(child);
        }
    }
}
