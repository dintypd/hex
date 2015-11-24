
public class Couple <P, S> {
	private P premier_;
	private S second_;
	
	public Couple(P premier, S second)
	{
		premier_ = premier;
		second_ = second;
	}
	
	public P getPremier()
	{
		return premier_;
	}
	
	public S getSecond()
	{
		return second_;
	}
}
