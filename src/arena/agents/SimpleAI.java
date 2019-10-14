package arena.agents;

import arena.core.Action;
import arena.core.GameState;
import arena.core.GameUtility;
import arena.core.PlayerAI;

/**
 * An example of a simple AI that chases the opponent if close and has line of sight using A* pathfinding.
 * Also, it will shoot the opponent and randomly places mines whenever it can.
 * 
 * @author ERAU AI Club
 */
public class SimpleAI extends PlayerAI
{
	@Override
	protected Action getNextAction(GameState gameState, GameUtility gameUtility)
	{
		// Cache positions of player and opponent
		int playerX = gameState.getPlayerX();
		int playerY = gameState.getPlayerY();
		int opponentX = gameState.getOpponentX();
		int opponentY = gameState.getOpponentY();
		
		// Calculate the manhattan distance between them
		int distance = gameUtility.manhattanDistance(playerX, playerY, opponentX, opponentY);
		
		// If the opponent is close, and we have a line of sight...
		if(distance <= 5 && gameUtility.haveLineOfSight(playerX, playerY, opponentX, opponentY))
		{
			// ... shoot the opponent
			int dx = opponentX - playerX;
			int dy = opponentY - playerY;
			
			 // opponent is to the right of us
			if(dx > 0)
			{
				return Action.ShootRight;
			}
			// opponent is to the left of us
			else if(dx < 0)
			{
				return Action.ShootLeft;
			}
			 // opponent is below us (in video games, the y-axis is inverted)
			else if(dy > 0)
			{
				return Action.ShootDown;
			}
			 // opponent is above us
			else if(dy < 0)
			{
				return Action.ShootUp;
			}
			// opponent is on us (should never happen ;p )
			else
			{
				return Action.NoAction;
			}
		}
		// We dont have line of sight, and opponent is far ...
		else
		{
			// ... place a mine randomly if we can
			if(gameState.canPlaceMine())
			{
				return gameUtility.chooseRandomly(Action.PlaceMineDown, Action.PlaceMineLeft, Action.PlaceMineRight, Action.PlaceMineUp);
			}
			// ... move towards the opponent
			else
			{
				return gameUtility.moveTowards(opponentX, opponentY);
			}
		}
	}
}