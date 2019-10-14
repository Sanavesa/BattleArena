import arena.core.*;
import arena.example.*;

public class Main
{
	public static void main(String[] args)
	{
		// Start the game with RandomAI vs TemplateAI
		BattleArena.startArena(SimpleAI.class, SimpleAI.class);
	}
}