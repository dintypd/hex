import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * Classe Carte
 * 
 * Cette classe s'occupe de la gestion du plateau de jeu. Elle contient entre autre
 * la liste des joueurs, ainsi que la liste des cases
 */
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
	 * Les blocs AFFICHAGE COORDONEES ne servent qu'à afficher les coordonnées x et y en haut, bas, droite, gauche du plateau
	 */
	public void afficher(){
	    String acc, acck, espace;
	    int i = 0;
	    acck = "        ";
	    espace = "";
	    
	    // AFFICHAGE COORDONNEES
		for(int k = 1; k < taille_ -1 ; ++k){
		    if (k<10)
			acck += " "+k+"  ";		    
		    else acck += ""+k+"  ";
		    }
		
		System.out.println(acck);
		//////////////////////////
		
		// pour chaque ligne:
		for(ArrayList<Case> l : carte_){
			acc = "";
			
			// on ajoute le nombre d'espaces nécessaires pour afficher en décalé
			for(int j = 0; j < i; ++j)
			{
			    acc += "  ";
			}
			
			acc +=  ((i==0 || i == taille_-1)?" ":"")+
					((i>0 && i < taille_-1)?i:"")+
					((i>9)?"":" ")+" |";
			// pour chaque case de cette ligne
			for(Case c : l){
                          acc += c+"|";
			}
			i += 1;
			// on affiche la ligne
			System.out.println(acc+"  "+((i==1 || i == taille_)?"":(i-1)));
			/////////////////////////////
		}	
		
		// AFFICHAGE COORDONNEES	
			for(int j = 0; j < i-1; ++j)
			{
			    espace += "  ";
			}
		System.out.println(espace+acck);
		/////////////////////////
	}

	/**
	 * Ajoute si c'est possible un pion du joueur courant aux coordonnées données
	 * @param x la coordonnée x du pion
	 * @param y la coordonnée y du pion
	 * @return un boolean qui dit si le pion a pu être placé
	 */
	public boolean ajoutePion(int x, int y, int couleur)
	{
		// on récupére la case ciblée
		Case c = getCase(x, y);
		
		// si celle ci est déjà associée à un joueur, on ne peux pas l'ajouter
		if(c.getCouleur() != 0)
		{
			return false;
		}
		
		// sinon on lui assigne la couleur du joueur
		c.setCouleur(couleur);

		// et on réalise l'union avec tous ses voisin de même couleur
		ArrayList<Case> voisins = getVoisins(c);
		for(Case voisin : voisins)
		{
			if(voisin.getCouleur() == couleur)
			{
				if(voisin.classe() != c.classe())
				{
					c.union(voisin);
				}
			}
		}
		
		// enfin on retourne que l'ajout s'est bien passé
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

	/**
	 * Méthode qui teste si l'un des joueurs à gagné
	 * @return vrai si la partie est finie, faux sinon
	 */
	public boolean finDuJeu()
	{
		// le moyen rapide de savoir si un des joueurs à gagner est 
		// de savoir si il existe un chemin entre deux cotés du plateau
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
	
	/**
	 * Méthode qui teste si il existe un chemin entre deux cases
	 * @return vrai s'il existe un chemin entre a et b
	 */
	public boolean existeCheminCase(Case a, Case b)
	{
		// il faut juste tester si a et b sont dans la même classe
		return a.classe() == b.classe();
	}
	
	/**
	 * Méthode qui teste si il existe un chemin entre deux cotés
	 */
	public boolean existeCheminCote()
	{
		// il faut juste tester si il existe un chemin entre les composantes droite et gauche, ou haut et bas
		return existeCheminCase(getCase(0, 1), getCase(taille_-1, 1)) ||
			   existeCheminCase(getCase(1, 0), getCase(1, taille_-1));
	}
	
	/**
	 * Méthode qui tester si l'ajout d'un pion en x, y relie deux composantes
	 * @param x la coordonnée x
	 * @param y la coordonnee y
	 * @return vrai si le pion(x, y) relie deux composantes
	 */
		public boolean relieComposantes(int x, int y)
	{
		// on récupére la case ciblée
		Case c = getCase(x, y);
		
		// on récupére la liste de ses voisins
		ArrayList<Case> voisins = getVoisins(c);
		
		// pour chaque voisins, on cherche s'il existe un autre voisin de la même couleur
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
	
	/**
	 * Méthode qui retourne un couple contenant:
	 * 	- un entier, correspondant à la longueur du plus court chemin entre deux cases dont les coordonnées sont en paramètre
	 * 	- une pile de cases, correspondante a ce chemin
	 * Cette méthode implémente l'algorithme de Dijkstra
	 */
	public Couple<Integer, Stack<Case>> calculeDistanceChemin(int x1, int y1, int x2, int y2)
	{
		// on récupère les deux cases qui nous intéressent
		Case source = getCase(x1, y1);
		Case cible = getCase(x2, y2);
		
		// on initialise les structures qui nous serviront 
		ArrayList<Case> nonTraites = new ArrayList<Case>();	
		Map<Case, Integer> dist = new HashMap<Case, Integer>();
		Map<Case, Case> prev = new HashMap<Case, Case>();
		
		// on remplitces structures
		for(int i = 0; i < taille_;  ++i)
		{
			for(int j = 0; j < taille_; ++j)
			{
				// on n'ajoute dans le "graphe" seulement les cases de même couleur que la cible et la source, ou de couleur neutre
				if(getCase(i, j).getCouleur() == source.getCouleur() || getCase(i, j).getCouleur() == 0)
				{
					dist.put(getCase(i, j), taille_*taille_);					
					nonTraites.add(getCase(i, j));
				}
			}
		}
		
		dist.put(source, 0);
		
		Case courant;
		boolean trouve = false;
		
		// tant que toutes les cases n'ont pas été traitées
		while(!nonTraites.isEmpty() && !trouve)
		{
			// on recherche la case dont la valeur associée est minimum dans la map dist
			courant = nonTraites.get(0);
			for(Case c : nonTraites)
			{
				if(dist.get(c) < dist.get(courant))
					courant = c;
			}
			// on supprime cette case du tableau des cases non traitées
			nonTraites.remove(courant);
			
			// si on est sur la case cible, alors il s'agit de la dernière itération
			if(courant == cible)
			{
				trouve = true;
			}
			else
			{
				int alt;
				
				// pour chacun de ses voisins, on cherche la distance de voisin a courant:
				//  -si voisin est de la même couleur, la distance vaut 0, 
				//  -sinon 1
				// bien entendu, on ne traite pas les voisins de couleur différente
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
		
		// enfin, on récupère le chemin
		Stack<Case> chemin = new Stack<Case>();
		
		while(prev.containsKey(cible))
		{
			chemin.add(cible);
			cible = prev.get(cible);
		}
		
		chemin.add(cible);
		
		// on construit le couple et on le retourne
		return new Couple<Integer, Stack<Case>>(distance, chemin);
	}
	
	/**
	 * Méthode qui retourne la distance entre les cases dont les coordonnées sont en paramètres
	 */
	public int calculeDistance(int x1, int y1, int x2, int y2)
	{
		return calculeDistanceChemin(x1, y1, x2, y2).getPremier();
	}
	
	/**
	 * Méthode qui affiche le chemin, deuxième composante du couple retourné par calculeDistanceChemin
	 */
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
		System.out.println(CLEAR);
		// on crée un scanner pour pouvoir lire ce qu'écris un joueur
		Scanner sc = new Scanner(System.in);
		
		// pour récupèrer le choix du joueur
		int choix;

		// booleen qui nous dit si la partie est encore en cours
		boolean enCours = true;
		boolean ajouter = false;
		boolean ordiGagne = true;
		boolean quit = false;
		
		// on lance la boucle du jeu
		while(enCours && !quit)
		{
			// affichage de la carte
			afficher();
			
			// demander commande
			// - 1 -> jouer
			// - 2 -> quitter
			// - 3 -> calculeDistance
			// - 4 -> afficheComposante
			System.out.println("Tour de "+joueurCourant_);
			System.out.println("Choisissez:");
			System.out.println(" 1: Jouer");
			System.out.println(" 2: Quitter");
			System.out.println(" 3: CalculeDistance");
			System.out.println(" 4: afficheComposante");
			
			choix = demanderValeur(0, 5, sc);
			if(choix == 1)
			{
				System.out.println("Ou voulez vous placer votre pion ?");
				
				int x, y;
			
				// tant que le joueur n'a pas ajouté de pion
				while(!ajouter)
				{
					// récupération de x
					System.out.println("X ?");
					x = demanderValeur(0, taille_-1, sc);

					// récupération de y
					System.out.println("Y ?");
					y = demanderValeur(0, taille_-1, sc);

					// on ajoute le pion en (x, y)
					ajouter = ajoutePion(x, y, joueurCourant_.getCouleur());

					// si on ne peut pas ajouter
					if(!ajouter)
					{
						System.out.println("Vous ne pouvez pas ajouter un pion ici !");
					}
				}
				System.out.println(CLEAR);
			
				// on teste si le jeu est finit
				if(enCours = !finDuJeu())
				{
					joueurCourant_ = joueurCourant_.suivant();
				}
			
				// on réinitialise le booleen ajouter a faux
				ajouter = false;
			}
			else if(choix == 2)
			{
				quit = true;
			}
			else if(choix == 3)
			{
				int x1, y1, x2, y2;
				
				// récupération de x1
				System.out.println("X1 ?");
				x1 = demanderValeur(-1, taille_, sc);

				// récupération de y1
				System.out.println("Y1 ?");
				y1 = demanderValeur(-1, taille_, sc);
				
				// récupération de x2
				System.out.println("X2 ?");
				x2 = demanderValeur(-1, taille_, sc);

				// récupération de y2
				System.out.println("Y2 ?");
				y2 = demanderValeur(-1, taille_, sc);
				
				System.out.println(CLEAR);
				System.out.println("Distance : "+calculeDistanceChemin(x1, y1, x2, y2).getPremier());
			}
			else
			{
				int x, y;
				// récupération de x
				System.out.println("X ?");
				x = demanderValeur(-1, taille_, sc);

				// récupération de y
				System.out.println("Y ?");
				y = demanderValeur(-1, taille_, sc);
				
				System.out.println(CLEAR);
				System.out.print("Composante : ");
				getCase(x, y).afficheComposante();
			}
		}
		
		
		if(!quit)
		{
			System.out.println(CLEAR);
			// on affiche le gagnant et le plateau final
			afficher();
			if(ordiGagne)
			{
				System.out.println("Ordi à gagné !");
			}
			else
			{
				System.out.println(joueurCourant_+" a gagné !");
			}
		}
		else
		{
			System.out.println(CLEAR);
			System.out.println(joueurCourant_+" a quitté la partie.");
		}
		
		sc.close();
	}
	
	/**
	 * Méthode qui lance le jeu entre l'ordinateur et un humain
	 */
	public void joueOrdiHumains()
	{
		System.out.println(CLEAR);
		// on crée un scanner pour pouvoir lire ce qu'écris un joueur
		Scanner sc = new Scanner(System.in);
		
		// pour récupèrer le choix du joueur
		int choix;

		// booleen qui nous dit si la partie est encore en cours
		boolean enCours = true;
		boolean ajouter = false;
		boolean ordiGagne = true;
		boolean quit = false;
		
		// on lance la boucle du jeu
		while(enCours && !quit)
		{
			// affichage de la carte
			afficher();
			
			// demander commande
			// - 1 -> jouer
			// - 2 -> quitter
			// - 3 -> calculeDistance
			// - 4 -> afficheComposante
			System.out.println("Tour de "+joueurCourant_);
			System.out.println("Choisissez:");
			System.out.println(" 1: Jouer");
			System.out.println(" 2: Quitter");
			System.out.println(" 3: CalculeDistance");
			System.out.println(" 4: afficheComposante");
			
			choix = demanderValeur(0, 5, sc);
			if(choix == 1)
			{
				System.out.println("Ou voulez vous placer votre pion ?");
				
				int x, y;
			
				// tant que le joueur n'a pas ajouté de pion
				while(!ajouter)
				{
					// récupération de x
					System.out.println("X ?");
					x = demanderValeur(0, taille_-1, sc);

					// récupération de y
					System.out.println("Y ?");
					y = demanderValeur(0, taille_-1, sc);

					// on ajoute le pion en (x, y)
					ajouter = ajoutePion(x, y, joueurCourant_.getCouleur());

					// si on ne peut pas ajouter
					if(!ajouter)
					{
						System.out.println("Vous ne pouvez pas ajouter un pion ici !");
					}
				}
				System.out.println(CLEAR);
				
				// si le joueur n'a pas gagné, c'est a l'ordi de jouer
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
			
				// on réinitialise le booleen ajouter a faux
				ajouter = false;
			}
			else if(choix == 2)
			{
				quit = true;
			}
			else if(choix == 3)
			{
				int x1, y1, x2, y2;
				
				// récupération de x1
				System.out.println("X1 ?");
				x1 = demanderValeur(-1, taille_, sc);

				// récupération de y1
				System.out.println("Y1 ?");
				y1 = demanderValeur(-1, taille_, sc);
				
				// récupération de x2
				System.out.println("X2 ?");
				x2 = demanderValeur(-1, taille_, sc);

				// récupération de y2
				System.out.println("Y2 ?");
				y2 = demanderValeur(-1, taille_, sc);
				
				System.out.println(CLEAR);
				System.out.println("Distance : "+calculeDistanceChemin(x1, y1, x2, y2).getPremier());
			}
			else
			{
				int x, y;
				// récupération de x
				System.out.println("X ?");
				x = demanderValeur(-1, taille_, sc);

				// récupération de y
				System.out.println("Y ?");
				y = demanderValeur(-1, taille_, sc);
				
				System.out.println(CLEAR);
				System.out.print("Composante : ");
				getCase(x, y).afficheComposante();
			}
		}
		
		
		if(!quit)
		{
			System.out.println(CLEAR);
			// on affiche le gagnant et le plateau final
			afficher();
			if(ordiGagne)
			{
				System.out.println("Ordi à gagné !");
			}
			else
			{
				System.out.println(joueurCourant_+" a gagné !");
			}
		}
		else
		{
			System.out.println(CLEAR);
			System.out.println(joueurCourant_+" a quitté la partie.");
		}
		
		sc.close();
	}
	
	public int demanderValeur(int min, int max, Scanner sc)
	{
		int value = sc.nextInt();
		
		while(value <= min || value >= max)
		{
			System.out.println("La valeur doit être comprise entre "+min+" et "+max+" (exclus)\n");
			value = sc.nextInt();
		}
		
		return value;
	}
	
	/**
	 * Méthode qui choisit quel pion va devoir jouer l'ordinateur
	 */
	public void evaluerPion()
	{
		// distance pour que l'ordi gagne
		Couple<Integer, Stack<Case>> distanceCheminO = calculeDistanceChemin(1, 0, 1, taille_-1);
		
		// distance pour que le joueur gagne
		Couple<Integer, Stack<Case>> distanceCheminJ = calculeDistanceChemin(0, 1, taille_-1, 1);
		
		// en premier on test s'il faut avoir un comportement de défense
		if(distanceCheminJ.getPremier() <= taille_/2 && distanceCheminO.getPremier() > 1)
		{
			Case courante = distanceCheminJ.getSecond().firstElement();
			distanceCheminJ.getSecond().remove(0);
			while(!ajoutePion(courante.getX(), courante.getY(), 2))
			{
				courante = distanceCheminJ.getSecond().firstElement();
				distanceCheminJ.getSecond().remove(0);
			}
			System.out.println("L'ordinateur à joué en ("+courante.getX()+" ; "+courante.getY()+")");
		}
		else // sinon on attaque
		{
			Case courante = distanceCheminO.getSecond().pop();
			while(!ajoutePion(courante.getX(), courante.getY(), 2))
			{
				courante = distanceCheminO.getSecond().pop();
			}
			System.out.println("L'ordinateur à joué en ("+courante.getX()+" ; "+courante.getY()+")");
		}
	}
}
