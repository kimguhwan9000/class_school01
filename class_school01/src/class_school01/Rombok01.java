package class_school01;

import lombok.Data;

//1. @Data 하나면 Getter, Setter, toString, 기본생성자가 다 만들어집니다.
@Data
class Member {
 private String name;
 private int age;
}

public class Rombok01 {
 public static void main(String[] args) {
     // 2. 객체 생성 (기본 생성자 사용)
	 Member member = new Member();

     // 3. 롬복이 만든 Setter 호출
	 member.setName("홍길동");
	 member.setAge(25);

     // 4. 롬복이 만든 Getter 및 toString 호출
     System.out.println("이름 출력: " + member.getName());
     System.out.println("나이 출력: " + member.getAge());
     
     // 5. toString() 결과 확인: User(name=홍길동, age=25) 가 출력됩니다.
     System.out.println("전체 정보: " + member.toString());
 }
}
