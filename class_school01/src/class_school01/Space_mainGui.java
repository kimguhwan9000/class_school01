package class_school01;

import javax.swing.*;
import java.awt.*;

public class Space_mainGui extends JFrame {
    private static final long serialVersionUID = 1L;
    private Planet planet;

    public Space_mainGui(Planet planet) {
        this.planet = planet;
        
        setTitle("우주 행성 위치 관측");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // 그림을 그리는 판(Panel)을 추가합니다.
        add(new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 우주 배경
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                // 행성 그리기
                int x = planet.getX();
                int y = planet.getY();
                String name = planet.getName();

                g.setColor(Color.YELLOW);
                g.fillOval(x, y, 30, 30);

                g.setColor(Color.WHITE);
                g.drawString(name, x, y - 10);
                g.drawString("행성 정보: " + name + " (" + x + "," + y + ")", 20, 30);
            }
        });
    }

    // ⭐ 이 메인 함수가 있어야 프로그램이 시작됩니다!
    public static void main(String[] args) {
        // 1. 데이터 준비
        Planet planet = new Planet(); 
        
        // 2. GUI 생성 및 실행
        SwingUtilities.invokeLater(() -> {
            Space_mainGui gui = new Space_mainGui(planet);
            gui.setVisible(true); // 이제 창이 뜹니다!
        });
    }
}
