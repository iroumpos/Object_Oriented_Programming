package ce326.hw2;

import java.util.ArrayList;

public class Minimizer {
    private InternalNode node;

    public Minimizer(InternalNode node) {
        this.node = node;
    }

    public double getMinValue() {
        double min = Double.MAX_VALUE;

        ArrayList<InternalNode> children = node.getChildren();
        if (children != null) {
            for(InternalNode child : children) {
                if(child.getChildren() == null || child.getChildernSize() == 0 ) {
                    InternalNode temp = child;
                    min = Math.min(min,temp.getValue());
                } else {
                    Minimizer childMin = new Minimizer(child);
                    min = Math.min(min, childMin.getMinValue());
                }

            }
        }
        return min;
    }
}
