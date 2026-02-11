
package kpax;
//부모 클래스 (Animal)
class Animal{
    String name;

    void eat(){
        System.out.println(name + "이(가) 밥을 먹습니다");
    }
}

// 자식 클래스 (Dog) - Animal을 상속받음
class Dog extends Animal{
    void bark() {
        System.out.println(name + "이(가) 밥을 먹습니다");
    }

}

public class helloworld {
    public static void main(String[] args) {
        Dog myDog = new Dog();

        //부모에게 물려받은 변수와 메서드 사용
        myDog.name = "밍키";
        myDog.eat();

        // 자식 클래스 본인의 메서드 사용
        myDog.bark();
        myDog.bark();
        myDog.bark();
        myDog.bark();
        myDog.bark();
    }    
}
