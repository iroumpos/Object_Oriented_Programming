package ce326.hw1;

import java.lang.Math;

public class Tree {
    public TreeNode root;

    public Tree(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {
        return root;
    }

    public String postorder(TreeNode node) {
        String str = "";
        
            TreeNode left = node.getLeft();
            
            if(left != null) {
                str += postorder(left);
            }
            TreeNode right = node.getRight();
            if(right != null) {
                str += postorder(right);
            }
            str += node.toString();
            str += " ";
    
        return str;
    }

    public String toString() {
        String str;
        str = postorder(root);
        return str;
    }
	
    public double calculate(TreeNode node) {
        double res=0;
        char operator;


        if(node.getType() == false) {
            TreeNode left = node.getLeft();
            TreeNode right = node.getRight();
            operator = node.getOperator();
            
            if(operator == '+') {
                return calculate(left) + calculate(right);
            }
            if(operator == '-') {
                return calculate(left) - calculate(right);
            }
            if(operator == '*') {
                return calculate(left) * calculate(right);
            }
            if(operator == 'x') {
                return calculate(left) * calculate(right);
            }
            if(operator == '/') {
                return calculate(left) / calculate(right);
            }
            if(operator == '^') {
                return Math.pow(calculate(left), calculate(right));
            }

        }
        /*else{
            res = node.getNumber();
            res = Double.parseDouble(res)
            res = node.getNumber();
        }*/
        return Double.parseDouble(node.getNumber());
    }

    public double calculate() {
        double result;
        result = calculate(root);
        return result;
    }

    public String toDotString(TreeNode node) {
        String str = "";

        if(node == null) {
            return "";
        }

        if(node.getType() == true) {
            str += node.hashCode() + " [label=\"" + node.getNumber() + "\", shape = circle, color = black]\n"; 
        }

        else {
            str += node.hashCode() + " [label=\"" + node.getOperator() + "\", shape = circle, color = black]\n";
            str += node.hashCode() + "->" + node.getLeft().hashCode() + "\n";
            str += node.hashCode() + "->" + node.getRight().hashCode() + "\n";
        }

        if(node.getRight() != null) {
            str += toDotString(node.getRight());
        }
        if(node.getLeft() != null) {
            str += toDotString(node.getLeft());
        }
        return str;
    }

    public String toDotString() {
        String str;
        str = toDotString(root);
        return str;
    }

}
