package class_school01;


public class Space_main {
	public static void main(String[] args) {
		Planet planet = new Planet();
		
		String planetName = planet.getName();
		int planetX = planet.getX();
		int planetY = planet.getY();
		
		
		System.out.println("가져온 행성 이름 : " + planetName);
		System.out.println("가져온 행성의 좌표는 : " + planetX);
		System.out.println("가져온 행성의 좌표는 : " + planetY);
	}
}
