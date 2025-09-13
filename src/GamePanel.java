
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	//change later
    private int rows = 14;
    private int columns = 18;

    private int mines = 40;
    private int maxFlags = 40;
    private int flags = 40;

    private boolean minesPlaced = false;
    private JButton spots[][];
    private boolean mine[][];
    private boolean flag[][];
    private boolean spotsCleared[][];

    private ImageIcon bomb = new ImageIcon("src/assets/Mine.png");
    private ImageIcon notMine = new ImageIcon("src/assets/x.png");

    private ImageIcon[] numbers;
    private ImageIcon flagPole = new ImageIcon("src/assets/Flag.png");
    private TopPanel topPanel;
    private JPanel gameOverPanel;
    private int[] bestScores = new int[3];
    private JLabel bestScore;
    private JLabel currentScore;
    private int diff = 2;

    private GameFrame gameFrame;
    private JButton playAgain;
    private ImageIcon stopwatch = new ImageIcon("src/assets/Stopwatch.png");
    private JLabel clock = new JLabel(stopwatch);
    private ImageIcon Record = new ImageIcon("src/assets/Record.png");
    private JLabel best = new JLabel(Record);
    private File scores = new File("src/assets/scores.txt");
    private Scanner scanner;
    private PrintStream printStream;
    private FileWriter writer;
    private JPanel leftGameOverPanel;
    private JPanel rightGameOverPanel;



    GamePanel() {
        init();


    }
    public void init() {
        this.setLayout(new GridLayout(rows, columns));
        this.spots = new  JButton[rows][columns];
        this.spotsCleared = new boolean[rows][columns];
        this.mine = new boolean[rows][columns];
        this.flag = new boolean[rows][columns];
        this.gameOverPanel = new JPanel(new BorderLayout());
        this.bestScore = new JLabel("_____");
        this.currentScore = new JLabel("_____");
        this.playAgain = new JButton();
        this.leftGameOverPanel = new JPanel(new BorderLayout());
        this.rightGameOverPanel = new JPanel(new BorderLayout());
        //gameOverPanel.setPreferredSize(new Dimension(300, 300));
        //playAgain.setPreferredSize(new Dimension(200, 100));
        try {
            this.scanner = new Scanner(scores);
            int i = 0;
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int num = Integer.parseInt(line);
                System.out.println(line);
                bestScores[i] = num;
                i++;


            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        leftGameOverPanel.add(new JLabel("Score:"), BorderLayout.NORTH);
        rightGameOverPanel.add(new JLabel("High Score:"), BorderLayout.NORTH);
        leftGameOverPanel.add(currentScore, BorderLayout.CENTER);
        rightGameOverPanel.add(bestScore, BorderLayout.CENTER);
        gameOverPanel.add(leftGameOverPanel, BorderLayout.WEST);
        gameOverPanel.add(rightGameOverPanel, BorderLayout.EAST);
        playAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDifficulty(diff);
            }
        });
        playAgain.setText("Try Again!");
        gameOverPanel.add(playAgain, BorderLayout.SOUTH);
        this.numbers = new ImageIcon[9];
        for(int i = 1; i < 9; i++) {
            String path = "src/assets/" + Integer.toString(i) + ".png";
            numbers[i] =  new ImageIcon(path);


        }
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                spots[i][j] = new JButton();
                this.add(spots[i][j]);
            }
        }



        startGame();

    }

    public void resetGame() {
        topPanel.reset();
        minesPlaced = false;
        flags = maxFlags;
        topPanel.setFlags(flags);
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                spots[i][j].setIcon(null);
                spots[i][j].setEnabled(true);
                mine[i][j] = false;
                flag[i][j] = false;
                spotsCleared[i][j] = false;




            }
        }
        this.removeAll();
        this.revalidate();
        this.repaint();
        init();


    }

    public void startGame() {
        minesPlaced = false;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                final int row = i;
                final int column = j;
                flag[i][j] = false;
                spotsCleared[i][j] = false;
                spots[i][j].setPreferredSize(new Dimension(30, 30));
                spots[i][j].setFont(new Font(spots[i][j].getFont().getName(), getFont().getStyle(), 10));
                spots[i][j].setForeground(Color.RED);
                spots[i][j].addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("blop (" + row + ", " + column + ")");
                        if(!minesPlaced) {
                            minePlacement(row, column);
                            topPanel.start();

                        }
                        if(!flag[row][column] && !mine[row][column]) {
                            clearSpots(row, column);
                        }
                        if(mine[row][column]) {

                            lose();

                        }


                    }
                });
                spots[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(SwingUtilities.isRightMouseButton(e)) {
                            if(!flag[row][column] && !spotsCleared[row][column]) {
                                spots[row][column].setIcon(flagPole);
                                flag[row][column] = true;
                                flags--;
                                topPanel.setFlags(flags);
                            } else if(flag[row][column]) {
                                spots[row][column].setIcon(null);
                                flag[row][column] = false;
                                flags++;
                                topPanel.setFlags(flags);

                            }

                        }
                    }

                });



            }

        }

    }

    public void win() {
        topPanel.stop();
        int score = topPanel.getScore();
        if(score < bestScores[diff-1]) {
            bestScores[diff-1] = score;
            try {
                writer = new FileWriter("src/assets/scores.txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                printStream = new PrintStream(scores);
                for(int i = 0; i < 3; i++) {
                    printStream.println(bestScores[i]);
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }




        }
        bestScore.setText(String.valueOf(bestScores[diff-1]));
        currentScore.setText(String.valueOf(score));


        this.removeAll();
        this.setLayout(new BorderLayout());
        this.revalidate();
        this.repaint();
        this.add(gameOverPanel, BorderLayout.CENTER);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        //System.out.println("won");

    }

    public void lose() {
        topPanel.stop();
        System.out.println("lost");

        // First, reveal the board and disable all buttons
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if (mine[i][j] && !flag[i][j]) {
                    // This is an unflagged mine, show the bomb icon
                    spots[i][j].setIcon(bomb);
                    spots[i][j].setDisabledIcon(bomb);

                } else if (flag[i][j] && !mine[i][j]) {
                    // This flag was placed on a non-mine, show the 'X' icon
                    spots[i][j].setIcon(notMine);
                    spots[i][j].setDisabledIcon(notMine);
                }

                // Disable the button to prevent further clicks
                spots[i][j].setEnabled(false);
            }
        }

        // Update the score labels for the game over panel
        currentScore.setText(Integer.toString(topPanel.getScore()));
        if (bestScores[diff - 1] == 2147483647) { // Integer.MAX_VALUE
            bestScore.setText("______");
        } else {
            bestScore.setText(Integer.toString(bestScores[diff - 1]));
        }

        // Create a timer to switch to the game over panel after a 3-second delay
        Timer gameOverTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code will run on the EDT after the delay
                removeAll();
                setLayout(new BorderLayout());
                add(gameOverPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
            }
        });

        // Ensure the timer only runs once
        gameOverTimer.setRepeats(false);

        // Start the timer
        gameOverTimer.start();
    }

	private void minePlacement(int row, int column) {
        int placedMines = 0;
		while(placedMines < mines) {
            boolean doNotPlace = false;
            int minePlacementRow = (int)(Math.random() * ((rows - 1)) + 1);
            int minePlacementColumn = (int)(Math.random() * ((columns - 1)) + 1);
            if(minePlacementRow == row && minePlacementColumn == column) {
                continue;

            }
            for(int j = -1; j <= 1; j++) {
                for(int k = -1; k <= 1; k++) {
                    if(minePlacementRow == (row+j) && minePlacementColumn == (column+k)) {
                        doNotPlace = true;
                        break;
                    }
                    if(doNotPlace) {
                        break;
                    }
                }
            }

            if(doNotPlace) {
                continue;

            }

            if(mine[minePlacementRow][minePlacementColumn]) {
                continue;
            }

            System.out.println("mine (" + minePlacementRow + ", " + minePlacementColumn + ")");
            mine[minePlacementRow][minePlacementColumn] = true;
            placedMines++;
            System.out.println(placedMines);


        }
        /*
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(mine[i][j]) {
                    spots[i][j].setText("X");

                }

            }

        }
        */

        minesPlaced = true;
		
	}

    private void clearSpots(int row, int column) {
        int spotsLeft = 0;
    	spots[row][column].setEnabled(false);
        if(flag[row][column]) {
            spots[row][column].setIcon(null);
            flag[row][column] = false;
            flags++;
            topPanel.setFlags(flags);

        }
        spotsCleared[row][column] = true;
    	int k = checkAdjacentSpots(row, column);
    	if(k == 0) {
    		for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++){
                    if(i == 0 && j == 0) {
                        continue;

                    }
                    try {
                        if(spotsCleared[row+i][column+j]) {
                            continue;

                        }
                    } catch (Exception e) {
                        continue;
                    }
                    System.out.println("(" + (row+i) + ", " + (column+j) + ")");
                    try {
                    	//spots[row+i][column+j].setEnabled(false);
    	                clearSpots((row+i), (column+j));
                    } catch(Exception e) {
                    	continue;
                    	
                    }

               	}
        	}
            	
    	} else {
            spots[row][column].setIcon(numbers[k]);
            spots[row][column].setDisabledIcon(numbers[k]);



        }

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(!spotsCleared[i][j] && !mine[i][j]) {
                    spotsLeft++;


                }


            }
        }

        if(spotsLeft == 0){
            win();
        }

    }

    private int checkAdjacentSpots(int row, int column) {
        int adjacentMines = 0;
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++){
                if(i == 0 && j == 0) {
                    continue;

                }
                try {
                    if(spotsCleared[row+i][column+j]) {
                        continue;

                    }
                } catch (Exception e) {
                    continue;
                }

                System.out.println("(" + (row+i) + ", " + (column+j) + ")");
                try {
	                if(mine[row+i][column+j]) {
	                    adjacentMines++;
	                }
                } catch(Exception e) {
                	continue;
                	
                }

            }

        
        }
        /*
        if(adjacentMines == 0) {
        	for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++){
                    if(i == 0 && j == 0) {
                        continue;

                    }
                    System.out.println("(" + (row+i) + ", " + (column+j) + ")");
                    try {
    	                clearSpots(row+i, column+j);
                    } catch(Exception e) {
                    	continue;
                    	
                    }

                }
        	
        	}
        }
         */
        
        return adjacentMines;

    }

    public void setTopPanel(TopPanel l) {
        this.topPanel = l;

    }

    public TopPanel getTopPanel() {
        return topPanel;

    }

    public void setDifficulty(int difficulty) {
        diff = difficulty;
        System.out.println("diff jsut theing: " + diff);
        switch(difficulty){
            case 1:
                mines = 10;
                flags = 10;
                rows = 9;
                columns = 10;
                this.removeAll();
                this.revalidate();
                this.repaint();
                init();
                topPanel.setFlags(flags);
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
                break;
            case 2:
                mines = 40;
                flags = 40;
                rows = 14;
                columns = 18;
                this.removeAll();
                this.revalidate();
                this.repaint();
                init();
                topPanel.setFlags(flags);
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
                break;
            case 3:
                mines = 99;
                flags = 99;
                rows = 20;
                columns = 24;
                this.removeAll();
                this.revalidate();
                this.repaint();
                init();
                topPanel.setFlags(flags);
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
                break;


        }

    }

    public GameFrame getGameFrame() {
        return gameFrame;

    }

    public Dimension resizeGame() {
        return gameFrame.getSize();

    }

    public void setGameFrame(GameFrame l) {
        this.gameFrame = l;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
