import java.util.ArrayList;


public class Carte {
	private ArrayList<ArrayList<Case>> carte_;
	private int taille_;
	
	public Carte(int taille)
	{
		taille_ = taille;
		carte_ = new ArrayList<ArrayList<Case>>();
		
		for(int i = 0; i < taille; ++i)
		{
			ArrayList<Case> l = new ArrayList<Case>();
			for(int j = 0; j < taille; ++j)
			{
				l.add(new Case(j, i));
			}
			carte_.add(l);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
