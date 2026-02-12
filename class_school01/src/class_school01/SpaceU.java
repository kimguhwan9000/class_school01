package class_school01;

// 이 클래스는 SpaceU_mainGui에서 데이터를 만들 때 '설계도'로 사용됩니다.
class PlanetU2 {
    String name;
    int x, y;

    PlanetU2(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    String getName() { return name; }
    int getX() { return x; }
    int getY() { return y; }
}

public class SpaceU {
    // 여기는 비워두거나 다른 테스트 용도로 쓰셔도 됩니다.
}
