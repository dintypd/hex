
public class Joueur {
	private int _couleur;
	private String _nom;
	private Joueur _suivant; // représente le joueur suivant dans l'ordre du jeu, celui qui joueras après
	
	/**
	 * Constructeur
	 * @param couleur la couleur du joueur
	 */
	public Joueur(int couleur, String nom)
	{
		_couleur = couleur;
		_nom = nom;
	}
	
	/**
	 * Méthode qui met à jour le joueur qui jouera après
	 * @param j le joueur qui jouera après "this"
	 */
	public void setSuivant(Joueur j)
	{
		_suivant = j;
	}
	
	/**
	 * Accesseur du joueur qui joueras après "this"
	 * @return le joueur en question
	 */
	public Joueur suivant()
	{
		return _suivant;
	}
	
	/**
	 * Accesseur de la couleur du joueur
	 * @return sa couleur
	 */
	public int getCouleur()
	{
		return _couleur;
	}
	
	/**
	 * Affichage du joueur: cela affiche son nom
	 */
	public String toString()
	{
		return _nom;
	}
}
