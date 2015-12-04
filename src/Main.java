import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Carte hex = new Carte(15);
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Choisissez :");
		System.out.println(" 1:Jouer contre un humain");
		System.out.println(" 2:Jouer contre un ordinateur");
		
		int choix = hex.demanderValeur(0, 3, sc); 
		if(choix == 1)
		{
			hex.joueDeuxHumains();
		}
		else
		{
			hex.joueOrdiHumains();
		}
	}
}
