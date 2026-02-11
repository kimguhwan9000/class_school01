// 1. 클래스 이름은 대문자로 시작 (java_test -> JavaTest)
public class JavaTest02Setter {
    public static void main(String[] args) {
        System.out.println("성우 특집으로 꾸며 드리고 있는 KBS 연기대상");

        String title = "KBS 연기대상";
        int year = 2026;

        System.out.println(year + "년 " + title);
        sayHello();

        // 2. 객체 생성 시 변수명은 소문자로 시작 (Actor -> actor)
        // 생성자를 사용하여 처음부터 이름을 가지고 태어나게 함
        VoiceActor actor = new VoiceActor("강수진");

        // 초기 상태 출력
        actor.printInfo();

        // 3. Setter를 사용하여 안전하게 이름 변경
        actor.setName("안지환");
        actor.printInfo();
    }

    public static void sayHello() {
        System.out.println("반갑습니다. 자바를 정복 중입니다!");
    }
}

// 클래스 이름은 CamelCase 권장
class VoiceActor {
    // 4. 캡슐화: 변수를 private으로 숨겨 외부에서 함부로 못 바꾸게 보호함
    private String name;

    // 5. 생성자(Constructor): 객체가 만들어질 때 실행되는 특수 함수
    public VoiceActor(String name) {
        this.name = name;
    }

    // 6. 표준 Setter: 관례적으로 'set + 변수명'으로 이름 지음
    public void setName(String name) {
        // 이름이 비어있지 않을 때만 변경하는 등의 '보호 로직' 추가 가능
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    // 정보를 출력하는 함수
    public void printInfo() {
        System.out.println("성우 이름 : " + this.name);
    }
}
