package class_school01;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpaceU_mainGui extends JFrame {
    private static final long serialVersionUID = 1L;
    private List<PlanetU2> planets;

    // 생성자: 행성 리스트를 받음
    public SpaceU_mainGui(List<PlanetU2> planets) {
        this.planets = planets;
        
        setTitle("우주 행성 실시간 관측");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        add(new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                if (SpaceU_mainGui.this.planets != null) {
                    for (PlanetU2 p : SpaceU_mainGui.this.planets) {
                        g.setColor(Color.YELLOW);
                        g.fillOval(p.getX(), p.getY(), 20, 20); // 행성 그림

                        g.setColor(Color.WHITE);
                        g.drawString(p.getName(), p.getX(), p.getY() - 5); // 이름 표시
                    }
                }
            }
        });
    }

    // ⭐ 실행의 주체: 메인 함수를 이 파일로 옮겼습니다.
    public static void main(String[] args) {
        // 1. 데이터 준비 (SpaceU.java에 있는 PlanetU2 클래스 사용)
        List<PlanetU2> planetList = new ArrayList<>();
        planetList.add(new PlanetU2("수성", 50, 80));
        planetList.add(new PlanetU2("금성", 120, 150));
        planetList.add(new PlanetU2("지구", 200, 200));
        planetList.add(new PlanetU2("화성", 280, 230));
        planetList.add(new PlanetU2("목성", 350, 300));
        planetList.add(new PlanetU2("토성", 450, 350));
        planetList.add(new PlanetU2("천왕성", 100, 400));
        planetList.add(new PlanetU2("해왕성", 200, 450));
        planetList.add(new PlanetU2("명왕성", 350, 480));
        planetList.add(new PlanetU2("X행성", 500, 100));

        // 2. GUI 창 띄우기
        SwingUtilities.invokeLater(() -> {
            SpaceU_mainGui gui = new SpaceU_mainGui(planetList);
            gui.setVisible(true);
        });
    }
}
