package arena.example;

import arena.core.Action;
import arena.core.GameState;
import arena.core.GameState.EntityType;
import arena.core.GameUtility;
import arena.core.PlayerAI;
import arena.core.Vector2;

public class ChasingAI extends PlayerAI
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
		
		if(found)
		{
			int myX = gameState.getPlayerX();
			int myY = gameState.getPlayerY();
			int distance = gameUtility.manhattanDistance(myX, myY, otherX, otherY);
			
			if(distance <= 5 && gameUtility.haveLineOfSight(myX, myY, otherX, otherY))
			{
				// Shoot the other player
				int dx = otherX - myX;
				int dy = otherY - myY;
				if(dx > 0)
					return Action.ShootRight;
				else if(dx < 0)
					return Action.ShootLeft;
				else if(dy > 0)
					return Action.ShootDown;
				else if(dy < 0)
					return Action.ShootUp;
				else
					return Action.NoAction;
			}
			else
			{
				// Run towards other player
				return gameUtility.moveTowards(otherX, otherY);
			}
		}
		else
		{
			int myX = gameState.getPlayerX();
			int myY = gameState.getPlayerY();
			Vector2 closestHealthPack = gameUtility.findNearest(myX, myY, EntityType.HealthPack);
			if(closestHealthPack != null && gameState.getPlayerHealth() < gameState.getPlayerMaxHealth())
			{
				return gameUtility.moveTowards(closestHealthPack.getX(), closestHealthPack.getY());
			}
			else
			{
				// Run towards center of map
				int centerX = gameState.getMapWidth()/2;
				int centerY = gameState.getMapHeight()/2;
				return gameUtility.moveTowards(centerX, centerY);
			}
		}
	}
}