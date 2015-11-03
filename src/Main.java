
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Carte hex = new Carte(15);
		System.out.println(hex.getVoisins(hex.getCarte().get(1).get(1)));
	}

}
