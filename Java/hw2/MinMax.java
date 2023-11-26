package ce326.hw2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

//import org.json.*;



public class MinMax {
    

    
    public static void main(String[] args) throws IOException, IllegalAccessException{
        Tree tree = null;
        OptimalTree opttree = null;
        File file = null;
        Scanner sc = new Scanner(System.in);
        //StringBuilder str = new StringBuilder();
        
        while(true) {
            System.out.println();
            System.out.println("-i <filename>   :  insert tree from file");
            System.out.println("-j [<filename>] :  print tree in the specified filename using JSON format");
            System.out.println("-d [<filename>] :  print tree in the specified filename using DOT format");
            System.out.println("-c              :  calculate tree using min-max algorithm");
            System.out.println("-p              :  calculate tree using min-max and alpha-beta pruning optimization");
            System.out.println("-q              :  quit this program");
            System.out.println();
            System.out.println("$> ");

            String option = sc.nextLine();
            
            switch (option.substring(0, 2)) {
                case "-i":
                    
                    file = new File(option.substring(3));
                    // if (file.isDirectory() == true)
                    //     System.out.println("It is directory");
                    if(file.exists() == false) {
                        System.out.println("Unable to find '"+option.substring(3)+"'");
                        System.out.println();
                        System.out.println();
                        break;
                    } else if(file.canRead() == false) {
                        System.out.println("Unable to open '"+option.substring(3)+"'");
                        System.out.println();
                        System.out.println();
                        break;
                    }    
                    
                    //System.out.println("File name is: "+filename);
                    
                    tree = new Tree(file);
                    if(tree!=null) {
                        if (tree.getRoot() != null) {
                            System.out.println();
                            System.out.println("OK");
                            System.out.println();
                            System.out.println();
                        } else {
                            System.out.println();
                            System.out.println();
                        }
                    }    
                    break;
                case "-c":
                    /*InternalNode root = null;
                    if(tree != null)
                        root = (InternalNode) tree.getRoot();
                    if (root.getType().equals("leaf")) {
                        System.out.println();
                        System.out.println("Not OK");
                        System.out.println();
                        System.out.println();
                        break;
                    }*/

                    if (tree != null) {
                        System.out.println(); 
                        ArrayList<Integer> path = tree.optimalPath();
                        if (path != null && path.size() > 0) {
                            for (int i=0; i < path.size()-1; i++) {
                                System.out.print(path.get(i)+", ");
                            }
                        
                            System.out.print(path.get(path.size()-1));
                            System.out.println();
                            System.out.println();  
                        } else if (path == null){
                            System.out.print("Not OK");
                            System.out.println();
                            System.out.println();
                        }
                    } else {
                        System.out.println();
                        System.out.println("Not OK");
                        System.out.println();
                        System.out.println();
                    }

                    break;
                case "-p":

                    
                        //File myfile = new File(option.substring(3));
                        opttree = new OptimalTree(file);
                        //OptimalTree opttree = (OptimalTree) tree; 
                        // System.out.println("check: "+((InternalNode) opttree.getRoot()).getType())
                        opttree.MinMax();
                        System.out.println();
                        System.out.print("[" + opttree.size() + "," + (opttree.CountPrunedNodes()) + "] ");
                        ArrayList<Integer> optpath = opttree.optimalPath();
                        //System.out.println(optpath);
                        if (optpath != null && optpath.size() > 0) {
                            for (int i=0; i < optpath.size()-1; i++) {
                                System.out.print(optpath.get(i)+", ");
                            }
                        
                            System.out.println(optpath.get(optpath.size()-1));
                            System.out.println();
                            System.out.println();

                        }
                    
                    break;
                case "-j":
                    boolean checker = true;
                    boolean opt = false;
                    if (option.length() > 2 && tree!=null) {
                        File jfile = new File(option.substring(3));
                        if (jfile.exists()) {
                            System.out.println();
		    		    	System.out.println("File '"+ option.substring(3) +"' already exists");    
                            System.out.println();
                            System.out.println();   
                            checker = false;                 
                        }
                        else {
                        try{
                            if (jfile.createNewFile() == false) {
		    		    	    System.out.println();
		    		    	    System.out.println("Unable to write '"+ option.substring(3) +"'");
                                System.out.println();
                                System.out.println();
                                checker = false;
                            }
                        } catch (IOException ex) {
                                System.out.println();
		    		    	    System.out.println("Unable to write '"+ option.substring(3) +"'");
                                System.out.println();
                                System.out.println();
                                checker = false;
                        }
                        }
                    }

                    // Output for Optimal Tree
                    if (checker == true && opttree != null) {
                        //tree.TraverseTree();
                        opttree.TraverseTree();
                        String output = opttree.toString();
                        output += "\n";
                        opt = true;
                        try {
                            FileWriter fw = new FileWriter(option.substring(3),true);
                            fw.write(output);
                            fw.close();
                            System.out.println();
                            System.out.println("OK");
                            System.out.println();
                            System.out.println();


                        } catch (Exception e) {
                            // TODO: handle exception
                            System.err.println("Error appending content to file: " + e.getMessage());
                        }
                    } 

                    // Output for normal Tree
                    if (checker == true && tree != null && opt == false) {
                        tree.TraverseTree();
                        String output = tree.toString();
                        output += "\n";
                        
                        try {
                            FileWriter fw = new FileWriter(option.substring(3),true);
                            fw.write(output);
                            fw.close();
                            System.out.println();
                            System.out.println("OK");
                            System.out.println();
                            System.out.println();

                        } catch (Exception e) {
                            // TODO: handle exception
                            System.err.println("Error appending content to file: " + e.getMessage());
                        }
                    }
                    break;
                case "-d":
                boolean flag = true;
                boolean value = false;
                if (option.length() > 2 && tree!=null) {
                    File jfile = new File(option.substring(3));
                    if (jfile.exists()) {
                        System.out.println();
                        System.out.println("File '"+ option.substring(3) +"' already exists");    
                        System.out.println();
                        System.out.println();   
                        flag = false;                 
                    }
                    else {
                    try{
                        if (jfile.createNewFile() == false) {
                            System.out.println();
                            System.out.println("Unable to write '"+ option.substring(3) +"'");
                            System.out.println();
                            System.out.println();
                            flag = false;
                        }
                    } catch (IOException ex) {
                            System.out.println();
                            System.out.println("Unable to write '"+ option.substring(3) +"'");
                            System.out.println();
                            System.out.println();
                            flag = false;
                    }
                    }
                }

                // Output for Optimal Tree
                if (flag == true && opttree != null) {
                    //tree.TraverseTree();
                    opttree.TraverseTree();
                    String output = opttree.toDotString();
                    output += "\n";
                    opt = true;
                    try {
                        FileWriter fw = new FileWriter(option.substring(3),true);
                        fw.write(output);
                        fw.close();
                        System.out.println();
                        System.out.println("OK");
                        System.out.println();
                        System.out.println();


                    } catch (Exception e) {
                        // TODO: handle exception
                        System.err.println("Error appending content to file: " + e.getMessage());
                    }
                } 

                // Output for normal Tree
                if (flag == true && tree != null && value == false) {
                    tree.TraverseTree();
                    String output = tree.toDotString();
                    output += "\n";
                    
                    try {
                        FileWriter fw = new FileWriter(option.substring(3),true);
                        fw.write(output);
                        fw.close();
                        System.out.println();
                        System.out.println("OK");
                        System.out.println();
                        System.out.println();

                    } catch (Exception e) {
                        // TODO: handle exception
                        System.err.println("Error appending content to file: " + e.getMessage());
                    }
                }
                break;
                case "-q":
                    sc.close();
                    return;
            }
        
        }

    }
}
