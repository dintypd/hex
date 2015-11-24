
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Carte hex = new Carte(15);
	
		hex.ajoutePion(1, 1, 2);
		hex.ajoutePion(2, 3, 2);
		hex.ajoutePion(4, 2, 2);
		
		hex.ajoutePion(1, 3, 1);
		hex.ajoutePion(2, 2, 1);
		hex.ajoutePion(3, 2, 1);
		hex.ajoutePion(3, 3, 1);
		hex.ajoutePion(1, 4, 1);
		
		hex.afficher();
		
		System.out.println(hex.affichePath(1, 1, 2, 3));
	}
}
