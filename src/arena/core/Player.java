package arena.core;

import javafx.scene.paint.Color;

final class Player extends Entity
{
	static final int HEALTH_MAX = 5;
	static final int HEALTH_START = 3;
	private int health = HEALTH_START;
	private final String name;
	private final Color color;
	private int shootCooldown = 0;
	private int placeMineCooldown = 0;
	private int xScaleMultiplier = 1;
	static final int MINE_COOLDOWN = 10;
	static final int SHOOT_COOLDOWN = 3;
	
	Player(Map map, int x, int y, String name, Color color)
	{
		super(map, x, y);
		this.name = name;
		this.color = color;
	}

	@Override
	void onCollided(Storm storm)
	{
		setHealth(0);
	}
	
	@Override
	final void onCollided(HealthPack healthPack)
	{
		setHealth(getHealth() + 1);
	}
	
	@Override
	final void onCollided(Mine mine)
	{
		setHealth(getHealth() - 1);
	}
	
	@Override
	final void onCollided(Projectile projectile)
	{
		// Ignore self-collision
		if(projectile.isOwner(this))
			return;
		
		setHealth(getHealth() - 1);
	}
	
	final boolean moveUp()
	{
		return move(getX(), getY() - 1);
	}
	
	final boolean moveDown()
	{
		return move(getX(), getY() + 1);
	}
	
	final boolean moveLeft()
	{
		xScaleMultiplier = -1;
		return move(getX() - 1, getY());
	}
	
	final boolean moveRight()
	{
		xScaleMultiplier = 1;
		return move(getX() + 1, getY());
	}
	
	private boolean move(int x, int y)
	{
		if(canMove(x, y))
		{
			setX(x);
			setY(y);
			return true;
		}
		
		return false;
	}
	
	private boolean canMove(int x, int y)
	{
		if(!map.isWithinBounds(x, y) || isDead())
			return false;
		
		Entity destinationEntity = map.getEntity(x, y);
		if(destinationEntity == null)
			return true;
		if(destinationEntity instanceof Wall || destinationEntity instanceof Player)
			return false;
		
		return true;
	}
	
	final boolean placeMineUp()
	{
		return placeMine(getX(), getY() - 1);
	}
	
	final boolean placeMineDown()
	{
		return placeMine(getX(), getY() + 1);
	}
	
	final boolean placeMineLeft()
	{
		xScaleMultiplier = -1;
		return placeMine(getX() - 1, getY());
	}
	
	final boolean placeMineRight()
	{
		xScaleMultiplier = 1;
		return placeMine(getX() + 1, getY());
	}
	
	private boolean placeMine(int x, int y)
	{
		if(isDead() || !canPlaceMine())
			return false;
		
		Mine mine = map.addMine(x, y);
		placeMineCooldown = MINE_COOLDOWN;
		return mine != null;
	}
	
	final boolean canPlaceMine()
	{
		return placeMineCooldown <= 0;
	}

	final boolean shootUp()
	{
		return shoot(0, -1);
	}
	
	final boolean shootDown()
	{
		return shoot(0, 1);
	}
	
	final boolean shootLeft()
	{
		xScaleMultiplier = -1;
		return shoot(-1, 0);
	}
	
	final boolean shootRight()
	{
		xScaleMultiplier = 1;
		return shoot(1, 0);
	}
	
	private boolean shoot(int speedX, int speedY)
	{
		if(isDead() || !canShoot())
			return false;
		
		Projectile projectile = map.addProjectile(getX(), getY(), speedX, speedY, this);
		shootCooldown = SHOOT_COOLDOWN;
		return projectile != null;
	}
	
	final boolean canShoot()
	{
		return shootCooldown <= 0;
	}

	final int getHealth()
	{
		return health;
	}

	final boolean isDead()
	{
		return health <= 0;
	}
	
	final void setHealth(int health)
	{
		this.health = Math.max(0, Math.min(HEALTH_MAX, health));
		
		if(isDead())
		{
			destroy();
		}
	}
	
	final String getName()
	{
		return name;
	}
	
	final Color getColor()
	{
		return color;
	}
	
	final int getXScaleMultiplier()
	{
		return xScaleMultiplier;
	}

	@Override
	public final String toString()
	{
		return getName();
	}
	
	final int getPlaceMineCooldown()
	{
		return placeMineCooldown;
	}
	
	final int getShootCooldown()
	{
		return shootCooldown;
	}
	
	@Override
	void onUpdate()
	{
		if(shootCooldown > 0)
		{
			shootCooldown--;
		}
		
		if(placeMineCooldown > 0)
		{
			placeMineCooldown--;
		}
	}
}