package class_school01;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
1. import lombok.Data;
"가장 강력한 종합 선물 세트"입니다.
이 한 줄을 가져와서 클래스 위에 @Data를 붙이면, 롬복이 컴파일할 때 아래 코드들을 자동으로 눈 안 보이게 만들어 줍니다.

Getter/Setter: getName(), setName(), getAge(), setAge()를 자동으로 만듭니다. (데이터를 읽고 쓰는 통로)
toString(): System.out.println(m1); 했을 때 주소값이 아니라 Member2(name=홍길동, age=25)라고 예쁘게 출력되게 만듭니다.
equals & hashCode: 두 객체의 값이 같은지 비교하는 복잡한 로직을 자동으로 짜줍니다.

2. import lombok.AllArgsConstructor;
"모든 재료를 한 번에 넣는 요리법"입니다.
클래스에 있는 모든 변수(name, age)를 인자로 받는 생성자를 만듭니다.

하는 일: 원래라면 여러분이 직접 쳐야 했을 아래 코드를 대신 생성합니다.
public Member2(String name, int age) {
    this.name = name;
    this.age = age;
}
덕분에 가능해진 일: Member2 m1 = new Member2("홍길동", 25); 처럼 한 줄로 객체를 만들고 데이터를 채울 수 있습니다.    
3. import lombok.NoArgsConstructor;
"빈 그릇을 준비하는 요리법"입니다.
파라미터가 아무것도 없는 기본 생성자를 만듭니다.

하는 일: 아무 내용 없는 기본 형태를 만듭니다.
public Member2() { }
왜 필요한가?

Member2 m2 = new Member2(); 처럼 일단 빈 객체를 만들고 나중에 setName()으로 값을 채우고 싶을 때 필요합니다.
나중에 배울 DB 연동 라이브러리(JPA, MyBatis)나 웹 데이터 변환 도구들은 내부적으로 이 기본 생성자를 써서 객체를 먼저 만들기 때문에 선택이 아닌 필수인 경우가 많습니다.    
    
*/

// 1. @Data: Getter, Setter, toString, equals, hashCode 자동 생성
// 2. @AllArgsConstructor: 모든 필드(name, age)를 받는 생성자
// 3. @NoArgsConstructor: 파라미터가 없는 '기본 생성자' (DB나 프레임워크 사용 시 필수)
@Data
@AllArgsConstructor
@NoArgsConstructor
class Member2 {
	private String name;
	private int age;
	private String job;
}

public class Rombok02 {
	public static void main(String[] args) {
		// 방법 A: @AllArgsConstructor를 이용해 생성과 동시에 데이터 주입 (가장 편함)
		Member2 m1 = new Member2("홍길동", 25, "정의의 용사");
		
		

		// 방법 B: @NoArgsConstructor와 Setter를 이용해 단계별로 데이터 주입
		Member2 m2 = new Member2();
		m2.setName("이순신");
		m2.setAge(40);
		m2.setJob("장군");

		// 결과 출력 (@Data가 만들어준 toString 덕분에 객체 내용이 바로 보입니다)
		System.out.println("--- 회원 정보 출력 ---");
		System.out.println("회원 1: " + m1); // 출력: Member(name=홍길동, age=25)
		System.out.println("회원 2: " + m2); // 출력: Member(name=이순신, age=40)

		// Getter 활용 예시
		if (m1.getAge() < m2.getAge()) {
			System.out.println(m2.getName() + "님이 형님입니다.");
		}
	}
}
