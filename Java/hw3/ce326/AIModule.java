package ce326.hw3;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;



public class AIModule {
    private static int EMPTY = 0;
    private static int AI = 1;
    private static int PLAYER = 2;
    private static int myboard[][];
    private static int ROW = 6;
    private static int COLUMN = 7;
    private static int result[];

    private static class MinMaxResult {
        public int column;
        public int value;

        public MinMaxResult(int column, int value) {
            this.column = column;
            this.value = value;
        }
    }

    public static void printBoard(int[][] board){
        System.out.println(" ------ AI ------");
        System.out.println("-----------------------------");
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++){
                if(board[i][j] == EMPTY)
                    System.out.print("|   ");
                if(board[i][j] == AI)
                    System.out.print("| O ");
                if(board[i][j] == PLAYER)
                    System.out.print("| X ");
            }
            System.out.println("|");
            //System.out.println("|   |   |   |   |   |   |   |");
            System.out.println("-----------------------------");
        }
        System.out.println();
        System.out.println("| 0 | 1 | 2 | 3 | 4 | 5 | 6 |");
    }

    public static void initBoard(int[][] board) {
        for (int i=0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++){
                board[i][j] = EMPTY;
            }
        }
    }

    public static int addtoBoard(int[][] board,int place,int type) {
        for (int i=0; i < ROW; i++) {
            if (board[ROW-1-i][place] == EMPTY) {
                board[ROW-1-i][place] = type;
                return i;
            }
        }
        return -1;
    }

    public static void add(int[][] board, int row,int column, int type) {
        if(board[row][column] == EMPTY)
            board[row][column] = type;
    }

    public static void remove(int[][] board, int row,int column) {
        board[row][column] = EMPTY;
    }


    public static void addtoBoardAI(int[][] board) {
        board[5][3] = AI;
    }

    public static int evaluate(List<Integer> window, int type) {
        int score = 0;
        int opp_score = PLAYER;
        if (type == PLAYER)
            opp_score = AI;

        if (Collections.frequency(window,type) == 4)
            score += 10000;
        if (Collections.frequency(window, type) == 3 && Collections.frequency(window, EMPTY) == 1)
            score += 16;
        if (Collections.frequency(window, type) == 2 && Collections.frequency(window, EMPTY) == 2){
            score += 4;
        }
        if (Collections.frequency(window, type) == 1 && Collections.frequency(window, EMPTY) == 3){
            score += 1;
        }
     

        if (Collections.frequency(window,opp_score) == 4)
            score -= 10000;
        if (Collections.frequency(window, opp_score) == 3 && Collections.frequency(window, EMPTY) == 1)
            score -= 16;
        if (Collections.frequency(window, opp_score) == 2 && Collections.frequency(window, EMPTY) == 2)
            score -= 4;
        if (Collections.frequency(window, opp_score) == 1 && Collections.frequency(window, EMPTY) == 3)
            score -= 1;


        return score;
    }

    public static int score_calc(int[][] board,int type) {
        int score = 0;

        // Check Horizontal
        for (int i=0; i < ROW; i++) {
            int row_array[] = new int[COLUMN];
            for (int j = 0; j < COLUMN; j++) {
                row_array[j] = board[i][j];
            }
            for (int k = 0; k < COLUMN - 3; k++) {
                int[] window_arr = Arrays.copyOfRange(row_array,k,k+4);
                List<Integer> window = Arrays.stream(window_arr).boxed().collect(Collectors.toList());
                score += evaluate(window,type);

            }
        }
        

        // Check Vertical
        for (int i=0; i < COLUMN; i++) {
            int column_array[] = new int[ROW];
            for (int j=0; j < ROW; j++) {
                column_array[j] = board[j][i];
            }
            for (int k=0; k < ROW - 3; k++) {
                int[] window_arr = Arrays.copyOfRange(column_array, k, k+4);
                List<Integer> window = Arrays.stream(window_arr).boxed().collect(Collectors.toList());
                score += evaluate(window, type);
            }
        }
       

        // Check upper diagonal
        for (int i = 0; i < ROW - 3; i++) {
            for (int j = 0; j < COLUMN - 3; j++) {
                int[] window_arr = new int[4];
                for (int k = 0; k < 4; k++) {
                    window_arr[k] = board[i+k][j+k];
                }
                List<Integer> window = Arrays.stream(window_arr).boxed().collect(Collectors.toList());
                score += evaluate(window, type);
            }
        }
       
        // Check lower diagonal
        for (int i = 0; i < ROW - 3; i++) {
            for (int j = 0; j < COLUMN - 3; j++) {
                int[] window_arr = new int[4];
                for (int k = 0; k < 4; k++) {
                    window_arr[k] = board[i+3-k][j+k];
                    // potential change
                    //  window_arr[k] = board[i+k][j-3+k];
                }
                List<Integer> window = Arrays.stream(window_arr).boxed().collect(Collectors.toList());
                score += evaluate(window, type);

            }
        }
        
        // System.out.println("Score is: "+score);
        return score;
    }

    public static boolean check_for_win(int[][] board,int type) {
        // Horizontal check
        for (int i=0; i < COLUMN-3; i++) {
            for (int j=0; j < ROW; j++) {
                if (board[j][i] == type && board[j][i+1] == type && board[j][i+2] == type && board[j][i+3] == type)
                    return true;
            }
        }

        // Vertical check
        for (int i=0; i < COLUMN; i++) {
            for (int j=0; j < ROW - 3; j++) {
                if (board[j][i] == type && board[j+1][i] == type && board[j+2][i] == type && board[j+3][i] == type)
                    return true;
            }
        }

        // Upper diagonal
        for (int i=0; i<COLUMN-3; i++) {
            for(int j=0; j < ROW-3; j++) {
                if (board[j][i] == type && board[j+1][i+1] == type && board[j+2][i+2] == type && board[j+3][i+3] == type)
                    return true;
            }
        }

        // Lower diagonal
        for (int i=0; i < COLUMN-3; i++) {
            for (int j = 3; j < ROW; j++) {
                if (board[j][i] == type && board[j-1][i+1] == type && board[j-2][i+2] == type && board[j-3][i+3] == type)
                    return true;
            }
        }
        return false;
    }

    public static int next_row_available(int[][] board,int col) {
        int i = 0;
        for (i = ROW - 1; i >= 0; i--) {
            if (board[i][col] == EMPTY){
                return i;
            }
        }
        
        return -1; // not available
    }

    public static List<Integer> valid_cols(int[][] board) {
        List<Integer> list = new ArrayList<>();
        for (int i=0; i < COLUMN; i++) {
            if (board[0][i] == EMPTY)
                list.add(i);
        }
        return list;
    }

    public static boolean is_terminal(int[][] board) {
        return check_for_win(board,AI) || check_for_win(board,PLAYER);
    }

    public static int[][] copy_board(int[][] board) {
        int[][] b_copy = new int[ROW][COLUMN];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                b_copy[i][j] = board[i][j];
            }
        }
        return b_copy;
    }


    public static MinMaxResult MinMax(int[][] board,int depth,int alpha,int beta,boolean isMax){
        MinMaxResult output = new MinMaxResult(-1, 0);
        List<Integer> locations = valid_cols(board);
        boolean terminal = is_terminal(board);
        List<List<Integer>> windows;
        int score = 0;
        // System.out.println("Depth is: "+depth);
        if (depth == 0 || terminal == true) {
            // if (terminal == true) {
            //     if (check_for_win(board,AI)) {
            //         output.column = -1;
            //         output.value = 1000000000;
            //         return output;
            //     } else if (check_for_win(board,PLAYER)) {
            //         output.column = -1;
            //         output.value = -1000000000;
            //         return output;
            //     }
            //     else {
            //         output.column = -1;
            //         output.value = 0;
            //         return output;
            //     }
            // } else {
             /* Depth is 0 */
                // System.out.println("Im in depth zero");
                //output.column = -1;
                windows = getAllWindows(board, 4);
                score = evaluateWindows(windows, AI, AI);
                score += evaluateWindows(windows, PLAYER, PLAYER);
                output.value = score;
                return output;
            //}

        }

        if (isMax == true) {
            //result[0] = locations.get(random.nextInt(locations.size()));
            //int column;
            output.value = -Integer.MAX_VALUE;
            /*if(locations.size() > 0)
                column = locations.get((int)(Math.random() * locations.size()));
            else
                column = 0;*/
            for(Integer col : locations) {
                int row = next_row_available(board, col);
                int board_copy[][] = copy_board(board);
                board_copy[row][col] = AI;
                int new_score = MinMax(board_copy, depth - 1, alpha, beta, false).value;
                System.out.println("value: "+new_score);
                //System.out.println(temp);
                if (new_score > output.value) {
                        output.value = new_score;
                        output.column = col;
                }

                alpha = Math.max(alpha,output.value);
                if (alpha >= beta)
                    break;
            }
            return output;
        } else {
            //int column;
            output.value = Integer.MAX_VALUE;
            /*if(locations.size() > 0)
                column = locations.get((int)(Math.random() * locations.size()));
            else
                column = 0;*/
            for(Integer col : locations) {
                int row = next_row_available(board, col);
                int board_copy[][] = copy_board(board);
                board_copy[row][col] = PLAYER;
                int new_score = MinMax(board_copy, depth - 1, alpha, beta, true).value;
                System.out.println("value: "+new_score);
                 if (new_score < output.value) {
                    output.value = new_score;
                    output.column = col;
                }

                beta = Math.min(beta,output.value);
                if (alpha >= beta)
                    break;
            }
            return output;
        }

    }

    public static List<List<Integer>> getAllWindows(int[][] board, int windowLength) {
        List<List<Integer>> windows = new ArrayList<>();

        // Rows
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j <= COLUMN - windowLength; j++) {
                List<Integer> window = new ArrayList<>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(board[i][j + k]);
                }
                windows.add(window);
            }
        }

        // Columns
        for (int i = 0; i <= ROW - windowLength; i++) {
            for (int j = 0; j < COLUMN; j++) {
                List<Integer> window = new ArrayList<>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(board[i + k][j]);
                }
                windows.add(window);
            }
        }

        // Diagonals (upper-left to lower-right)
        for (int i = 0; i <= ROW - windowLength; i++) {
            for (int j = 0; j <= COLUMN - windowLength; j++) {
                List<Integer> window = new ArrayList<>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(board[i + k][j + k]);
                }
                windows.add(window);
            }
        }

        // Diagonals (upper-right to lower-left)
        for (int i = 0; i <= ROW - windowLength; i++) {
            for (int j = COLUMN - 1; j >= windowLength - 1; j--) {
                List<Integer> window = new ArrayList<>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(board[i + k][j - k]);
                }
                windows.add(window);
            }
        }

        return windows;
    }

    public static int evaluateWindow(List<Integer> window, int playerType, int currentPlayer) {
        int countPlayer = Collections.frequency(window, playerType);
        int countEmpty = Collections.frequency(window, EMPTY);

        if (countPlayer == 0) {
            // No player checkers in the window
            if (countEmpty == 4) return 0; // Four blanks
            return 0; // Checkers of different color
        } else if (countPlayer == 1) {
            // One checker of the player's color
            if (countEmpty == 3) {
                //System.out.println("one piece");
                return (currentPlayer == AI) ? 1 : -1;
            }
            return 0; // Other combinations
        } else if (countPlayer == 2) {
            // Two checkers of the player's color
            if (countEmpty == 2) {
                //System.out.println("two piece");
                return (currentPlayer == AI) ? 4 : -4;
            }
            return 0; // Other combinations
        } else if (countPlayer == 3) {
            // Three checkers of the player's color
            if (countEmpty == 1) {
                //System.out.println("three piece");
                return (currentPlayer == AI) ? 16 : -16;
            }
            return 0; // Other combinations
        } else if (countPlayer == 4) {
            // Four checkers of the player's color
            //System.out.println("four piece");
            return (currentPlayer == AI) ? 10000 : -10000; // or more, depending on the priority
        }

        return 0; // Default case
    }

    public static int evaluateWindows(List<List<Integer>> windows, int playerType, int currentPlayer) {
        int totalScore = 0;

        for (List<Integer> window : windows) {
            int windowScore = evaluateWindow(window, playerType, currentPlayer);
            totalScore += windowScore;
        }

        return totalScore;
    }


    public static MinMaxResult myMinMax(int[][] board,int depth,int alpha,int beta,boolean isMax){
        MinMaxResult output = new MinMaxResult(-1, 0);
        List<Integer> locations = valid_cols(board);
        List<List<Integer>> windows;
        int score = 0;
        int bestPos = 0;
        int bestScore = isMax ? -Integer.MAX_VALUE : Integer.MAX_VALUE;

        // System.out.println("locations: "+locations);
        for (Integer col : locations) {
            // System.out.println("column: "+col);
            int row = next_row_available(board, col);
            
            if (depth == 0) {
                    
                    add(board,row,col,AI);
                    windows = getAllWindows(board, 4);
                    score = evaluateWindows(windows, AI, AI);
                    score += evaluateWindows(windows, PLAYER, PLAYER);
                    //score = score_calc(board, AI);
                    System.out.println("score: "+score+" col: "+col);
                    

                if(is_terminal(board)){
                    remove(board, row, col);
                    output.value = score;
                    output.column = col;
                    // System.out.println("Hi from terminal!");
                    return output;
                }
                
                if(score >= bestScore) {
                    bestScore = Math.max(score, bestScore);
                    bestPos = col;
                }

                remove(board, row, col);
                alpha = Math.max(alpha,bestScore);
                if (beta <= alpha)
                    break;
            }
            else {
                if(!isMax) {
                    add(board, row, col, PLAYER);

                    if(is_terminal(board)){
                        remove(board, row, col);
                        output.value = score;
                        output.column = col;
                        return output;
                    }
                    output = myMinMax(board, depth - 1, alpha, beta, true);
                    score = output.value;
                    if(score <= bestScore) {
                        bestScore = Math.min(score, bestScore);
                        bestPos = col;
                    }
                    remove(board, row, col);
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha)
                        break;

                }

                if(isMax) {
                    add(board, row, col, AI);
                    if(is_terminal(board)){
                        remove(board, row, col);
                        output.value = score;
                        output.column = col;
                        return output;
                    }
                    output = myMinMax(board, depth - 1, alpha, beta, false);
                    score = output.value;
                    if (score >= bestScore) {
                        if(score == bestScore) {
                            if(Math.abs(bestPos - 3) > Math.abs(col - 3))
                                bestPos = col;
                        }
                        else {
                            bestPos = col;
                        }
                        bestScore = Math.max(score, bestScore);
                        bestPos = col;
                    }
                    remove(board, row, col);
                    alpha = Math.max(alpha, bestScore);
                    if(beta <= alpha)
                        break;
                }
            }

        }

        output.column = bestPos;
        output.value = bestScore;
        return output;
    }
    

    public static void main(String[] args) {
        myboard = new int[ROW][COLUMN];
        MinMaxResult result;
        List<List<Integer>> windows;
        initBoard(myboard);
        // Initial position for AI
        addtoBoardAI(myboard);

        // add(myboard, 4, 1, AI);
        // add(myboard, 5, 1, PLAYER);
        // add(myboard, 3, 1, PLAYER);

        // add(myboard, 5, 2, PLAYER);
        // add(myboard, 4, 2, AI);
        // add(myboard, 3, 2, PLAYER);
        // add(myboard, 2, 2, AI);

        // add(myboard, 4, 3, PLAYER);
        // add(myboard, 3, 3, AI);
        // add(myboard, 2, 3, AI);
        
        // add(myboard, 5, 4, PLAYER);

        // printBoard(myboard);
        // result = MinMax(myboard, 5, -Integer.MAX_VALUE, Integer.MAX_VALUE, true);
        // System.out.println("Column to check: "+result.column+" Value: "+result.value);
        
        int currentPlayer = PLAYER;

        while(true) {
            printBoard(myboard);
            System.out.println();

            if (currentPlayer == PLAYER){
                Scanner sc = new Scanner(System.in);
                System.out.print("Next ? ");
                String column = sc.next();
                System.out.println();
                addtoBoard(myboard,Integer.parseInt(column),PLAYER);
            }
            printBoard(myboard);
            /* AI decides its move */
            result = MinMax(myboard, 5, -Integer.MAX_VALUE, Integer.MAX_VALUE, true);
            System.out.println("Column to check: "+result.column+" Value: "+result.value);
            int col = result.column;
            try {
                if (myboard[0][col] == EMPTY) {
                    int row = next_row_available(myboard, col);
                    System.out.println("Row: "+row+" Col: "+col);
                    addtoBoard(myboard, col, AI);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("col = -1 value: "+result.value);
            }
            if (check_for_win(myboard, AI)) {
                printBoard(myboard);
                System.out.println("AI WON! BETTER LUCK NEXT TIME");
                System.exit(42);
            }

            if (check_for_win(myboard, PLAYER)) {
                printBoard(myboard);
                System.out.println("CONGRATS U HAVE WON!");
                System.exit(42);
            }
        }

    }
}
