import java.util.ArrayList;
import java.util.Scanner;

// mail: benjamin.le-clere@etu.univ-nantes.fr

public class Carte {
	private ArrayList<ArrayList<Case>> carte_;
	private int taille_; // la taille de la carte
	private Joueur joueurCourant_; // le joueur qui joue actuellement
	private ArrayList<Joueur> joueurs_;

	/**
	 * Constructeur qui initialise le plateau
	 * @param taille la taille du plateau, qui est carré
	 */
	public Carte(int taille)
	{
		taille_ = taille;
		carte_ = new ArrayList<ArrayList<Case>>();

		// on initialise le plateau de jeu: aucune case n'a de couleur
		for(int i = 0; i < taille; ++i)
		{
			ArrayList<Case> l = new ArrayList<Case>();
			for(int j = 0; j < taille; ++j)
			{
				l.add(new Case(j, i));
			}
			carte_.add(l);
		}

		// on initialise les cases des coté, chaque coté à la même couleur que son coté opposé
		for(int i = 0; i < taille; ++i)
		{
			for(int j = 0; j < taille; ++j)
			{
				if(i == 0 || i == taille_-1)
				{
					if(j > 0 && j < taille_-1)
					{
						ajoutePion(i, j, 1);
					}
				}
				else if(i > 0 && i < taille_-1)
				{
					if(j == 0 || j == taille_-1)
					{
						ajoutePion(i, j, 2);
					}
				}
			}
		}

		// initialisation des coins
		carte_.get(0).get(0).setCouleur(9);
		carte_.get(0).get(taille_-1).setCouleur(9);
		carte_.get(taille_-1).get(0).setCouleur(9);
		carte_.get(taille_-1).get(taille_-1).setCouleur(9);

		// on initialise la liste des joueurs
		joueurs_ = new ArrayList<Joueur>();
		Joueur j1 = new Joueur(1, "j1");
		Joueur j2 = new Joueur(2, "j2");
		j1.setSuivant(j2);
		j2.setSuivant(j1);
		joueurs_.add(j1);
		joueurs_.add(j2);
		joueurCourant_ = j1;
	}

	/**
	 * Accesseur de la carte
	 * @return la carte
	 */
	public ArrayList<ArrayList<Case>> getCarte(){
		return carte_;
	}

	/**
	 * Accesseur de la taille de la carte
	 * @return la taille
	 */
	public int getTaille(){
		return taille_;
	}

	/**
	 * Méthode d'affichage du plateau de jeu
	 */
	public void afficher(){
		String acc;
		int i = 0;
		// pour chaque ligne:
		for(ArrayList<Case> l : carte_){
			acc = "";
			// on ajoute le nombre d'espaces nécessaires pour afficher en décalé
			for(int j = 0; j < i; ++j)
			{
				acc += " ";
			}
			acc += "|";
			// pour chaque case de cette ligne
			for(Case c : l){
                          acc += c+"|";
			}
			i += 1;
			// on affiche la ligne
			System.out.println(acc);
		}
	}

	/**
	 * Ajoute si c'est possible un pion du joueur courant aux coordonnées données
	 * @param x la coordonnée x du pion
	 * @param y la coordonnée y du pion
	 * @return un boolean qui dit si le pion a pu être placé
	 */
	public boolean ajoutePion(int x, int y, int couleur)
	{
		Case c = getCase(x, y);
		if(c.getCouleur() != 0)
		{
			return false;
		}
		c.setCouleur(couleur);

		ArrayList<Case> voisins = getVoisins(c);
		for(Case voisin : voisins)
		{
			if(voisin.getCouleur() == couleur)
			{
				c.union(voisin);
			}
		}
		return true;
	}

	/**
	 * Accesseur des voisins d'une case
	 * @param c une case
	 * @return un tableau contenant les cases voisines de c
	 */
	public ArrayList<Case> getVoisins(Case c)
	{
		ArrayList<Case> voisins = new ArrayList<Case>();
		boolean hg = false;
		boolean bd = false;
		if(c.getX() != 0)
		{
			// voisin gauche
			voisins.add(getCase(c.getX()-1, c.getY()));
			//voisins.add(carte_.get(c.getX()-1).get(c.getY()));
			if(c.getY() != 0)
			{
				// voisin haut gauche
				voisins.add(getCase(c.getX(), c.getY()-1));
				//voisins.add(carte_.get(c.getX()).get(c.getY()-1));
				hg = true;
			}
			if(c.getY() != taille_-1)
			{
				// voisin bas gauche, bas droite
				voisins.add(getCase(c.getX()-1, c.getY()+1));
				voisins.add(getCase(c.getX(), c.getY()+1));
				//voisins.add(carte_.get(c.getX()-1).get(c.getY()+1));
				//voisins.add(carte_.get(c.getX()).get(c.getY()+1));
				bd = true;
			}
		}
		if(c.getX() != taille_-1)
		{
			// voisin droite
			voisins.add(getCase(c.getX()+1, c.getY()));
			//voisins.add(carte_.get(c.getX()+1).get(c.getY()));
			if(c.getY() != 0)
			{
				// voisin haut droite haut gauche
				voisins.add(getCase(c.getX()+1, c.getY()-1));
				//voisins.add(carte_.get(c.getX()+1).get(c.getY()-1));
				if(!hg)
				{
					voisins.add(getCase(c.getX(), c.getY()-1));
					//voisins.add(carte_.get(c.getX()).get(c.getY()-1));
				}
			}
			if(c.getY() != taille_-1)
			{
				// voisin bas droite
				if(!bd)
				{
					voisins.add(getCase(c.getX(), c.getY()+1));
					//voisins.add(carte_.get(c.getX()).get(c.getY()+1));
				}
			}
		}

		return voisins;
	}

	/**
	 * Méthode qui lance le jeu
	 */
	public void joueDeuxHumains()
	{
		// on crée un scanner pour pouvoir lire ce qu'écris un joueur
		Scanner sc = new Scanner(System.in);
		int x;
		int y;

		// booleen qui nous dit si la partie est encore en cours
		boolean enCours = true;
		boolean ajouter = false;
		while(enCours)
		{
			System.out.println("Tour de "+joueurCourant_);
			System.out.println("Ou voulez vous placer votre pion ?");
			afficher();

			while(!ajouter)
			{

				// récupération de x
				System.out.println("X ?");
				x = sc.nextInt();
				while(x < 0 || x >= taille_)
				{
					System.out.println("X doit être compris entre 0 et "+(taille_-1)+"\nX ?");
					x = sc.nextInt();
				}

				// récupération de y
				System.out.println("Y ?");
				y = sc.nextInt();
				while(y < 0 || y >= taille_)
				{
					System.out.println("Y doit être compris entre 0 et "+(taille_-1)+"\nY ?");
					y = sc.nextInt();
				}

				// on ajoute le pion en (x, y)
				ajouter = ajoutePion(x, y, joueurCourant_.getCouleur());
				// pour le debug, on affiche la composante
				getCase(x, y).afficheComposante();

				// si on ne peut pas ajouter
				if(!ajouter)
				{
					System.out.println("Vous ne pouvez pas ajouter un pion ici !");
				}
			}

			// on passe au joueur suivant
			System.out.println("Fin du tour\n");
			joueurCourant_ = joueurCourant_.suivant();

			// on teste si le jeu est fini
			enCours = !finDuJeu();
			ajouter = false;
		}
	}

	// retourne faux pour le moment
	public boolean finDuJeu()
	{
		return false;
	}

	/**
	 * Accesseur de la case aux coordonnées données
	 * @param x la première coordonnée
	 * @param y la seconde coordonnée
	 * @return la case de coordonnées (x, y)
	 */
	public Case getCase(int x, int y)
	{
		return carte_.get(y).get(x);
	}
}
