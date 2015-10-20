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
	}
}