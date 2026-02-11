import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pacman extends JFrame {
    public Pacman() {
        add(new GameBoard());
        setTitle("Pacman AI - Chasing Mode");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 520);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Pacman().setVisible(true));
    }
}

class GameBoard extends JPanel implements ActionListener {
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    
    private Timer timer;
    private boolean inGame = true;
    private boolean isPowerUp = false;
    private int powerUpTimer = 0;
    
    private int score = 0;
    private int stage = 1;
    private int pacmanX, pacmanY, pacmanDX, pacmanDY, reqDX, reqDY;
    private int ghostX, ghostY, ghostDX, ghostDY;
    private int pacmanAnimPos = 0, pacmanAnimDir = 1;

    // 맵 데이터
    private final short levelData[] = {
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 3, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 3, 1,
        1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1,
        1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1,
        1, 1, 1, 2, 1, 1, 0, 1, 1, 1, 2, 1, 1, 1, 1,
        1, 0, 0, 2, 1, 0, 0, 0, 0, 1, 2, 0, 0, 0, 1,
        1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1,
        1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1,
        1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1,
        1, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1,
        1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1,
        1, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 1,
        1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1,
        1, 3, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 2, 3, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
    };
    private short[] screenData;

    public GameBoard() {
        setFocusable(true);
        setBackground(Color.BLACK);
        addKeyListener(new TAdapter());
        screenData = new short[N_BLOCKS * N_BLOCKS];
        initLevel();
        timer = new Timer(120, this); // 속도 조정
        timer.start();
    }

    private void initLevel() {
        System.arraycopy(levelData, 0, screenData, 0, levelData.length);
        pacmanX = 7 * BLOCK_SIZE; pacmanY = 11 * BLOCK_SIZE;
        pacmanDX = pacmanDY = reqDX = reqDY = 0;
        ghostX = 7 * BLOCK_SIZE; ghostY = 7 * BLOCK_SIZE; // 중앙 근처에서 시작
        ghostDX = ghostDY = 0;
        isPowerUp = false;
    }

    // 몬스터 추격 AI 핵심 로직
    private void moveGhost() {
        // 현재 위치가 타일에 딱 맞을 때만 방향 전환 결정
        if (ghostX % BLOCK_SIZE == 0 && ghostY % BLOCK_SIZE == 0) {
            int targetX = pacmanX;
            int targetY = pacmanY;

            // 파워업 상태면 반대로 도망가도록 타겟 수정
            if (isPowerUp) {
                targetX = (pacmanX > SCREEN_SIZE / 2) ? 0 : SCREEN_SIZE;
                targetY = (pacmanY > SCREEN_SIZE / 2) ? 0 : SCREEN_SIZE;
            }

            // 갈 수 있는 방향 중 팩맨과 가장 가까워지는 방향 선택
            int minDistance = Integer.MAX_VALUE;
            int nextDX = 0, nextDY = 0;

            int[] dx = {BLOCK_SIZE, -BLOCK_SIZE, 0, 0};
            int[] dy = {0, 0, BLOCK_SIZE, -BLOCK_SIZE};

            for (int i = 0; i < 4; i++) {
                if (canMove(ghostX + dx[i], ghostY + dy[i])) {
                    // 다음 칸에서 팩맨까지의 거리 계산 (피타고라스 정리 대신 단순 거리합)
                    int dist = Math.abs((ghostX + dx[i]) - targetX) + Math.abs((ghostY + dy[i]) - targetY);
                    if (dist < minDistance) {
                        minDistance = dist;
                        nextDX = dx[i];
                        nextDY = dy[i];
                    }
                }
            }
            ghostDX = nextDX;
            ghostDY = nextDY;
        }

        ghostX += ghostDX;
        ghostY += ghostDY;

        // 충돌 체크
        if (Math.abs(pacmanX - ghostX) < 15 && Math.abs(pacmanY - ghostY) < 15) {
            if (isPowerUp) {
                ghostX = 7 * BLOCK_SIZE; ghostY = 7 * BLOCK_SIZE; // 잡히면 중앙 리젠
                score += 200;
            } else { inGame = false; }
        }
    }

    private void movePacman() {
        if (reqDX != 0 || reqDY != 0) {
            if (canMove(pacmanX + reqDX, pacmanY + reqDY)) {
                pacmanDX = reqDX; pacmanDY = reqDY;
            }
        }
        if (canMove(pacmanX + pacmanDX, pacmanY + pacmanDY)) {
            pacmanX += pacmanDX; pacmanY += pacmanDY;
            int pos = (pacmanY / BLOCK_SIZE) * N_BLOCKS + (pacmanX / BLOCK_SIZE);
            if (screenData[pos] == 2) { screenData[pos] = 0; score += 10; }
            else if (screenData[pos] == 3) { screenData[pos] = 0; score += 50; isPowerUp = true; powerUpTimer = 40; }
        }
    }

    private boolean canMove(int nx, int ny) {
        if (nx < 0 || nx >= SCREEN_SIZE || ny < 0 || ny >= SCREEN_SIZE) return false;
        return screenData[(ny/BLOCK_SIZE)*N_BLOCKS + (nx/BLOCK_SIZE)] != 1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            movePacman();
            moveGhost();
            if (isPowerUp) { powerUpTimer--; if (powerUpTimer <= 0) isPowerUp = false; }
            updateAnim();
            checkClear();
        }
        repaint();
    }

    private void updateAnim() {
        pacmanAnimPos += pacmanAnimDir;
        if (pacmanAnimPos <= 0 || pacmanAnimPos >= 4) pacmanAnimDir *= -1;
    }

    private void checkClear() {
        boolean cleared = true;
        for (short d : screenData) if (d == 2 || d == 3) cleared = false;
        if (cleared) { stage++; initLevel(); }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < screenData.length; i++) {
            int x = (i % N_BLOCKS) * BLOCK_SIZE;
            int y = (i / N_BLOCKS) * BLOCK_SIZE;
            if (screenData[i] == 1) { g.setColor(new Color(25, 25, 166)); g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE); }
            else if (screenData[i] == 2) { g.setColor(Color.WHITE); g.fillOval(x + 10, y + 10, 4, 4); }
            else if (screenData[i] == 3) { g.setColor(Color.ORANGE); g.fillOval(x + 8, y + 8, 8, 8); }
        }
        g.setColor(Color.YELLOW);
        int angle = (pacmanDX > 0) ? 45 : (pacmanDX < 0) ? 225 : (pacmanDY > 0) ? 315 : 135;
        g.fillArc(pacmanX+2, pacmanY+2, 20, 20, angle + (pacmanAnimPos*10), 360 - (pacmanAnimPos*20));
        g.setColor(isPowerUp ? Color.CYAN : Color.RED);
        g.fillRoundRect(ghostX+2, ghostY+2, 20, 20, 8, 8);
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + score + "  STAGE: " + stage, 20, SCREEN_SIZE + 30);
        if (!inGame) { g.setColor(Color.RED); g.setFont(new Font("Arial", Font.BOLD, 20)); g.drawString("GAME OVER", 130, 240); }
    }

    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) { reqDX = -BLOCK_SIZE; reqDY = 0; }
            else if (key == KeyEvent.VK_RIGHT) { reqDX = BLOCK_SIZE; reqDY = 0; }
            else if (key == KeyEvent.VK_UP) { reqDX = 0; reqDY = -BLOCK_SIZE; }
            else if (key == KeyEvent.VK_DOWN) { reqDX = 0; reqDY = BLOCK_SIZE; }
        }
    }
}
