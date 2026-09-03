import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

public class TopPanel extends JPanel {

    public static final int EASY = 1;
    public static final int MEDIUM = 2;
    public static final int HARD = 3;

    private final JLabel flags = new JLabel("040");
    private final JLabel time = new JLabel("00:00:00:000");
    private final JButton difficulty;
    private final JPopupMenu levels;
    private final JMenuItem easy;
    private final JMenuItem medium;
    private final JMenuItem hard;

    private JLabel flag;
    private JLabel clock;

    private Timer timer;
    private long startTime;
    private int msElapsed;
    private int diff = MEDIUM;
    private GamePanel gamePanel;

    public TopPanel() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 16, 6));
        this.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        this.difficulty = new JButton("Medium");
        this.difficulty.setFocusable(false);
        this.difficulty.setToolTipText("Change game difficulty");

        this.levels = new JPopupMenu();
        this.easy = new JMenuItem("Easy");
        this.medium = new JMenuItem("Medium");
        this.hard = new JMenuItem("Hard");
        this.levels.add(easy);
        this.levels.add(medium);
        this.levels.add(hard);

        this.difficulty.addActionListener(e -> levels.show(difficulty, 0, difficulty.getHeight()));
        easy.addActionListener(e -> setDifficulty(EASY));
        medium.addActionListener(e -> setDifficulty(MEDIUM));
        hard.addActionListener(e -> setDifficulty(HARD));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int baseSize = Math.max(16, screenSize.height / 45);

        Font monoFont = new Font(Font.MONOSPACED, Font.BOLD, baseSize);
        Font uiFont = new Font(flags.getFont().getName(), Font.BOLD, baseSize);

        flags.setFont(monoFont);
        time.setFont(monoFont);
        flags.setToolTipText("Flags remaining");
        time.setToolTipText("Elapsed time");

        difficulty.setFont(uiFont);
        easy.setFont(uiFont);
        medium.setFont(uiFont);
        hard.setFont(uiFont);

        ImageIcon flagIcon = loadScaledIcon("/assets/Flag.png", baseSize * 2);
        ImageIcon clockIcon = loadScaledIcon("/assets/Stopwatch.png", baseSize * 2);
        flag = (flagIcon != null) ? new JLabel(flagIcon) : new JLabel();
        clock = (clockIcon != null) ? new JLabel(clockIcon) : new JLabel();

        timer = new Timer(40, e -> {
            msElapsed = (int) (System.currentTimeMillis() - startTime);
            updateTime();
        });

        this.add(difficulty);
        this.add(flag);
        this.add(flags);
        this.add(clock);
        this.add(time);

        updateTime();
    }

    private ImageIcon loadScaledIcon(String resourcePath, int size) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) return null;
        ImageIcon icon = new ImageIcon(url);
        if (icon.getImage() == null) return null;
        Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public void updateDifficultyDisplay(int i) {
        this.diff = i;
        switch (i) {
            case EASY:
                difficulty.setText("Easy");
                break;
            case HARD:
                difficulty.setText("Hard");
                break;
            case MEDIUM:
            default:
                difficulty.setText("Medium");
                break;
        }
    }

    public void setDifficulty(int i) {
        updateDifficultyDisplay(i);
        reset();
        if (gamePanel != null) {
            gamePanel.setDifficulty(i);
        }
    }

    public void setFlags(int count) {
        if (count >= 0) {
            flags.setText(String.format("%03d", count));
        } else {
            flags.setText(String.valueOf(count));
        }
    }

    public void updateTime() {
        int ms = msElapsed % 1000;
        int sec = (msElapsed / 1000) % 60;
        int min = (msElapsed / 60000) % 60;
        int hour = msElapsed / 3600000;

        time.setText(String.format("%02d:%02d:%02d:%03d", hour, min, sec, ms));
    }

    public void start() {
        startTime = System.currentTimeMillis() - msElapsed;
        timer.start();
    }

    public int getScore() {
        return msElapsed;
    }

    public void stop() {
        if (timer.isRunning()) {
            msElapsed = (int) (System.currentTimeMillis() - startTime);
            timer.stop();
            updateTime();
        }
    }

    public void reset() {
        timer.stop();
        msElapsed = 0;
        updateTime();
    }

    public void setGamePanel(GamePanel l) {
        this.gamePanel = l;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
