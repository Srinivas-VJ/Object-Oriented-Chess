package User;
import Pieces.Colour;

public class Player
{
	public Colour color;
	String name;
	
	public Player(Colour c, String name)
	{
		this.color = c;
		this.name = name;
	}
}