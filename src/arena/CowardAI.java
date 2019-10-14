package arena;

import arena.core.Action;
import arena.core.GameState;
import arena.core.GameUtility;
import arena.core.PlayerAI;

public class CowardAI extends PlayerAI
{
	@Override
	protected Action getNextAction(GameState gameState, GameUtility gameUtility)
	{
		// Find other player
		int otherX = -1;
		int otherY = -1;
		boolean found = false;
		for(int y = 0; y < gameState.getMapHeight() && !found; y++)
		{
			for(int x = 0; x < gameState.getMapWidth() && !found; x++)
			{
				if(gameState.isHostilePlayer(x, y))
				{
					found = true;
					otherX = x;
					otherY = y;
				}
			}
		}
		
		// Calculate distance
		int myX = gameState.getPlayerX();
		int myY = gameState.getPlayerY();
		double distance = Math.sqrt(Math.pow(myX - otherX, 2) + Math.pow(myY - otherY, 2));
		boolean sameLane = ((myX == otherX) || (myY == otherY));
		
		// Put mine towards them if near, move away if far
		if(distance < 10 && sameLane)
		{
			if(otherX > myX && !gameState.isWall(myX+1, myY))
				return Action.PlaceMineRight;
			else if(otherX < myX && !gameState.isWall(myX-1, myY))
				return Action.PlaceMineLeft;
			else if(otherY > myY && !gameState.isWall(myX, myY+1))
				return Action.PlaceMineDown;
			else if(otherY < myY && !gameState.isWall(myX, myY-1))
				return Action.PlaceMineUp;
			else
				return Action.NoAction;
		}
		else
		{
			if(otherX > myX && !gameState.isWall(myX+1, myY))
				return Action.MoveLeft;
			else if(otherX < myX && !gameState.isWall(myX-1, myY))
				return Action.MoveRight;
			else if(otherY > myY && !gameState.isWall(myX, myY+1))
				return Action.MoveUp;
			else if(otherY < myY && !gameState.isWall(myX, myY-1))
				return Action.MoveDown;
			else
				return Action.NoAction;
		}
	}
}