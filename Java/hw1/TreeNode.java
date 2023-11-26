package ce326.hw1;

public class TreeNode {
    
    private TreeNode left,right;
    private String number;
    private char operator;
    private boolean type; // false: operator
                          // true: operand

    public TreeNode(TreeNode left,TreeNode right, char operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.type = false;
    }

    public TreeNode(TreeNode left,TreeNode right, String number) {
        this.left = left;
        this.right = right;
        this.number = number;
        this.type = true;
    }

    
    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public String getNumber() {
        return number;
    }

    public char getOperator() {
        return operator;
    }

    public boolean getType() {
        return type;
    }


    public String toString() {
        String str = "";
        if (type == false) {
            return str + operator;
        }
        else {
            return str + number;
        }
    }
}
