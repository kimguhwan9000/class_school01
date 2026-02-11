import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 메인 창 설정: 상단에 점수판(StatusBar)을 추가했습니다.
 */
public class Tetris extends JFrame {
    private JLabel statusBar;

    public Tetris() {
        statusBar = new JLabel(" Score: 0  |  Lines: 0"); // 점수 표시 레이블
        statusBar.setFont(new Font("Monospaced", Font.BOLD, 14));
        statusBar.setForeground(Color.WHITE);
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.DARK_GRAY);
        
        add(statusBar, BorderLayout.NORTH); // 화면 상단에 배치
        add(new Board(statusBar));         // 게임 보드에 상태바 전달

        setTitle("Java Tetris Pro");
        setSize(300, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Tetris().setVisible(true);
        });
    }
}

/**
 * 게임 보드: 점수 계산 로직이 추가되었습니다.
 */
class Board extends JPanel implements Runnable {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;
    private final int DELAY = 400;

    private Thread thread;
    private boolean isPaused = false;
    private int curX = 0, curY = 0;
    private int numLinesRemoved = 0; // 지운 줄 수
    private int totalScore = 0;       // 총 점수
    private int[][] board;
    private Shape curPiece;
    private JLabel statusBar;

    public Board(JLabel statusBar) {
        this.statusBar = statusBar;
        setFocusable(true);
        setBackground(Color.BLACK);
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        curPiece = new Shape();
        addKeyListener(new TAdapter());
        newPiece();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        thread = new Thread(this);
        thread.start();
    }

    private void newPiece() {
        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 - 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(0);
            thread = null;
            statusBar.setText(" GAME OVER! Score: " + totalScore);
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) return false;
            if (board[y][x] != 0) return false;
        }
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    /**
     * 줄 제거 및 점수 계산: 
     * 1줄: 100점 / 2줄: 300점 / 3줄: 500점 / 4줄(테트리스): 800점
     */
    private void removeFullLines() {
        int currentLines = 0;
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 0) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                currentLines++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) board[k] = board[k + 1].clone();
                i--;
            }
        }

        if (currentLines > 0) {
            numLinesRemoved += currentLines;
            // 보너스 점수 시스템
            int bonus = switch(currentLines) {
                case 1 -> 100;
                case 2 -> 300;
                case 3 -> 500;
                case 4 -> 800;
                default -> 0;
            };
            totalScore += bonus;
            statusBar.setText(" Score: " + totalScore + "  |  Lines: " + numLinesRemoved);
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int squareWidth = (int) getSize().getWidth() / BOARD_WIDTH;
        int squareHeight = (int) getSize().getHeight() / BOARD_HEIGHT;

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] != 0) drawSquare(g, j * squareWidth, (BOARD_HEIGHT - 1 - i) * squareHeight, board[i][j]);
            }
        }
        if (curPiece.getShape() != 0) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, x * squareWidth, (BOARD_HEIGHT - 1 - y) * squareHeight, curPiece.getShape());
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, int shape) {
        Color[] colors = {Color.BLACK, new Color(204, 102, 102), new Color(102, 204, 102), new Color(102, 102, 204),
                          new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204), new Color(218, 170, 0)};
        g.setColor(colors[shape]);
        g.fillRect(x + 1, y + 1, (int) getSize().getWidth() / BOARD_WIDTH - 2, (int) getSize().getHeight() / BOARD_HEIGHT - 2);
        g.setColor(colors[shape].brighter()); // 블록 테두리 효과
        g.drawRect(x + 1, y + 1, (int) getSize().getWidth() / BOARD_WIDTH - 2, (int) getSize().getHeight() / BOARD_HEIGHT - 2);
    }

    @Override
    public void run() {
        while (thread != null) {
            try {
                Thread.sleep(DELAY);
                if (!isPaused) {
                    if (!tryMove(curPiece, curX, curY - 1)) {
                        for (int i = 0; i < 4; i++) board[curY - curPiece.y(i)][curX + curPiece.x(i)] = curPiece.getShape();
                        removeFullLines();
                        newPiece();
                    }
                }
            } catch (InterruptedException e) { break; }
        }
    }

    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();
            if (keycode == 'P' || keycode == 'p') isPaused = !isPaused;
            if (isPaused) return;
            switch (keycode) {
                case KeyEvent.VK_LEFT -> tryMove(curPiece, curX - 1, curY);
                case KeyEvent.VK_RIGHT -> tryMove(curPiece, curX + 1, curY);
                case KeyEvent.VK_DOWN -> tryMove(curPiece, curX, curY - 1);
                case KeyEvent.VK_UP -> tryMove(curPiece.rotateLeft(), curX, curY);
                case KeyEvent.VK_SPACE -> { while (tryMove(curPiece, curX, curY - 1)); }
            }
        }
    }
}

class Shape {
    private int[][] coords = new int[4][2];
    private int pieceShape;
    private static final int[][][] allCoords = {
        {{0,0}, {0,0}, {0,0}, {0,0}}, {{0,-1}, {0,0}, {-1,0}, {-1,1}}, {{0,-1}, {0,0}, {1,0}, {1,1}},
        {{0,-1}, {0,0}, {0,1}, {0,2}}, {{-1,0}, {0,0}, {1,0}, {0,1}}, {{0,0}, {1,0}, {0,1}, {1,1}},
        {{-1,-1}, {0,-1}, {0,0}, {0,1}}, {{1,-1}, {0,-1}, {0,0}, {0,1}}
    };

    public void setShape(int shape) {
        for (int i = 0; i < 4; i++) System.arraycopy(allCoords[shape][i], 0, coords[i], 0, 2);
        pieceShape = shape;
    }
    public void setRandomShape() { setShape((int) (Math.random() * 7) + 1); }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public int getShape() { return pieceShape; }
    public int minY() {
        int res = coords[0][1];
        for (int i = 1; i < 4; i++) res = Math.min(res, coords[i][1]);
        return res;
    }
    public Shape rotateLeft() {
        if (pieceShape == 5) return this;
        Shape result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; i++) {
            result.coords[i][0] = y(i);
            result.coords[i][1] = -x(i);
        }
        return result;
    }
}
