//부모 클래스 (Animal)
class Animal{
    String name;
}

class Dog extends Animal{

}

public class Class_extends01 {
    public static void main(String[] args) {
        Dog myDog = new Dog();

        //부모에게 물려받은 변수와 메서드 사용
        myDog.name = "바둑이";
        System.out.println(myDog.name);
    }
}
