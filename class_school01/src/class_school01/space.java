package class_school01;


class Planet {
	String name = "목성";
	int x = 100;
	int y = 100;
	
	
	// Getter 메소드들
	String getName() {
		return name;
	}
	
	int getX() {
		return x;
	}
	
	int getY() {
		return y;
	}
	

    // 나중에 위치를 바꿀 수 있도록 Setter도 추가했습니다.
    public void setName(String name) { this.name = name; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}

class Planet2 {
    String name;
    int x, y;

    // 생성자를 만들면 10개를 만들 때 코드가 훨씬 줄어듭니다.
    /*
    생성자의 2대 규칙
    이름 일치: 생성자의 이름은 반드시 클래스 이름과 대소문자까지 똑같아야 합니다.
    반환 타입 없음: 생성자는 void나 String 같은 리턴 타입을 절대 적지 않습니다. (적는 순간 생성자가 아니라 일반 함수가 되어버립니다.)
    */
    Planet2(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    String getName() { return name; }
    int getX() { return x; }
    int getY() { return y; }
}


public class space {
	public static void main(String[] args) {
		
	}
}
