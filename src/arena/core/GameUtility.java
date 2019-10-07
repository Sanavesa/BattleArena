package arena.core;

import arena.core.GameState.EntityType;

public final class GameUtility
{
	private final GameState gameState;
	private final Node[][] nodes;
	
	GameUtility(GameState gameState)
	{
		this.gameState = gameState;
		nodes = new Node[gameState.getMapWidth()][gameState.getMapLength()];
		constructPathfindingNodes();
	}
	
	public final boolean isWalkable(int x, int y)
	{
		if(isOutOfBounds(x, y))
			return false;
		
		EntityType entityType = gameState.getEntityAt(x, y);
		if(entityType == EntityType.Empty)
			return true;
		if(entityType == EntityType.Storm || entityType == EntityType.Wall)
			return false;
		return true;
	}
	
	private final void constructPathfindingNodes()
	{
		for(int y = 0; y < gameState.getMapLength(); y++)
		{
			for(int x = 0; x < gameState.getMapWidth(); x++)
			{
				nodes[x][y] = new Node(x, y, isWalkable(x, y));
			}
		}
	}
	
	public final boolean isOutOfBounds(int x, int y)
	{
		return (x < 0 || y < 0 || x >= gameState.getMapWidth() || y >= gameState.getMapLength());
	}
	
	public final Node[] calculatePath(int startX, int startY, int destinationX, int destinationY)
	{
		if(isOutOfBounds(startX, startY))
			return new Node[0];

		if(isOutOfBounds(destinationX, destinationY))
			return new Node[0];
		
		Node startNode = nodes[startX][startY];
		Node destinationNode = nodes[destinationX][destinationY];
		Node[] path = AStar.calculatePath(nodes, startNode, destinationNode);
		return path;
	}
	
	public final boolean isReachable(int startX, int startY, int destinationX, int destinationY)
	{
		return calculatePath(startX, startY, destinationX, destinationY).length > 0;
	}
	
	public final boolean haveLineOfSight(int startX, int startY, int destinationX, int destinationY)
	{
		// Check same lane
		if(startX != destinationX && startY != destinationY)
			return false;
		
		int dx = destinationX - startX;
		int dy = destinationY - startY;
		
		if(dx != 0) // x changing
		{
			for(int x = startX; x != destinationX; x += Math.signum(dx))
			{
				if(gameState.isWall(x, startY) || gameState.isStorm(x, startY))
					return false;
			}
			
			return true;
		}
		else if(dy != 0) // y changing
		{
			for(int y = startY; y != destinationY; y += Math.signum(dy))
			{
				if(gameState.isWall(startX, y) || gameState.isStorm(startX, y))
					return false;
			}
			
			return true;
		}
		else // on top of each other
		{
			return false;
		}
	}
	
	public final Action moveTowards(int destinationX, int destinationY)
	{
		int startX = gameState.getMyPlayerX();
		int startY = gameState.getMyPlayerY();
		Node[] path = calculatePath(startX, startY, destinationX, destinationY);
		
		if(path.length > 0) // A path exists
		{
			int targetX = path[0].getX();
			int targetY = path[0].getY();
			
			if(targetX > startX)
				return Action.MoveRight;
			else if(targetX < startX)
				return Action.MoveLeft;
			else if(targetY > startY)
				return Action.MoveDown;
			else if(targetY < startY)
				return Action.MoveUp;
			else
				return Action.NoAction;
		}
		else // Unreachable
		{
			return Action.NoAction;
		}
	}
	
	public final int manhattanDistance(int startX, int startY, int destinationX, int destinationY)
	{
		int dx = startX - destinationX;
		int dy = startY - destinationY; 
		return Math.abs(dx) + Math.abs(dy);
	}
	
	public final double euclidianDistance(int startX, int startY, int destinationX, int destinationY)
	{
		int dx = startX - destinationX;
		int dy = startY - destinationY; 
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public final Vector2 findNearest(int x, int y, EntityType criterion)
	{
		Vector2 closest = new Vector2();
		int distance = Integer.MAX_VALUE;
		boolean anyMatches = false;
		for(int yy = 0; yy < gameState.getMapLength(); yy++)
		{
			for(int xx = 0; xx < gameState.getMapWidth(); xx++)
			{
				EntityType entityType = gameState.getEntityAt(xx, yy);
				if(entityType != criterion)
					continue;
				int calculatedDistance = manhattanDistance(x, y, xx, yy);
				if(calculatedDistance < distance)
				{
					closest.setX(xx);
					closest.setY(yy);
					distance = calculatedDistance;
					anyMatches = true;
				}
			}
		}
		return (anyMatches)? closest : null;
	}
	
	public final Vector2 findFurthest(int x, int y, EntityType criterion)
	{
		Vector2 furthest = new Vector2();
		int distance = Integer.MIN_VALUE;
		boolean anyMatches = false;
		for(int yy = 0; yy < gameState.getMapLength(); yy++)
		{
			for(int xx = 0; xx < gameState.getMapWidth(); xx++)
			{
				EntityType entityType = gameState.getEntityAt(xx, yy);
				if(entityType != criterion)
					continue;
				int calculatedDistance = manhattanDistance(x, y, xx, yy);
				if(calculatedDistance > distance)
				{
					furthest.setX(xx);
					furthest.setY(yy);
					distance = calculatedDistance;
					anyMatches = true;
				}
			}
		}
		return (anyMatches)? furthest : null;
	}
	
	public final boolean isWithinStorm(int x, int y, int stormSize)
	{
		double minX = Math.min(x + 1, Math.abs(x - gameState.getMapWidth()));
		double minY = Math.min(y + 1, Math.abs(y - gameState.getMapLength()));
		double distance = Math.min(minX, minY);
		return distance <= stormSize;
	}
}