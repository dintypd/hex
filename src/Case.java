import java.util.ArrayList;


public class Case {
	private int x_;
	private int y_;
	private int couleur_;
	private Case representant_;
	private ArrayList<Case> fils_;
	
	public Case(int x, int y)
	{
		x_ = x;
		y_ = y;
		couleur_ = 0;
		fils_ = new ArrayList<Case>();
	}
	
	public int getX()
	{
		return x_;
	}
	
	public int getY()
	{
		return y_;
	}
	
	public boolean estVoisinDe(Case c)
	{
		boolean voisin = false;
		
		if(Math.abs(x_-c.getX()) <= 1 && Math.abs(y_-c.getY()) <= 1)
		{
			if(!((x_-c.getX() == 1 && y_-c.getY() == 1 ) ||
			   (x_-c.getX() == -1 && x_-c.getY() == 1)))
			{
				voisin = true;
			}
		}
		
		return voisin;
	}
	

}