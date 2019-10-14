import arena.agents.*;
import arena.core.*;

public class Main
{
	public static void main(String[] args)
	{
		// Start the game with RandomAI vs TemplateAI
		BattleArena.startArena(SimpleAI.class, SimpleAI.class);
	}
}