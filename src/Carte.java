import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

// mail: benjamin.le-clere@etu.univ-nantes.fr

public class Carte {
	private ArrayList<ArrayList<Case>> carte_;
	private int taille_; // la taille de la carte
	private Joueur joueurCourant_; // le joueur qui joue actuellement
	private ArrayList<Joueur> joueurs_;

    public static final String CLEAR = "\033[2J\033[;H";

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
	    System.out.println(CLEAR);
	    String acc, acck, espace;
	    int i = 0;
	    acck = "        ";
	    espace = "";
		for(int k = 1; k < taille_ -1 ; ++k){
		    if (k<10)
			acck += " "+k+"  ";		    
		    else acck += ""+k+"  ";
		    }
	
		System.out.println(acck);
		// pour chaque ligne:
		for(ArrayList<Case> l : carte_){
			acc = "";
			// on ajoute le nombre d'espaces nécessaires pour afficher en décalé
			for(int j = 0; j < i; ++j)
			{
			    acc += "  ";
			}
			acc += ((i==0 || i == taille_-1)?" ":"")+((i>0 && i < taille_-1)?i:"")+((i>9)?"":" ")+" |";
			// pour chaque case de cette ligne
			for(Case c : l){
                          acc += c+"|";
			}
			i += 1;
			// on affiche la ligne
			System.out.println(acc+"  "+((i==1 || i == taille_)?"":(i-1)));
		}		
			for(int j = 0; j < i-1; ++j)
			{
			    espace += "  ";
			}
		System.out.println(espace+acck);
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
			if(c.getY() != 0)
			{
				// voisin haut gauche
				voisins.add(getCase(c.getX(), c.getY()-1));
				hg = true;
			}
			if(c.getY() != taille_-1)
			{
				// voisin bas gauche, bas droite
				voisins.add(getCase(c.getX()-1, c.getY()+1));
				voisins.add(getCase(c.getX(), c.getY()+1));
				bd = true;
			}
		}
		if(c.getX() != taille_-1)
		{
			// voisin droite
			voisins.add(getCase(c.getX()+1, c.getY()));
			if(c.getY() != 0)
			{
				// voisin haut droite haut gauche
				voisins.add(getCase(c.getX()+1, c.getY()-1));
				if(!hg)
				{
					voisins.add(getCase(c.getX(), c.getY()-1));
				}
			}
			if(c.getY() != taille_-1)
			{
				// voisin bas droite
				if(!bd)
				{
					voisins.add(getCase(c.getX(), c.getY()+1));
				}
			}
		}

		return voisins;
	}

	// retourne faux pour le moment
	public boolean finDuJeu()
	{
		return existeCheminCote();
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
	
	public boolean existeCheminCase(Case a, Case b)
	{
		return a.classe() == b.classe();
	}
	
	public boolean existeCheminCote()
	{
		return existeCheminCase(getCase(0, 1), getCase(taille_-1, 1)) ||
			   existeCheminCase(getCase(1, 0), getCase(1, taille_-1));
	}
	
	public boolean relieComposantes(int x, int y)
	{
		Case c = getCase(x, y);
		ArrayList<Case> voisins = getVoisins(c);
		
		for(Case voisin1 : voisins)
		{
			for(Case voisin2 : voisins)
			{
				if(voisin1 != voisin2)
				{
					if(voisin1.classe() != voisin2.classe() && 
					   voisin1.getCouleur() == voisin2.getCouleur() && 
					   voisin1.getCouleur() != 0 && 
					   voisin2.getCouleur() != 0)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public Couple<Integer, Stack<Case>> calculeDistanceChemin(int x1, int y1, int x2, int y2)
	{
		Case source = getCase(x1, y1);
		Case cible = getCase(x2, y2);
		
		ArrayList<Case> nonTraites = new ArrayList<Case>();
		
		Map<Case, Integer> dist = new HashMap<Case, Integer>();
		Map<Case, Case> prev = new HashMap<Case, Case>();
		
		for(int i = 0; i < taille_;  ++i)
		{
			for(int j = 0; j < taille_; ++j)
			{
				if(getCase(i, j).getCouleur() == source.getCouleur() || getCase(i, j).getCouleur() == 0)
				{
					dist.put(getCase(i, j), 50000);					
					nonTraites.add(getCase(i, j));
				}
			}
		}
		
		dist.put(source, 0);
		
		Case courant;
		boolean trouve = false;
		
		while(!nonTraites.isEmpty() && !trouve)
		{
			courant = nonTraites.get(0);
			for(Case c : nonTraites)
			{
				if(dist.get(c) < dist.get(courant))
					courant = c;
			}
			nonTraites.remove(courant);
			
			if(courant == cible)
			{
				trouve = true;
			}
			else
			{
				int alt;
				
				for(Case voisin : getVoisins(courant))
				{
					if(nonTraites.contains(voisin))
					{
						if(voisin.getCouleur() == 0)
						{
							alt = dist.get(courant) + 1;
						}
						else
						{
							alt = dist.get(courant);
						}
						if(alt < dist.get(voisin))
						{
							dist.put(voisin, alt);
							prev.put(voisin, courant);
						}
					}
				}
			}
		}
		
		int distance = dist.get(cible);
		
		Stack<Case> chemin = new Stack<Case>();
		
		while(prev.containsKey(cible))
		{
			chemin.add(cible);
			cible = prev.get(cible);
		}
		
		chemin.add(cible);
		
		return new Couple<Integer, Stack<Case>>(distance, chemin);
	}
	
	public int calculeDistance(int x1, int y1, int x2, int y2)
	{
		return calculeDistanceChemin(x1, y1, x2, y2).getPremier();
	}
	
	public String affichePath(int x1, int y1, int x2, int y2)
	{
		Stack<Case> chemin = calculeDistanceChemin(x1, y1, x2, y2).getSecond();
		
		String acc = "";
		for(Case c : chemin)
		{
			acc += c.affichePosition()+"\n";
		}
		return acc;
	}
	
	/**
	 * Méthode qui lance le jeu entre deux humains
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

				// si on ne peut pas ajouter
				if(!ajouter)
				{
					System.out.println("Vous ne pouvez pas ajouter un pion ici !");
				}
			}

			// on passe au joueur suivant
			System.out.println("Fin du tour\n");
			
			// on teste si le jeu est finit
			if(enCours = !finDuJeu())
			{
				joueurCourant_ = joueurCourant_.suivant();
			}

			// on teste si le jeu est fini
			ajouter = false;
		}
		
		// on affiche le gagnant et le plateau final
		System.out.println(joueurCourant_+" a gagné !");
		afficher();
		sc.close();
	}
	
	/**
	 * Méthode qui lance le jeu entre l'ordinateur et un humain
	 */
	public void joueOrdiHumains()
	{
		// on crée un scanner pour pouvoir lire ce qu'écris un joueur
		Scanner sc = new Scanner(System.in);
		int x;
		int y;

		// booleen qui nous dit si la partie est encore en cours
		boolean enCours = true;
		boolean ajouter = false;
		boolean ordiGagne = true;
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

				// si on ne peut pas ajouter
				if(!ajouter)
				{
					System.out.println("Vous ne pouvez pas ajouter un pion ici !");
				}
			}

			// on passe au joueur suivant
			System.out.println("Fin du tour\n");
			if(enCours = !finDuJeu())
			{
				System.out.println("Tour de l'ordinateur");
				evaluerPion();
			}
			else
			{
				ordiGagne = false;
			}
			
			// on teste si le jeu est finit
			enCours = !finDuJeu();

			ajouter = false;
		}
		
		// on affiche le gagnant et le plateau final
		if(ordiGagne)
		{
			System.out.println("Ordi à gagné !");
		}
		else
		{
			System.out.println(joueurCourant_+" a gagné !");
		}
		afficher();
		sc.close();
	}
	
	public void evaluerPion()
	{
		// distance pour que l'ordi gagne
		Couple<Integer, Stack<Case>> distanceCheminO = calculeDistanceChemin(1, 0, 1, taille_-1);
		
		// distance pour que le joueur gagne
		Couple<Integer, Stack<Case>> distanceCheminJ = calculeDistanceChemin(0, 1, taille_-1, 1);
		System.out.println(distanceCheminJ.getPremier());
		System.out.println(distanceCheminO.getPremier());
		if(distanceCheminJ.getPremier() <= taille_/2 && distanceCheminO.getPremier() > 1)
		{
			Case courante = distanceCheminJ.getSecond().firstElement();
			distanceCheminJ.getSecond().remove(0);
			while(!ajoutePion(courante.getX(), courante.getY(), 2))
			{
				courante = distanceCheminJ.getSecond().firstElement();
				distanceCheminJ.getSecond().remove(0);
			}
			System.out.println("1");
		}
		else
		{
			Case courante = distanceCheminO.getSecond().pop();
			while(!ajoutePion(courante.getX(), courante.getY(), 2))
			{
				courante = distanceCheminO.getSecond().pop();
			}
			System.out.println("2");
		}
	}
}
