public class java_test01 {
    public static void main(String[] args) {
        System.out.println("성우 특집으로 꾸며 드리고 있는 KBS연기 대상");

        // 1. 변수 선언과 값 넣기
        String title = "KBS연기대상";
        int year = 2026;

        // 2. 변수 사용해서 출력하기
        System.out.println(year + "년" + title);

        sayHello();

        // [중요] 다른 클래스 사용하기
        // 클래스이름 변수이름 = new 클래스이름();
        Voice_actor Actor = new Voice_actor();


        //다른 클래스 안의 함수 사용하기
        Actor.actors();

        //다른 클래스 안의 함수의 변수의 값 바꾸는 방법
        Actor.name = "안지환";

        Actor.actors();
        
    }

    public static void sayHello(){
        System.out.println("반갑습니다 저는 오랜만에 자바 공부중입니다.");
    }
}

// 새로운 클래스 만들기
class Voice_actor {
    String name = "강수진";
    
    void actors(){
        System.out.println(name);
    }
}
