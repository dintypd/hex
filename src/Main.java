
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Carte hex = new Carte(8);
		System.out.println(hex.calculeDistance(0, 0, 3, 3));
	}
}
