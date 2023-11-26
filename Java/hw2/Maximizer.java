package ce326.hw2;

import java.util.ArrayList;

public class Maximizer {
    private InternalNode node;

    public Maximizer(InternalNode node) {
        this.node = node;
    }

    public double getMaxValue() {
        double max = Double.MIN_VALUE;

        ArrayList<InternalNode> children = node.getChildren();
        if (children != null) {
            for(InternalNode child : children) {
                if(child.getChildren() == null || child.getChildernSize() == 0) {
                    InternalNode temp = child;
                    max = Math.max(max,temp.getValue());
                } else {
                    Maximizer childMax = new Maximizer(child);
                    max = Math.max(max, childMax.getMaxValue());
                }

            }
        }
        return max;
    }
}
