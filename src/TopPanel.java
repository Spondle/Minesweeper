import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;


public class TopPanel extends JPanel {

    private JLabel flags = new JLabel("40");
    private JLabel time = new JLabel("00:00");
    private Timer timer;
    private int msElapsed;
    private JButton difficulty;
    private JPopupMenu levels;
    private JMenuItem easy;
    private JMenuItem medium;
    private JMenuItem hard;
    private int diff = 2;
    private JLabel flag;
    private ImageIcon flagPole;
    private JLabel clock;
    private ImageIcon stopwatch;
    private GamePanel gamePanel;

    public TopPanel() {
        this.setLayout(new FlowLayout());
        this.difficulty = new JButton("Medium");
        this.levels = new JPopupMenu();
        this.easy = new JMenuItem("Easy");
        this.medium = new JMenuItem("Medium");
        this.hard = new JMenuItem("Hard");
        this.levels.add(easy);
        this.levels.add(medium);
        this.levels.add(hard);
        this.difficulty.addActionListener(e -> levels.show(difficulty, 0, difficulty.getHeight()));
        easy.addActionListener(e -> this.setDifficulty(1));
        medium.addActionListener(e -> this.setDifficulty(2));
        hard.addActionListener(e -> this.setDifficulty(3));

        flagPole = new ImageIcon("src/assets/Flag.png");
        flag = new JLabel(flagPole);

        stopwatch = new ImageIcon("src/assets/Stopwatch.png");
        clock = new JLabel(stopwatch);


        timer = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                msElapsed++;
                uptdateTime();

            }
        });
        this.add(difficulty);
        this.add(flag);
        this.add(flags);
        this.add(clock);
        this.add(time);



    }

    public void setDifficulty(int i) {
        this.diff = i;
        switch (i) {
            case 1:
                difficulty.setText("Easy");
                gamePanel.setDifficulty(1);
                break;
            case 2:
                difficulty.setText("Medium");
                gamePanel.setDifficulty(2);
                break;
            case 3:
                difficulty.setText("Hard");
                gamePanel.setDifficulty(3);
                break;

        }



    }

    public void setFlags(int count) {
        String number = Integer.toString(count);
        flags.setText(number);

    }

    public void uptdateTime() {
        int ms = msElapsed % 1000;
        int sec = msElapsed / 1000;
        int min = msElapsed / 60000;
        int hour = msElapsed / 3600000;

        String timeElapsed = String.format("%02d:%02d:%02d:%03d", hour, min, sec, ms);
        time.setText(timeElapsed);


    }

    public void start() {
        timer.start();
    }

    public int getScore() {
        return msElapsed;

    }

    public void stop() {
        timer.stop();
    }

    public void reset() {
        timer.stop();
        msElapsed = 0;
        uptdateTime();

    }

    public void setGamePanel(GamePanel l) {
        this.gamePanel = l;
    }

    public GamePanel getGamePanel() {
        return gamePanel;

    }


}
