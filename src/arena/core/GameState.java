package arena.core;

public final class GameState
{
	public enum EntityType
	{
		Empty,
		Wall,
		Player,
		Mine,
		Projectile,
		HealthPack,
		Storm
	}
	
	private final Player player;
	private final Map map;
	private final EntityType[][] visualMap;
	
	GameState(Player player)
	{
		this.player = player;
		map = player.map;
		visualMap = new EntityType[map.getWidth()][map.getHeight()];
		constructVisualMap();
	}
	
	EntityType getEntityType(int x, int y)
	{
		Entity entity = map.getEntity(x, y);
		if(entity == null)
			return EntityType.Empty;
		
		if(entity instanceof Wall)
			return EntityType.Wall;
		
		if(entity instanceof Player)
			return EntityType.Player;
		
		if(entity instanceof Mine)
			return EntityType.Mine;
		
		if(entity instanceof HealthPack)
			return EntityType.HealthPack;
		
		if(entity instanceof Projectile)
			return EntityType.Projectile;
		
		if(entity instanceof Storm)
			return EntityType.Storm;
		
		return EntityType.Empty;
	}
	
	private final void constructVisualMap()
	{
		for(int y = 0; y < map.getHeight(); y++)
		{
			for(int x = 0; x < map.getWidth(); x++)
			{
				visualMap[x][y] = getEntityType(x, y); 
			}
		}
	}
	
	public final int getMapWidth()
	{
		return map.getWidth();
	}
	
	public final int getMapLength()
	{
		return map.getHeight();
	}
	
	public final int getPlayerHealth()
	{
		return player.getHealth();
	}
	
	public final int getPlayerMaxHealth()
	{
		return Player.HEALTH_MAX;
	}
	
	public final int getPlayerX()
	{
		return player.getX();
	}
	
	public final int getPlayerY()
	{
		return player.getY();
	}
	
	public final boolean isEmpty(int x, int y)
	{
		return map.isEmpty(x, y);
	}
	
	public final boolean isWall(int x, int y)
	{
		return map.getEntity(x, y) instanceof Wall;
	}
	
	public final boolean isStorm(int x, int y)
	{
		return map.getEntity(x, y) instanceof Storm;
	}
	
	public final boolean isProjectile(int x, int y)
	{
		return map.getEntity(x, y) instanceof Projectile;
	}
	
	public final boolean isHostileProjectile(int x, int y)
	{
		if(!isProjectile(x, y))
			return false;
		
		Projectile projectile = (Projectile) map.getEntity(x, y);
		return !projectile.isOwner(player);
	}
	
	public final boolean isFriendlyProjectile(int x, int y)
	{
		if(!isProjectile(x, y))
			return false;
		
		Projectile projectile = (Projectile) map.getEntity(x, y);
		return projectile.isOwner(player);
	}
	
	public final boolean isMine(int x, int y)
	{
		return map.getEntity(x, y) instanceof Mine;
	}
	
	public final boolean isHealthPack(int x, int y)
	{
		return map.getEntity(x, y) instanceof HealthPack;
	}
	
	public final boolean isHostilePlayer(int x, int y)
	{
		Entity entity = map.getEntity(x, y);
		if(entity == null || !(entity instanceof Player))
			return false;
		
		return !player.equals(entity);
	}
}