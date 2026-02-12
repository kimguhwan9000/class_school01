package class_school01;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

class Entity {
    float x, y, speed, dirX, dirY;
    boolean isExiting = true;
    Entity(float x, float y, float speed) { this.x = x; this.y = y; this.speed = speed; }
}

public class SuperPacman extends JFrame {
    private static final long serialVersionUID = 1L;
    private final int MAP_SIZE = 20, TILE_SIZE = 30, CHAR_SIZE = 22;
    private int[][] currentMap = new int[MAP_SIZE][MAP_SIZE];
    private Entity player;
    private List<Entity> ghosts = new ArrayList<>();
    private int stage = 1, score = 0, smallTotal = 0, smallEaten = 0;
    private boolean isHunter = false;
    private long hunterTimer = 0;
    
    private enum GameState { PLAYING, LOAD_MENU }
    private GameState state = GameState.PLAYING;
    private Set<Integer> pressedKeys = new HashSet<>();
    private List<String> saveFiles = new ArrayList<>();
    private float scrollOffset = 0;
    private String statusMsg = ""; 
    private long statusMsgTimer = 0;

    public SuperPacman() {
        setTitle("PACMAN FINAL - CLICK RECOVERED");
        setSize(615, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        player = new Entity(TILE_SIZE + 4, TILE_SIZE + 4, 6.0f);
        initStage(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { 
                pressedKeys.add(e.getKeyCode()); 
                if (state == GameState.LOAD_MENU) handleMenuKey(e.getKeyCode());
            }
            public void keyReleased(KeyEvent e) { pressedKeys.remove(e.getKeyCode()); }
        });

        addMouseWheelListener(e -> {
            if (state == GameState.LOAD_MENU) {
                scrollOffset -= e.getWheelRotation() * 35.0f;
                fixScroll();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (state == GameState.LOAD_MENU) {
                    // ⭐ 가장 확실한 방법: 마우스가 클릭된 '창 내부' 좌표를 직접 사용합니다.
                    // 창의 테두리와 상단바 두께(Insets)를 빼주어 순수 그림 영역 좌표를 구합니다.
                    Insets insets = getInsets();
                    int mx = e.getX() - insets.left;
                    int my = e.getY() - insets.top;
                    handleMouseClick(mx, my);
                }
            }
        });

        new Timer(16, e -> { update(); repaint(); }).start();
    }

    private void initStage(boolean resetPos) {
        int[][] maze = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,1,1,0,1,1,0,1,1,1,0,1,1,0,1}, {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,0,1}, {1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
            {1,1,1,0,0,1,1,1,0,0,0,0,1,1,1,0,0,1,1,1}, {1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
            {1,0,1,1,0,1,0,1,1,0,0,1,1,0,1,0,1,1,0,1}, {1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,0,1}, {1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
            {1,1,1,0,0,1,1,1,0,1,1,0,1,1,1,0,0,1,1,1}, {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,1,1,0,1,1,0,1,1,1,0,1,1,0,1}, {1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1},
            {1,0,0,0,0,1,1,1,1,0,0,1,1,1,1,0,0,0,0,1}, {1,0,1,1,0,1,1,1,1,0,0,1,1,1,1,0,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        for (int i = 0; i < 20; i++) System.arraycopy(maze[i], 0, currentMap[i], 0, 20);
        Random rnd = new Random();
        int pCount = 0, targetPills = 4 + (stage - 1) * 2;
        while (pCount < targetPills) {
            int rx = rnd.nextInt(20), ry = rnd.nextInt(20);
            if (currentMap[ry][rx] == 0) { currentMap[ry][rx] = 2; pCount++; }
        }
        smallTotal = 0; smallEaten = 0;
        for (int[] row : currentMap) for (int t : row) if (t == 0) smallTotal++;
        if (resetPos) { player.x = TILE_SIZE + 4; player.y = TILE_SIZE + 4; player.dirX = 0; player.dirY = 0; }
        ghosts.clear();
        for (int i = 0; i < 4 + (stage - 1) * 2; i++) ghosts.add(new Entity(285, 285, 2.0f + (stage * 0.1f)));
        isHunter = false;
    }

    private void update() {
        if (state == GameState.LOAD_MENU) return;
        if (pressedKeys.contains(KeyEvent.VK_S)) saveGame();
        if (pressedKeys.contains(KeyEvent.VK_L)) { state = GameState.LOAD_MENU; refreshSaveList(); scrollOffset = 0; }

        float nx = player.x, ny = player.y;
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) nx -= player.speed;
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) nx += player.speed;
        if (!isWall(nx, player.y)) player.x = nx;
        if (pressedKeys.contains(KeyEvent.VK_UP)) ny -= player.speed;
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) ny += player.speed;
        if (!isWall(player.x, ny)) player.y = ny;

        int tx = (int) (player.x + 11) / TILE_SIZE;
        int ty = (int) (player.y + 11) / TILE_SIZE;
        if (ty >= 0 && ty < 20 && tx >= 0 && tx < 20) {
            if (currentMap[ty][tx] == 0) { score += 10; currentMap[ty][tx] = -1; smallEaten++; }
            else if (currentMap[ty][tx] == 2) { isHunter = true; hunterTimer = System.currentTimeMillis() + 6000; score += 50; currentMap[ty][tx] = -1; }
        }
        if (isHunter && System.currentTimeMillis() > hunterTimer) isHunter = false;
        if (smallTotal > 0 && smallEaten >= smallTotal) { stage++; initStage(true); return; }

        Random rnd = new Random();
        for (Entity g : ghosts) {
            if (g.isExiting) { if (g.y > 210) g.y -= g.speed; else g.isExiting = false; }
            else {
                float gx = g.x + g.dirX * g.speed, gy = g.y + g.dirY * g.speed;
                if (isWall(gx, gy) || rnd.nextInt(40) == 0) {
                    int d = rnd.nextInt(4);
                    float[] vx = {0, 0, -1, 1}, vy = {-1, 1, 0, 0};
                    g.dirX = vx[d]; g.dirY = vy[d];
                } else { g.x = gx; g.y = gy; }
            }
            if (Math.abs(player.x - g.x) < 18 && Math.abs(player.y - g.y) < 18) {
                if (isHunter) { g.x = 285; g.y = 285; g.isExiting = true; score += 200; }
                else { stage = 1; score = 0; initStage(true); return; }
            }
        }
    }

    private boolean isWall(float nx, float ny) {
        int l = (int) (nx + 3) / 30, r = (int) (nx + CHAR_SIZE - 3) / 30;
        int t = (int) (ny + 3) / 30, b = (int) (ny + CHAR_SIZE - 3) / 30;
        if (l < 0 || r >= 20 || t < 0 || b >= 20) return true;
        return (currentMap[t][l] == 1 || currentMap[t][r] == 1 || currentMap[b][l] == 1 || currentMap[b][r] == 1);
    }

    private String getSavePath() {
        String path = System.getenv("APPDATA") + File.separator + "PacmanJava";
        new File(path).mkdirs();
        return path;
    }

    private void saveGame() {
        if (System.currentTimeMillis() < statusMsgTimer) return;
        String name = "SAVE_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        try (PrintWriter out = new PrintWriter(new File(getSavePath(), name))) {
            out.println(stage + " " + score + " " + player.x + " " + player.y);
            for (int[] row : currentMap) { for (int t : row) out.print(t + " "); out.println(); }
            statusMsg = "Game Saved: " + name; 
            statusMsgTimer = System.currentTimeMillis() + 3000;
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleMouseClick(int mx, int my) {
        String dir = getSavePath();
        // ⭐ 이제 그리기 좌표(150)와 클릭 좌표(my)가 1:1로 매칭됩니다.
        for (int i = 0; i < saveFiles.size(); i++) {
            float iy = 150 + i * 45 + scrollOffset;
            
            // 1. 세이브 항목 (불러오기)
            if (mx >= 80 && mx <= 450 && my >= iy && my <= iy + 40) {
                String fileName = saveFiles.get(i);
                loadGame(new File(dir, fileName));
                state = GameState.PLAYING;
                statusMsg = "Game Loaded: " + fileName; // 불러오기 성공 메시지
                statusMsgTimer = System.currentTimeMillis() + 3000;
                break;
            }
            // 2. 삭제 버튼 (X)
            if (mx >= 460 && mx <= 500 && my >= iy && my <= iy + 40) {
                new File(dir, saveFiles.get(i)).delete();
                refreshSaveList(); fixScroll();
                break;
            }
        }
    }

    private void loadGame(File f) {
        try (Scanner sc = new Scanner(f)) {
            stage = sc.nextInt(); score = sc.nextInt();
            player.x = sc.nextFloat(); player.y = sc.nextFloat();
            smallTotal = 0; smallEaten = 0;
            for (int y = 0; y < 20; y++) for (int x = 0; x < 20; x++) {
                currentMap[y][x] = sc.nextInt();
                if (currentMap[y][x] == 0) smallTotal++;
                if (currentMap[y][x] == -1) { smallTotal++; smallEaten++; }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void refreshSaveList() {
        saveFiles.clear();
        File[] files = new File(getSavePath()).listFiles((d, n) -> n.startsWith("SAVE_"));
        if (files != null) {
            for (File f : files) saveFiles.add(f.getName());
            saveFiles.sort(Collections.reverseOrder());
        }
    }

    private void handleMenuKey(int code) {
        if (code == KeyEvent.VK_ESCAPE) state = GameState.PLAYING;
        if (code == KeyEvent.VK_UP) { scrollOffset += 25; fixScroll(); }
        if (code == KeyEvent.VK_DOWN) { scrollOffset -= 25; fixScroll(); }
    }

    private void fixScroll() {
        float maxS = Math.max(0, saveFiles.size() * 45 - 380);
        if (scrollOffset > 0) scrollOffset = 0;
        if (scrollOffset < -maxS) scrollOffset = -maxS;
    }

    public void paint(Graphics g) {
        Image buf = createImage(getWidth(), getHeight());
        if (buf == null) return;
        Graphics bg = buf.getGraphics();
        draw(bg);
        g.drawImage(buf, 0, 0, this);
    }

    private void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // ⭐ 실제 컴포넌트 내부 크기를 구해서 그립니다.
        Insets insets = getInsets();
        g2.translate(insets.left, insets.top);

        g2.setColor(new Color(5, 5, 20)); 
        g2.fillRect(0, 0, getWidth(), getHeight());

        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                int dy = y * 30 + 60, dx = x * 30 + 7;
                if (currentMap[y][x] == 1) { g2.setColor(new Color(40, 60, 180)); g2.fillRect(dx, dy, 28, 28); }
                else if (currentMap[y][x] == 0) { g2.setColor(Color.LIGHT_GRAY); g2.fillRect(dx + 13, dy + 13, 4, 4); }
                else if (currentMap[y][x] == 2) { g2.setColor(Color.WHITE); g2.fillOval(dx + 8, dy + 8, 14, 14); }
            }
        }

        for (Entity gh : ghosts) {
            g2.setColor(isHunter ? Color.CYAN : Color.RED);
            g2.fillRect((int) gh.x + 7, (int) gh.y + 60, CHAR_SIZE, CHAR_SIZE);
        }

        g2.setColor(Color.YELLOW); g2.fillOval((int) player.x + 7, (int) player.y + 60, CHAR_SIZE, CHAR_SIZE);
        g2.setColor(Color.WHITE); g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2.drawString("STAGE:" + stage + " SCORE:" + score + " LEFT:" + (smallTotal - smallEaten), 25, 50);

        if (System.currentTimeMillis() < statusMsgTimer) {
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Monospaced", Font.BOLD, 15));
            g2.drawString(statusMsg, 25, 650);
        }

        if (state == GameState.LOAD_MENU) {
            g2.setColor(new Color(0, 0, 0, 235)); g2.fillRect(50, 100, 510, 480);
            g2.setColor(Color.WHITE); g2.drawString("SAVE LIST (ESC to Close)", 80, 135);

            float viewH = 400, totalH = saveFiles.size() * 45;
            if (totalH > viewH) {
                g2.setColor(new Color(60, 60, 60)); g2.fillRect(535, 150, 10, (int) viewH);
                float hH = (viewH / totalH) * viewH;
                float hY = 150 + (-scrollOffset / (totalH - viewH)) * (viewH - hH);
                g2.setColor(Color.CYAN); g2.fillRect(535, (int) hY, 10, (int) hH);
            }

            Shape old = g2.getClip();
            g2.setClip(50, 150, 480, 400); 
            for (int i = 0; i < saveFiles.size(); i++) {
                int iy = (int) (150 + i * 45 + scrollOffset);
                g2.setColor(new Color(70, 70, 70)); g2.fillRect(80, iy, 370, 40); 
                g2.setColor(new Color(200, 50, 50)); g2.fillRect(460, iy, 40, 40); 
                g2.setColor(Color.WHITE); g2.drawString(saveFiles.get(i), 90, iy + 25);
                g2.drawString("X", 475, iy + 25);
            }
            g2.setClip(old);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SuperPacman().setVisible(true));
    }
}
