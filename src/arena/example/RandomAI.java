package arena.example;

import java.util.Random;

import arena.core.Action;
import arena.core.GameState;
import arena.core.GameUtility;
import arena.core.PlayerAI;

public class RandomAI extends PlayerAI
{
	private final Random rand;
	private final Action[] actions;
	
	public RandomAI()
	{
		rand = new Random();
		actions = Action.values();
	}
	
	@Override
	protected Action getNextAction(GameState gameState, GameUtility gameUtility)
	{
		return actions[rand.nextInt(actions.length-8)];
	}
}