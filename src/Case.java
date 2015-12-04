import java.util.ArrayList;

/**
 * Classe Case
 * 
 * Cette classe s'occupe de la gestion des cases, notamment de la structure de Classe-Union
 */
public class Case{
	// ses coordonnées
	private int x_;
	private int y_;
	// sa couleur, 0 par défaut
	private int couleur_;
	// le représentant de sa classe
	private Case representant_;
	// ses descendants
	private ArrayList<Case> fils_;
	// nombre de descendants
	private int nbDescendants_;
		// couleurs utiles seulement à l'affichage
        public static final String DEFAUT = "\u001B[0m";
        public static final String ROUGE = "\u001B[31m";
        public static final String VERT = "\u001B[32m";

	/**
	 * Constructeur d'une case, la couleur est mise à 0 par défault
	 * @param x la première coordonnée de la case
	 * @param y la seconde coordonnée de la case
	 */
	public Case(int x, int y)
	{
		x_ = x;
		y_ = y;
		couleur_ = 0;
		fils_ = new ArrayList<Case>();
		representant_ = null;
		nbDescendants_ = 0;
	}

	/**
	 * Accesseur de la première coordonnée de la case
	 * @return la coordonnée x de la case
	 */
	public int getX()
	{
		return x_;
	}

	/**
	 * Accesseur de la seconde coordonnée de la case
	 * @return la coordonnée y de la case
	 */
	public int getY()
	{
		return y_;
	}

	/**
	 * Accesseur de la couleur de la case
	 * @return la couleur de la case
	 */
	public int getCouleur()
	{
		return couleur_;
	}

	/**
	 * Modificateur de la couleur d'une case
	 * @param couleur la couleur voulue pour la case
	 */
	public void setCouleur(int couleur)
	{
		couleur_ = couleur;
	}

	/**
	 * Test de voisinage de deux cases
	 * @param c la case dont on veut savoir si elle est voisine de "this"
	 * @return vrai si la case en paramètre et "this" sont voisines
	 */
	public boolean estVoisinDe(Case c)
	{
		boolean voisin = false;

		if(Math.abs(x_-c.getX()) <= 1 && Math.abs(y_-c.getY()) <= 1)
		{
			if(!((x_-c.getX() == 1 && y_-c.getY() == 1 ) ||
			   (x_-c.getX() == -1 && x_-c.getY() == -1)))
			{
				voisin = true;
			}
		}

		return voisin;
	}

	/**
	 * Affichage d'une case via sa position
	 * @return une string du type [x;y]
	 */
	public String affichePosition()
	{
		return "["+x_+";"+y_+"]";
	}

	/**
	 * Affichage d'une case selon sa couleur
	 * @return une string correspondant à la couleur du pion
	 */
	public String toString(){
	    if (couleur_ ==0){
		return "_._";
	    }
	    else if (couleur_==1){
		return "_"+VERT+couleur_+DEFAUT+"_";}
	    else if (couleur_==2){
		return "_"+ROUGE+couleur_+DEFAUT+"_";}
	    else return " "+couleur_+" ";
	}

	/**
	 * Renvoie le nombre de descendants d'une case
	 * @return le nombre de fils d'une case et des fils de ses fils, comptant la case elle-même
	 */
	public int getNbDescendants(){
		return nbDescendants_;
	}

	/**
	 * Modificateur du représentant de la case
	 * @param c une case, nouveau représentant de "this"
	 */
	public void setClasse(Case c)
	{
		representant_ = c;
		c.addFils(this);
	}

	/**
	 * Ajoute un fils à la case c
	 */
	public void addFils(Case c)
	{
		fils_.add(c);
		nbDescendants_ += 1 + c.getNbDescendants();
	}
	
	/**
	 * Accesseur du représentant de la case, utilise la compression des chemins
	 * @return une case représentante de la classe qui contient "this"
	 */
	public Case classe()
	{
		// si le représentant d'une case est a null, alors cette case est représentant
		if(representant_ == null)
		{
			return this;
		}
		else
		{
			// on réalise la compression des chemins
			return representant_ = representant_.classe();
		}
	}

	/**
	 * Méthode qui réalise l'union des classes de "this" et voisin en utilisant l'union par rang
	 * @param voisin une case membre de la classe que l'on veut unir à celle de "this"
	 */
	public void union(Case voisin)
	{
		Case repThis = classe();
		Case repVoisin = voisin.classe();
		
		// on réalise l'union par rang
		if(repThis.getNbDescendants() > repVoisin.getNbDescendants())
		{
			repVoisin.setClasse(repThis);
		}
		else
		{
			repThis.setClasse(repVoisin);
		}
	}

	/**
	 * Affichage via sa position de chaque case de la classe de "this"
	 */
	public void afficheComposante()
	{
		// on affiche la composante du représentant de cette classe
		if(representant_ == null)
		{
			System.out.println(parcoursProfondeur());
		}
		else
		{
			representant_.afficheComposante();
		}
	}

	/**
	 * Parcours en profondeur de la liste de tous les descendants de "this"
	 * @return une string contenant l'affichage des position de chaque descendant de "this"
	 */
	public String parcoursProfondeur()
	{
		String acc = affichePosition();

		for(Case fils : fils_)
		{
			acc += fils.parcoursProfondeur();
		}

		return acc;
	}
	
	/**
	 * Retourne la distance entre deux cases "à vol d'oiseau"
	 */
	public int distance(Case c) {
		int temp = Math.max(
			     Math.abs(c.getY() - this.y_),     
			     Math.abs(c.getX() - this.x_));
		
		return Math.max(temp,
			     Math.abs((c.getX() - c.getY())*-1 - (this.x_ - this.y_)*-1));
			
	}
}
