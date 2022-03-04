import java.awt.*;
import java.awt.geom.*;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel {
    private static final long serialVersionUID = 7148504528835036004L;
    private Game game;
    private Point2D center;
    private Point2D origin;

    public Display() {
        super();
        game = new Game();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (center == null) {
            center = new Point2D.Double(getWidth() / 2, getHeight() / 2);
            origin = new Point2D.Double(center.getX() - getWidth() * 0.4, center.getY() - getHeight() * 0.4);
        }
        g2.setColor(Color.BLUE);
        g2.fill(new Rectangle2D.Double(center.getX() - getWidth() * 0.4, center.getY() - getHeight() * 0.4, getWidth() * 0.8, getHeight() * 0.8));

        double hSeparation = getHeight() * 0.8 / game.numRows;
        double wSeparation = getWidth() * 0.8 / game.numCols;
        double diameter = Math.min(hSeparation, wSeparation);

        for (int i = 0; i < game.numRows; i++)
            for (int j = 0; j < game.numCols; j++) {
                Color color;
                if (game.board[i][j] == -1)
                    color = Color.YELLOW;
                else if (game.board[i][j] == 1)
                    color = Color.RED;
                else
                    color = Color.BLUE.darker();
                g2.setColor(color);
                g2.fill(new Ellipse2D.Double(origin.getX() + j * wSeparation, origin.getY() + i * hSeparation, diameter, diameter));
            }
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        do {
            int col = -1;
            if (game.curPlayer == -1) {
                System.out.print("Enter column (0-" + (game.numCols - 1) + "): ");
                col = scanner.nextInt();
                while ((col < 0 || col > game.numCols - 1) || game.turn(col, game.curPlayer) == -1) {
                    System.out.println("Try again");
                    System.out.print("Enter column (0-" + game.numCols + "): ");
                    col = scanner.nextInt();
                }
            } else {
                col = game.findBestMove();
                game.turn(col, game.curPlayer);
            }
            game.curPlayer *= -1;
        } while (game.checkWinner() == 0 && !game.isFull());
        if (game.checkWinner() == 0)
            System.out.println("Tie!");
        else
            System.out.println("Winner: Player " + (game.curPlayer == -1 ? 2 : 1));
        removeAll();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Display panel = new Display();
            panel.setBackground(Color.gray);
            JFrame frame = new JFrame("Connect 4");

            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
