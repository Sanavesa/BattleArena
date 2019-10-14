package arena.example;

import arena.core.*;

public class TemplateAI extends PlayerAI
{

	@Override
	protected Action getNextAction(GameState gameState, GameUtility gameUtility)
	{
		return Action.NoAction;
	}

}
