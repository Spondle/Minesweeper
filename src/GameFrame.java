import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {

    GameFrame() {

        this.setTitle("Minesweeper");
        this.setResizable(false);

        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TopPanel topPanel = new TopPanel();
        GamePanel gamePanel = new GamePanel();
        topPanel.setGamePanel(gamePanel);
        gamePanel.setTopPanel(topPanel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(gamePanel, BorderLayout.CENTER);
        this.add(panel);
        this.setContentPane(panel);
        gamePanel.setGameFrame(this);
        this.pack();
        this.setLocationRelativeTo(null);





    }
}
