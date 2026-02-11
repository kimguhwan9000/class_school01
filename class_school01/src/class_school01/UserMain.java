package class_school01;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
class User {
    private String name;
    private int age;
}

public class UserMain {
    public static void main(String[] args) {
        // @AllArgsConstructor로 생성된 생성자
        User user = new User("홍길동", 25);

        // @Data가 만들어준 Getter/Setter/toString
        user.setAge(30);
        System.out.println(user.getName()); // 출력: 홍길동
        System.out.println(user);           // 출력: User(name=홍길동, age=30)
    }
}
