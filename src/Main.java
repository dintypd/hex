
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Case c00 = new Case(0, 0);
		Case c10 = new Case(1, 0);
		Case c20 = new Case(2, 0);
		Case c01 = new Case(0, 1);
		Case c11 = new Case(1, 1);
		Case c21 = new Case(2, 1);
		Case c02 = new Case(0, 2);
		Case c12 = new Case(1, 2);
		Case c22 = new Case(2, 2);
		System.out.println(c11.estVoisinDe(c00));
		System.out.println(c11.estVoisinDe(c01));
		System.out.println(c11.estVoisinDe(c02));
		System.out.println(c11.estVoisinDe(c12));
		System.out.println(c11.estVoisinDe(c22));
	}

}
