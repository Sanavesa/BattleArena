package arena.core;

import javafx.scene.paint.Color;

final class Projectile extends Entity
{
	private final int speedX;
	private final int speedY;
	private final Player player;
	public final Color color;
	
	Projectile(Map map, int x, int y, int speedX, int speedY, Player player)
	{
		super(map, x, y);
		this.player = player;
		this.speedX = speedX;
		this.speedY = speedY;
		color = player.getColor().interpolate(Color.WHITE, 0.5);
	}
	
	boolean isOwner(Player player)
	{
		return this.player.equals(player);
	}
	
	@Override
	protected void onUpdate()
	{
		int oldX = getX();
		int oldY = getY();
		setX(getX() + speedX);
		setY(getY() + speedY);
		
		// Didnt move -> destroy
		if(oldX == getX() && oldY == getY())
		{
			destroy();
		}
	}
	
	@Override
	protected void onCollided(HealthPack healthPack)
	{
		player.setHealth(player.getHealth() + 1);
		destroy();
	}
	
	@Override
	protected void onCollided(Mine mine)
	{
		destroy();
	}
	
	@Override
	protected void onCollided(Projectile projectile)
	{
		destroy();
	}
	
	@Override
	protected void onCollided(Player player)
	{
		// Ignore collision with owner player
		if(player == this.player)
			return;
		
		destroy();
	}
	
	@Override
	protected void onCollided(Wall wall)
	{
		destroy();
	}
	
	@Override
	void onCollided(Storm storm)
	{
		destroy();
	}
}