package ce326.hw1;

import java.util.Scanner;
import java.lang.String;

public class ArithmeticCalculator {
    

    public static TreeNode BuildTree(String str) {
       
        int cnt_par = 0; // count parenthesis
        int j;
        
        for(int i = str.length()-1; i>=0; i--){
            int temp_right=0;
            int temp_left=0;
            if(str.charAt(0) == '(' && str.charAt(str.length()-1) == ')') {
                for( j=str.length()-2; j>=1; j--){
                    if(str.charAt(j) == ')')
                            temp_right++;
                    if(str.charAt(j) == '(') {
                        temp_left++;
                        if(temp_right == 0)
                            break;
                    }
                }
                if(j == 0 && (temp_left < temp_right)) {
                    //System.out.println("Num of par: "+temp);
                    str = "(" + str;
                    //System.out.println("hi there(add): "+str);
                    str = str.substring(1, str.length()-1);
                    //System.out.println("New str: "+str);
                    i = str.length()-1;

                }

                if(j == 0 && (temp_left > temp_right)) {
                    //System.out.println("Num of par: "+temp);
                    str = str + ")";
                    //System.out.println("The string: "+str);
                    str = str.substring(2, str.length()-2);
                    i = str.length()-2;
                }

                if(j == 0 && (temp_left == temp_right)) {
                    //System.out.println("Num of par: "+temp);
                    str = str.substring(1, str.length()-1);
                    i = str.length()-1;
                }
            
            }
            
            if(str.charAt(i) == ')') {
                cnt_par--;
            }
            if(str.charAt(i) == '(') {
                cnt_par++;
            }

           
            if(cnt_par == 0 && (str.charAt(i) == '+' || str.charAt(i) == '-')) {
                
                //System.out.println("left exp: "+str.substring(0, i));
                //System.out.println("right exp: "+str.substring(i+1,str.length()));
                // System.out.println("str: "+str);
                TreeNode left = BuildTree(str.substring(0, i));
                TreeNode right = BuildTree(str.substring(i+1));
                TreeNode parent = new TreeNode(left, right, str.charAt(i));
                return parent;
            }
        }
        
        for(int i = str.length()-1; i>=0; i--){
            int temp_right=0;
            int temp_left=0;
            if(str.charAt(0) == '(' && str.charAt(str.length()-1) == ')') {
                for( j=str.length()-2; j>=1; j--){
                    if(str.charAt(j) == ')')
                            temp_right++;
                    if(str.charAt(j) == '(') {
                        temp_left++;
                        if(temp_right == 0)
                            break;
                    }
                }
                if(j == 0 && (temp_left < temp_right)) {
                    
                    str = "(" + str;
                    //System.out.println("Hi there(mul): "+str);
                    str = str.substring(1, str.length()-1);
                    //System.out.println("New str: "+str);
                    i = str.length()-1;
                }

                if(j == 0 && (temp_left > temp_right)) {
                    //System.out.println("Num of par: "+temp);
                    str = str + ")";
                    //System.out.println("The string: "+str);
                    str = str.substring(2, str.length()-2);
                    i = str.length()-2;
                }

                if(j == 0 && (temp_left == temp_right)) {
                    //System.out.println("Num of par: "+temp);
                    str = str.substring(1, str.length()-1);
                    i = str.length()-1;
                }
            
            }
            
            if(str.charAt(i) == ')') {
                cnt_par--;
            }
            if(str.charAt(i) == '(') {
                cnt_par++;
            }
            if(cnt_par < 0 && (str.charAt(i) == '*' || str.charAt(i) == 'x' || str.charAt(i) == '/')) {
                TreeNode left = BuildTree(str.substring(1, i));
                TreeNode right = BuildTree(str.substring(i + 1));
                TreeNode parent = new TreeNode(left, right, str.charAt(i));
                return parent;
            }
            if(cnt_par == 0 && (str.charAt(i) == '*' || str.charAt(i) == 'x' || str.charAt(i) == '/')) {
               
                // System.out.println("left exp: "+str.substring(0, i));
                // System.out.println("right exp: "+str.substring(i+1,str.length()));
                TreeNode left = BuildTree(str.substring(0, i));
                TreeNode right = BuildTree(str.substring(i + 1));
                TreeNode parent = new TreeNode(left, right, str.charAt(i));
                return parent;
            }
        }
            
        
        for(int i = str.length()-1; i>=0; i--){
            int temp_right=0;
            int temp_left=0;
            if(str.charAt(0) == '(' && str.charAt(str.length()-1) == ')') {
                for( j=str.length()-2; j>=1; j--){
                    if(str.charAt(j) == ')')
                            temp_right++;
                    if(str.charAt(j) == '(') {
                        temp_left++;
                        if(temp_right == 0)
                            break;
                    }
                }

                if(j == 0 && (temp_left < temp_right)) {
                    //System.out.println("Num of par: "+temp);
                    str = "(" + str;
                    str = str.substring(1, str.length()-1);
                    i = str.length()-1;
                }

                if(j == 0 && (temp_left > temp_right)) {
                    //System.out.println("Num of par: "+temp);
                    str = str + ")";
                    //System.out.println("The string: "+str);
                    str = str.substring(2, str.length()-2);
                    i = str.length()-2;
                }

                if(j == 0 && (temp_left == temp_right)) {
                    //System.out.println("Num of par: "+temp);
                    str = str.substring(1, str.length()-1);
                    i = str.length()-1;
                }
            
            }
            
            if(str.charAt(i) == ')') {
                cnt_par--;
            }
            if(str.charAt(i) == '(') {
                cnt_par++;
            }
            if(cnt_par == 0 && (str.charAt(i) == '^')) {
                //System.out.println("left exp: "+str.substring(0, i));
                //System.out.println("right exp: "+str.substring(i+1,str.length()));
                TreeNode left = BuildTree(str.substring(0, i));
                TreeNode right = BuildTree(str.substring(i + 1));
                TreeNode parent = new TreeNode(left, right, str.charAt(i));
                return parent;
            }
        }
        
        TreeNode value = new TreeNode(null, null, str);
        return value;
    }
    public static void main(String []args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.print("Expression: ");
        String line = sc.nextLine();
        //System.out.println("Expression: "+line);
        char[] operators = {'+','-','/','*','x','^'}; 

        /* Correctness Checks*/
        
        /* Check for opened parenthesis & Starting an expression with ')' */
        String expression = new String(line);
        String str = expression.replaceAll("\\s+","");
        //System.out.println("Expression: "+expression);
        //System.out.println("Expression with whitespace: "+str);
        int parenthesis = 0;
        int i = 0;

        while(i < expression.length()){
            if(expression.charAt(i) == '(') {
                parenthesis++;
                if(i >=1 && expression.charAt(i-1) == ')') {
                    System.out.println("[ERROR] ')' appears before opening parenthesis");
                    System.exit(42);
                }    
            }

            if(expression.charAt(i) == ')') {
                if(parenthesis <= 0){
                    System.out.print("\n");
                    System.out.println("[ERROR] Closing unopened parenthesis");
                    System.exit(42);
                }

                if(parenthesis == 0 && expression.charAt(i+1) == '('){
                    System.out.println("[ERROR] ')' appears before opening parenthesis");
                    System.exit(42);
                }
                parenthesis--;
            }

            /* Checks for invalid character*/
            if((Character.isDigit(expression.charAt(i)) == false) && (expression.charAt(i) != ' ') && (expression.charAt(i) != '(')
                && (expression.charAt(i) != ')')  && (expression.charAt(i) != '+') && (expression.charAt(i) != '-') 
                && (expression.charAt(i) != '/') && (expression.charAt(i) != '*') && (expression.charAt(i) != 'x') && (expression.charAt(i) != '/')
                && (expression.charAt(i) != '^') && (expression.charAt(i) != '.')) {
                    System.out.print("\n");
                    System.out.println("[ERROR] Invalid character");
                    System.exit(42);
                }
            

            /* Consecutive operands */
            
           
            /* Starting or ending an expression with operators*/
            if(expression.charAt(0) == '+' || expression.charAt(expression.length()-1) == '+' || 
                expression.charAt(0) == '-' || expression.charAt(expression.length()-1) == '-' ||
                expression.charAt(0) == '*' || expression.charAt(expression.length()-1) == '*' ||
                expression.charAt(0) == '/' || expression.charAt(expression.length()-1) == '/' ||
                expression.charAt(0) == 'x' || expression.charAt(expression.length()-1) == 'x' ||
                expression.charAt(0) == '^' || expression.charAt(expression.length()-1) == '^') 
                {
                    System.out.println();
                    System.out.println("[ERROR] Starting or ending with operator");
                    System.exit(42);
                }
            
            i++;
        }
        
        if(parenthesis != 0){
            System.out.print("\n");
            System.out.println("[ERROR] Not closing opened parenthesis");
            System.exit(42);
        }

        /* Check for consecutive operators & operators,operands before and after parenthesis */
        i = 0;
        while(i < str.length() - 1){
            if((new String(operators).indexOf(str.charAt(i))) >= 0 && (new String(operators).indexOf(str.charAt(i + 1))) >= 0) {
                System.out.println();
                System.out.println("[ERROR] Consecutive operators");
                System.exit(42);
            }

            if(str.charAt(i) == '(' && (new String(operators).indexOf(str.charAt(i + 1))) >= 0) {
                System.out.println();
                System.out.println("[ERROR] Operator appears after opening parenthesis");
                System.exit(42);
            }

            if(str.charAt(i + 1) == ')' && (new String(operators).indexOf(str.charAt(i))) >= 0) {
                System.out.println();
                System.out.println("[ERROR] Operator appears before closing parenthesis");
                System.exit(42);
            }

            if(Character.isDigit(str.charAt(i)) == true && str.charAt(i + 1) == '(') {
                System.out.println();
                System.out.println("[ERROR] Operand before opening parenthesis");
                System.exit(42);
            }

            if(Character.isDigit(str.charAt(i + 1)) == true && str.charAt(i) == ')') {
                System.out.println();
                System.out.println("[ERROR] Operand after closing parenthesis");
                System.exit(42);
            }

            i++;
        }

        /* Correctness tests have been passed */

        /* Building binary tree */
        
        TreeNode root = BuildTree(str);
        Tree tree = new Tree(root);

       
        
        
        String option = sc.nextLine();
        
        switch (option) {
            case "-s":
                String postfix = tree.toString();
                System.out.println();
                System.out.println("Postfix: "+postfix);
                break;
            case "-c":
                double result = tree.calculate();
                System.out.println();
                System.out.print("Result: ");
                System.out.println((String.format("%.6f", result)));
                break;
            case "-d":
                String todot = tree.toDotString();
                System.out.println();
                System.out.println(todot);
                break;
        }
         
    }
}
