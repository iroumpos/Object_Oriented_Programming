package ce326.hw2;

import java.util.ArrayList;

public class InternalNode extends TreeNode{
    
    // array for the children of the internal node ?what type?
    private ArrayList<InternalNode> children;
    private double alpha;
    private double beta;
    private double value;
    private String type;
    private boolean pruned;
    

    // default constructor
    public InternalNode() {}

    public InternalNode(ArrayList<InternalNode> array){
        array = this.children;
    }

    public InternalNode(double value, String type, boolean pruned) {
        this.value = value;
        this.type = type;
        this.children = null;
        this.pruned = pruned;
    }

    public InternalNode(Double value, String type, ArrayList<InternalNode> children, double alpha, double beta, boolean pruned) {
        this.value = value;
        this.type = type;
        this.children = children;
        this.alpha = alpha;
        this.beta = beta;
        this.pruned = pruned;
    }

    public void setChildren(int size) {
        this.children = new ArrayList<InternalNode>(size);
    }

    public ArrayList<InternalNode> getChildren() {
        return children;
    }

    public int getChildernSize() {
        return this.children.size();
    }

    public void insertChild(int pos, InternalNode X) {
        this.children.add(pos, X);
    }

    public InternalNode getChild(int pos) {    
        return children.get(pos);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue () {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPruned(boolean pruned) {
        this.pruned = pruned;
    }
    
    public boolean getPruned() {
        return pruned;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha (double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }
    
    public void setBeta (double beta) {
        this.beta = beta;
    }

}
