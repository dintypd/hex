import java.util.ArrayList;


public class Case {
	private int x_;
	private int y_;
	private int couleur_;
	private Case representant_;
	private ArrayList<Case> fils_;
	
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
		representant_ = this;
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
	
	public String toString()
	{
		return couleur_+"";
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
	
}