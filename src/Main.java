
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Carte hex = new Carte(5);
		System.out.println(hex.relieComposantes(2, 2));
		hex.joueDeuxHumains();
	}
}
