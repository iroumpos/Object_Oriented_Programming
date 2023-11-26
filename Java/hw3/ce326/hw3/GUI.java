package ce326.hw3;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTMLDocument.RunElement;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GUI extends JFrame implements ActionListener{
    
    public static int HEIGHT = 900;
    public static int WIDTH = 800;
    public int ROWS = 6;
    public int COLUMNS = 7;
    public int EMPTY = 0;
    public int AI = 1;
    public int PLAYER = 2;
    public int[][] gameBoard;
    int lastRow = -1;
    int lastCol = -1;
    int col_from_key = -1;
    int depth = -1;
    boolean isPlayerTurn = false;
    boolean endofgame = false;
    int whoWon = -1;    // 0: PLAYER WON ----- 1: AI WON
    MinMaxResult result = new MinMaxResult(-1, 1);
    public Timer timer_red;
    public Timer timer_yellow;
    public Color EMPTY_COLOR = Color.WHITE;
    public Color AI_COLOR = Color.YELLOW;
    public Color PLAYER_COLOR = Color.RED;
    
    int movecount = 0;
    String level;
    String FILENAME = "game_record.xml";
    private static final String CONNECT4_DIRECTORY = "Connect4";
    private List<GameInfo> recordedGames = new ArrayList<>();
    private ArrayList<int[]> recordMoves = new ArrayList<>();



    public LocalDateTime startTime;
    
    JFrame frame;
    JPanel Mypanel;
    JPanel GamePanel;
    JMenuBar menuBar;
    JMenu Newgame,player,History,Help;
    JMenuItem Trivial,Medium,Hard;
    JRadioButtonMenuItem ai,you;
    JDialog modalBox;
    RoundButton[][] boardButtons;
    JList<GameInfo> hList;
    DefaultListModel<GameInfo> listModel;
    WindowListener dialogWindowListener;

    private static class MinMaxResult {
        public int column;
        public int value;

        public MinMaxResult(int column, int value) {
            this.column = column;
            this.value = value;
        }
    }


    public class GameInfo {
        private String filename;
        private ArrayList<int[]> recordedMoves;
    
        public GameInfo(String filename,  ArrayList<int[]> recordedMoves) {
            this.filename = filename;
            this.recordedMoves = recordedMoves;
        }
    
        public String getFilename() {
            return filename;
        }
    
    
        public ArrayList<int[]> getRecordedMoves() {
            return recordedMoves;
        }

        @Override
        public String toString() {
            return filename; // Return just the game name as the string representation
    }
    }
    


    public GUI() {
        this.setTitle("Connect4");
        this.setSize(HEIGHT, WIDTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        GamePanel = new JPanel(new GridLayout(ROWS, COLUMNS));
        GamePanel.setBackground(Color.BLUE);

        /*---------Create Connect4 grid--------- */
        gameBoard = new int[ROWS][COLUMNS];
        initBoard(gameBoard);
        boardButtons = new RoundButton[ROWS][COLUMNS];
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++){
                boardButtons[i][j] = new RoundButton("");
                boardButtons[i][j].setEnabled(true);
                boardButtons[i][j].addActionListener(this);
                GamePanel.add(boardButtons[i][j]);
            }
        }

        // Add a KeyListener to the Connect4GUI class
        addKeyListener(new KeyAdapter() {
        	@Override
            public void keyPressed(KeyEvent e) {
        		int keyCode = e.getKeyCode();
        		if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_6) {
                    col_from_key = keyCode - KeyEvent.VK_0;
                    dropPiece(col_from_key, PLAYER);
                    //System.out.println("Dropping piece for player from keyboard!");
                    if(check_for_win(gameBoard,PLAYER)) {
                        whoWon = 0;
                        //printBoard(gameBoard);
                        
                        ArrayList<int[]> movesCopy = new ArrayList<>(recordMoves);
                        recordGame(level, movesCopy);
                        
                        movecount = 0;
                        JOptionPane.showMessageDialog(GamePanel, "You won!");
                        JOptionPane.getRootFrame().addWindowListener(dialogWindowListener);
                        resetsBoards(gameBoard, boardButtons);
                        
                    }
                    result = AImove();
                    dropPiece(result.column, AI);
                    if(check_for_win(gameBoard,AI)) {
                        whoWon = 1;
                        //printBoard(gameBoard);
                        ArrayList<int[]> movesCopy = new ArrayList<>(recordMoves);
                        recordGame(level, movesCopy);
                        
                        movecount = 0;
                        JOptionPane.showMessageDialog(GamePanel, "You lost!");
                        JOptionPane.getRootFrame().addWindowListener(dialogWindowListener);
                        resetsBoards(gameBoard, boardButtons);
                    }
                }
            }
        });
        // Enable the Connect4GUI to receive key events
        setFocusable(true);
        

        /*--------Menu Options--------- */
        menuBar = new JMenuBar();
        Newgame = new JMenu("New Game");
        Trivial = new JMenuItem("Trivial");
        Medium = new JMenuItem("Medium");
        Hard = new JMenuItem("Hard");

        Trivial.addActionListener(this);
        Medium.addActionListener(this);
        Hard.addActionListener(this);

        
        player = new JMenu("1st Player");
        ButtonGroup group = new ButtonGroup();
        ai = new JRadioButtonMenuItem("AI");
        ai.addActionListener(this);
        group.add(ai);
        player.add(ai);
        you = new JRadioButtonMenuItem("You");
        you.addActionListener(this);
        group.add(you);
        player.add(you);

        History = new JMenu("History");
        // JMenuItem historyItem = new JMenuItem("View History");
        // historyItem.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         System.out.println("I'm in history");
        //     }
        // });

        // History.add(historyItem);
        listModel = new DefaultListModel<>();
        hList = new JList<>(listModel);
        hList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        hList.setModel(listModel);
        hList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = hList.getSelectedIndex();
                    System.out.println("index: "+selectedIndex);
                    if (selectedIndex != -1) {
                        resetsBoards(gameBoard, boardButtons);
                        GameInfo selectedGame = listModel.getElementAt(selectedIndex);
                        System.out.println("name: "+selectedGame.getFilename());

                        reenactGame(selectedGame);
                        
                    }
                }
            }
            
        });
        JScrollPane pane = new JScrollPane(hList);
        
        History.add(pane);

        Help = new JMenu("Help");
        
        dialogWindowListener = new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Perform any desired action when the dialog is closed
                //System.out.println("Dialog closed");
            }
        };

        Newgame.add(Trivial);
        Newgame.add(Medium);
        Newgame.add(Hard);
        menuBar.add(Newgame);
        menuBar.add(player);
        menuBar.add(History);
        menuBar.add(Help);
        this.setJMenuBar(menuBar);
        this.add(GamePanel);
        GamePanel.setVisible(true);
        this.setVisible(true);
       
    }

    public void initBoard(int[][] board){
        for(int i=0; i < ROWS; i++) {
            for (int j=0; j < COLUMNS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public boolean setPiece(int[][] board, int row,int column, int type) {
        if (board[row][column] == EMPTY)
            return false;
        
        board[row][column] = type;
        lastRow = row;
        lastCol = column;
        return true;
    }

    public int[][] getBoard() {
        return gameBoard;
    }

    public JButton[][] getBoardButtons() {
        return boardButtons;
    }

    private int check_column(JButton button) {
        for (int i=0; i < ROWS; i++) {
            for (int j=0; j < COLUMNS; j++) {
                if (boardButtons[i][j] == button) {
                    //System.out.println("Row: "+i+" Column: "+j);
                    return j;
                }   
            }
        }
        return -1;
    }

    private int check_row(int col) {
        if (col == -1)
            return -1;
        for (int i = ROWS - 1; i >= 0; i--) {
            if(gameBoard[i][col] == EMPTY)
                return i;
        }
        return -1;
    }

    public void recordMove(int row, int col, int type) {
        int[] move = new int[] {row, col, type};
        recordMoves.add(move);
    }
    

    public void dropPiece(int column,int type) {
        int row = check_row(column);
        if (column != -1 && row != -1) {
            if (type == AI) {
                gameBoard[row][column] = AI;
                boardButtons[row][column].setBackground(AI_COLOR);
                recordMove(row,column,type);
                movecount++;  
            }
            else if (type == PLAYER) {
                gameBoard[row][column] = PLAYER;
                boardButtons[row][column].setBackground(PLAYER_COLOR);
                recordMove(row,column,type);
                movecount++;
            }
        }
    }

    public void clearRecordedMoves() {
        for(int i=0; i < movecount; i++) {
            recordMove(-1,-1,-1);
        }
    }

    public MinMaxResult AImove(){
        return MinMax(gameBoard, depth, -Integer.MAX_VALUE, Integer.MAX_VALUE, true);
    }

    public void resetsBoards(int[][] board,RoundButton[][] gui) {
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY;
                gui[i][j].setBackground(Color.WHITE);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


private void reenactGame(GameInfo gameInfo) {
    
    ArrayList<int[]> recordedMoves = gameInfo.getRecordedMoves();
    int delay = 3000; // 3 seconds delay

    Timer timer = new Timer(delay, new ActionListener() {
        int currentMoveIndex = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentMoveIndex < recordedMoves.size()) {
                int[] move = recordedMoves.get(currentMoveIndex);
                int row = move[0];
                int col = move[1];
                int type = move[2];

                // Apply the move to the game board and update the GUI
                gameBoard[row][col] = type;
                // Update the GUI components here
                if(type == AI) {
                    boardButtons[row][col].setBackground(AI_COLOR);
                } else {
                    boardButtons[row][col].setBackground(PLAYER_COLOR);
                }

                currentMoveIndex++;
            } else {
                ((Timer) e.getSource()).stop(); // Stop the timer when all moves are done
            }
        }
    });

    timer.start();
}

    public void recordGame(String level, ArrayList<int[]> moves) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
    
            Element rootElement = doc.createElement("game");
            doc.appendChild(rootElement);
    
            Element levelElement = doc.createElement("level");
            levelElement.appendChild(doc.createTextNode(level));
            rootElement.appendChild(levelElement);
    
            Element startTimeElement = doc.createElement("start_time");
            LocalDateTime startTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startTimeElement.appendChild(doc.createTextNode(startTime.format(formatter)));
            rootElement.appendChild(startTimeElement);
    
            Element movesElement = doc.createElement("moves");
            rootElement.appendChild(movesElement);
    
            for (int[] move : moves) {
                Element moveElement = doc.createElement("move");
                moveElement.setAttribute("row", String.valueOf(move[0]));
                moveElement.setAttribute("column", String.valueOf(move[1]));
                moveElement.setAttribute("type", move[2] == AI ? "AI" : "PLAYER");
                movesElement.appendChild(moveElement);
            }
    
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            
            String userhome = System.getProperty("user.home");
            String Connect4path = userhome + File.separator + CONNECT4_DIRECTORY;
            File directory = new File(Connect4path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
    
            String filename = "game_record_";
            filename += startTime.format(formatter);
            filename += ".xml";
            String filepath = Connect4path + File.separator + filename;
            
            FileWriter writer = new FileWriter(filepath);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            
            writer.close();
            String entry = HistoryEntry();
            GameInfo game = new GameInfo(entry, moves);
            listModel.add(0,game);
            recordedGames.add(0, game);
            recordMoves.clear();

            // System.out.println("Game recorded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public String HistoryEntry(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd - HH:mm");
        startTime = LocalDateTime.now();
        String result = startTime.format(formatter);
        result += "  L: " + level;
        if (whoWon == 0)
            result += " W: P";
        if (whoWon == 1)
            result += " W: AI";
        return result;
    }

   

    //---------------------------- LOGIC CONNECT 4 -------------------------------------
    

    public  int addtoBoard(int[][] board,int place,int type) {
        for (int i=0; i < ROWS; i++) {
            if (board[ROWS-1-i][place] == EMPTY) {
                board[ROWS-1-i][place] = type;
                return i;
            }
        }
        return -1;
    }

    
    public void add(int[][] board, int row,int column, int type) {
        if(board[row][column] == EMPTY)
            board[row][column] = type;
    }

    public void remove(int[][] board, int row,int column) {
        board[row][column] = EMPTY;
    }
    public int next_row_available(int[][] board,int col) {
        for (int i = ROWS-1; i >=0; i--) {
            if (board[i][col] == EMPTY)
                return i;
        }
        return -1; // not available
    }

    public List<Integer> valid_cols(int[][] board) {
        List<Integer> list = new ArrayList<>();
        for (int i=0; i < COLUMNS; i++) {
            if (board[0][i] == EMPTY)
                list.add(i);
        }
        return list;
    } 
    public boolean check_for_win(int[][] board,int type) {
        // Horizontal check
        for (int i=0; i < COLUMNS-3; i++) {
            for (int j=0; j < ROWS; j++) {
                if (board[j][i] == type && board[j][i+1] == type && board[j][i+2] == type && board[j][i+3] == type)
                    return true;
            }
        }

        // Vertical check
        for (int i=0; i < COLUMNS; i++) {
            for (int j=0; j < ROWS - 3; j++) {
                if (board[j][i] == type && board[j+1][i] == type && board[j+2][i] == type && board[j+3][i] == type)
                    return true;
            }
        }

        // Upper diagonal
        for (int i=0; i<COLUMNS-3; i++) {
            for(int j=0; j < ROWS-3; j++) {
                if (board[j][i] == type && board[j+1][i+1] == type && board[j+2][i+2] == type && board[j+3][i+3] == type)
                    return true;
            }
        }

        // Lower diagonal
        for (int i=0; i < COLUMNS-3; i++) {
            for (int j = 3; j < ROWS; j++) {
                if (board[j][i] == type && board[j-1][i+1] == type && board[j-2][i+2] == type && board[j-3][i+3] == type)
                    return true;
            }
        }
        return false;
    }
    public boolean is_terminal(int[][] board) {
        return check_for_win(board,AI) || check_for_win(board,PLAYER);
    }

    public int[][] copy_board(int[][] board) {
        int[][] b_copy = new int[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                b_copy[i][j] = board[i][j];
            }
        }
        return b_copy;
    }
    


    public List<List<Integer>> getAllWindows(int[][] board, int windowLength) {
        List<List<Integer>> windows = new ArrayList<>();

        // Rows
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j <= COLUMNS - windowLength; j++) {
                List<Integer> window = new ArrayList<>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(board[i][j + k]);
                }
                windows.add(window);
            }
        }

        // COLUMNSs
        for (int i = 0; i <= ROWS - windowLength; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                List<Integer> window = new ArrayList<>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(board[i + k][j]);
                }
                windows.add(window);
            }
        }

        // Diagonals (upper-left to lower-right)
        for (int i = 0; i <= ROWS - windowLength; i++) {
            for (int j = 0; j <= COLUMNS - windowLength; j++) {
                List<Integer> window = new ArrayList<>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(board[i + k][j + k]);
                }
                windows.add(window);
            }
        }

        // Diagonals (upper-right to lower-left)
        for (int i = 0; i <= ROWS - windowLength; i++) {
            for (int j = COLUMNS - 1; j >= windowLength - 1; j--) {
                List<Integer> window = new ArrayList<>();
                for (int k = 0; k < windowLength; k++) {
                    window.add(board[i + k][j - k]);
                }
                windows.add(window);
            }
        }

        return windows;
    }

    public  int evaluateWindow(List<Integer> window, int playerType, int currentPlayer) {
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

    public  int evaluateWindows(List<List<Integer>> windows, int playerType, int currentPlayer) {
        int totalScore = 0;

        for (List<Integer> window : windows) {
            int windowScore = evaluateWindow(window, playerType, currentPlayer);
            totalScore += windowScore;
        }

        return totalScore;
    }


    public MinMaxResult MinMax(int[][] board,int depth,int alpha,int beta,boolean isMax){
        MinMaxResult output = new MinMaxResult(-1, 0);
        List<Integer> locations = valid_cols(board);
        boolean terminal = is_terminal(board);
        List<List<Integer>> windows;
        int score = 0;
        // System.out.println("Depth is: "+depth);
        if (depth == 0 || terminal == true) {
            if (terminal == true) {
                if (check_for_win(board,AI)) {
                    output.column = -1;
                    output.value = 1000000000;
                    return output;
                } else if (check_for_win(board,PLAYER)) {
                    output.column = -1;
                    output.value = -1000000000;
                    return output;
                }
                else {
                    output.column = -1;
                    output.value = 0;
                    return output;
                }
            } else {
             /* Depth is 0 */
                // System.out.println("Im in depth zero");
                //output.column = -1;
                windows = getAllWindows(board, 4);
                score = evaluateWindows(windows, AI, AI);
                score += evaluateWindows(windows, PLAYER, PLAYER);
                output.value = score;
                return output;
            }

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
                //System.out.println("value: "+new_score);
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
                //System.out.println("value: "+new_score);
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

    public void printBoard(int[][] board){
        System.out.println(" ------ AI ------");
        System.out.println("-----------------------------");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++){
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

    //----------------------------------------------------------------------------------------------------


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ai) {
            isPlayerTurn = false;
        }

        if (e.getSource() == you) {
            //System.out.println("My turn!");
            isPlayerTurn = true;
            resetsBoards(gameBoard, boardButtons);
        }

        if (e.getSource() == Trivial) {
            level = "Trivial";
            depth = 1;
            resetsBoards(gameBoard,boardButtons);
            if (isPlayerTurn == false) {
                resetsBoards(gameBoard,boardButtons);
                dropPiece(3,AI);
            }
        }
        if (e.getSource() == Medium) {
            level = "Medium";
            depth = 3;
            resetsBoards(gameBoard,boardButtons);
            if (isPlayerTurn == false) {
                resetsBoards(gameBoard,boardButtons);
                dropPiece(3,AI);
                
            }
        }

        if (e.getSource() == Hard) {
            level = "Hard";
            depth = 5;
            resetsBoards(gameBoard,boardButtons);
            if (isPlayerTurn == false) {
                resetsBoards(gameBoard,boardButtons);
                dropPiece(3,AI);
                
            }
        }
        
        if (e.getSource() == History) {
            resetsBoards(gameBoard, boardButtons);
            
        }

        JButton button = (JButton) e.getSource();

        int col = check_column(button); 
        int row = check_row(col);
        

        if (col != -1) {
            if (row == -1) {
                System.out.println("Column is full");
                System.exit(42);
            }
            

            if (gameBoard[row][col] == EMPTY) {
                dropPiece(col,PLAYER);
                //printBoard(gameBoard);
                if(movecount == 42){
                    JOptionPane.showMessageDialog(GamePanel, "The game is drawn!");
                }
                if(check_for_win(gameBoard,PLAYER)) {
                    
                    whoWon = 0;
                    
                    // Create a copy of recordMoves before clearing it
                    ArrayList<int[]> movesCopy = new ArrayList<>(recordMoves);
                    
                    recordGame(level, movesCopy);
                   
                    JOptionPane.showMessageDialog(GamePanel, "You won!");
                    endofgame = true; 
                    resetsBoards(gameBoard, boardButtons);
                    // recordMoves.removeAll(recordMoves);
                    //System.out.println("recordmoves array: "+recordMoves);
                
                    //replayGame();
                    
                    movecount = 0;
                }
                
                
                
                    result = AImove();
                    dropPiece(result.column, AI);
                    if(movecount == 42){
                        JOptionPane.showMessageDialog(GamePanel, "The game is drawn!");
                    }
                    // System.out.println("After MinMax is called column: "+result.column+" : "+result.value);
                    //printBoard(gameBoard);
                    if(check_for_win(gameBoard,AI)) {
                        // boardButtons[check_row(result.column)][result.column].setBackground(AI_COLOR);
                        whoWon = 1;
                        
                        ArrayList<int[]> movesCopy = new ArrayList<>(recordMoves);
                        recordGame(level, movesCopy);
                        
                        JOptionPane.showMessageDialog(GamePanel, "You lost!");
                        endofgame = true; 
                        resetsBoards(gameBoard, boardButtons);
                        // recordMoves.removeAll(recordMoves);
                        
                        
                        movecount = 0;
                    }
                   
                
            }
        }
    }


  
}
