import java.util.ArrayList;


public class Case implements Comparable<Case>{
	private int x_;
	private int y_;
	private int couleur_;
	private Case representant_;
	private ArrayList<Case> fils_;
        public static final String DEFAUT = "\u001B[0m";
        public static final String ROUGE = "\u001B[31m";
        public static final String BLEU = "\u001B[34m";

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
		String descendants = "";
		for(Case fils : fils_)
		{
			descendants += fils.affichePosition()+";";
		}
		return "["+x_+";"+y_+"] ("+descendants+")";
	}

	/**
	 * Affichage d'une case selon sa couleur
	 * @return une string correspondant à la couleur du pion
	 */
	public String toString(){
          if (couleur_==1){
            return BLEU+couleur_+DEFAUT;}
          else if (couleur_==2){
              return ROUGE+couleur_+DEFAUT;}
          else return couleur_+"";
	}

	/**
	 * Renvoie le nombre de descendants d'une case
	 * @return le nombre de fils d'une case et des fils de ses fils, comptant la case elle-même
	 */
	public int nombreDeDescendants(){
		int nbfils = 1;
		for(Case f : fils_){
			nbfils += f.nombreDeDescendants();
		}
		return nbfils + fils_.size();
	}

	/**
	 * Accesseur du représentant de la case, utilise la compression des chemins
	 * @return une case représentante de la classe qui contient "this"
	 */
	public Case classe()
	{
		if(representant_ == null)
		{
			return this;
		}
		else
		{
			return representant_ = representant_.classe();
		}
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

	public void addFils(Case c)
	{
		fils_.add(c);
	}

	/**
	 * Méthode qui réalise l'union des classes de "this" et voisin en utilisant l'union par rang
	 * @param voisin une case membre de la classe que l'on veut unir à celle de "this"
	 */
	public void union(Case voisin)
	{
		Case repThis = classe();
		Case repVoisin = voisin.classe();
		if(repThis != repVoisin)
		{
			if(repThis.nombreDeDescendants() > repVoisin.nombreDeDescendants())
			{
				repVoisin.setClasse(repThis);
			}
			else
			{
				repThis.setClasse(repVoisin);
			}
		}
	}

	/**
	 * Affichage via sa position de chaque case de la classe de "this"
	 */
	public void afficheComposante()
	{
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

	@Override
	public int compareTo(Case o) {
		if(x_ > o.getX())
		{
			return 1;
		}
		else if(x_ == o.getX())
		{
			if(y_ > o.getY())
			{
				return 1;
			}
			else if(y_ < o.getY())
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return -1;
		}
	}
}
