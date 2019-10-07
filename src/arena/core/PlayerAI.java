package arena.core;

public abstract class PlayerAI
{
	protected abstract Action getNextAction(GameState gameState, GameUtility gameUtility);
	
	final void playTurn(Player player)
	{
		if(player.isDead() || player.isDestroyed())
			return;
		
		// TODO: Do time limitations
		GameState gameState = new GameState(player);
		GameUtility gameUtility = new GameUtility(gameState);
		Action action = getNextAction(gameState, gameUtility);
//		System.out.println(player + " " + action);
		
		switch(action)
		{
			case NoAction:
				break;
				
			case MoveUp:
				player.moveUp();
				break;
				
			case MoveDown:
				player.moveDown();
				break;
				
			case MoveLeft:
				player.moveLeft();
				break;
				
			case MoveRight:
				player.moveRight();
				break;
				
			case ShootUp:
				player.shootUp();
				break;
				
			case ShootDown:
				player.shootDown();
				break;
				
			case ShootLeft:
				player.shootLeft();
				break;
				
			case ShootRight:
				player.shootRight();
				break;
				
			case PlaceMineUp:
				player.placeMineUp();
				break;
				
			case PlaceMineDown:
				player.placeMineDown();
				break;
				
			case PlaceMineLeft:
				player.placeMineLeft();
				break;
				
			case PlaceMineRight:
				player.placeMineRight();
				break;
				
			default:
				throw new IllegalStateException("Unknown action: " + action);
		}
	}
}