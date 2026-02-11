package class_school01;

import java.util.ArrayList;
import java.util.List;

class PlanetNew {
    String name;
    int x, y;

    // 생성자를 만들면 10개를 만들 때 코드가 훨씬 줄어듭니다.
    PlanetNew(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    String getName() { return name; }
    int getX() { return x; }
    int getY() { return y; }
}

public class Space_array {
    public static void main(String[] args) {
        // 1. 리스트 생성 (행성들을 담을 바구니)
        List<PlanetNew> planets = new ArrayList<>();

        // 2. 반복문을 사용하거나 직접 10개 추가
        planets.add(new PlanetNew("수성", 10, 20));
        planets.add(new PlanetNew("금성", 30, 40));
        planets.add(new PlanetNew("지구", 50, 60));
        planets.add(new PlanetNew("화성", 70, 80));
        planets.add(new PlanetNew("목성", 100, 200));
        planets.add(new PlanetNew("토성", 150, 250));
        planets.add(new PlanetNew("천왕성", 200, 300));
        planets.add(new PlanetNew("해왕성", 250, 350));
        planets.add(new PlanetNew("명왕성", 300, 400));
        planets.add(new PlanetNew("X행성", 400, 500));
        planets.add(new PlanetNew("수성", 10, 20));
        planets.add(new PlanetNew("금성", 30, 40));
        planets.add(new PlanetNew("지구", 50, 60));
        planets.add(new PlanetNew("화성", 70, 80));
        planets.add(new PlanetNew("목성", 100, 200));
        planets.add(new PlanetNew("토성", 150, 250));
        planets.add(new PlanetNew("천왕성", 200, 300));
        planets.add(new PlanetNew("해왕성", 250, 350));
        planets.add(new PlanetNew("명왕성", 300, 400));
        planets.add(new PlanetNew("X행성", 400, 500));
        planets.add(new PlanetNew("수성", 10, 20));
        planets.add(new PlanetNew("금성", 30, 40));
        planets.add(new PlanetNew("지구", 50, 60));
        planets.add(new PlanetNew("화성", 70, 80));
        planets.add(new PlanetNew("목성", 100, 200));
        planets.add(new PlanetNew("토성", 150, 250));
        planets.add(new PlanetNew("천왕성", 200, 300));
        planets.add(new PlanetNew("해왕성", 250, 350));
        planets.add(new PlanetNew("명왕성", 300, 400));
        planets.add(new PlanetNew("X행성", 400, 500));
        
        
        System.out.println("======= 행성 리스트 (총 " + planets.size() + "개) =======");

        // 3. 가장 깔끔한 출력 방식 (for-each 문)
        for (PlanetNew p : planets) {
            System.out.println("행성: " + p.getName() + " | 위치: (" + p.getX() + ", " + p.getY() + ")");
        }
        
        for (int i = 0; i < planets.size(); i++) {
            PlanetNew p = planets.get(i);
            // i + 1을 하면 0번이 아닌 1번부터 표시할 수 있습니다.
            System.out.println((i + 1) + "번 행성: " + p.getName() + " | 위치: (" + p.getX() + ", " + p.getY() + ")");
        }
    }
}
