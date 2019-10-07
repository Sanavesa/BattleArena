package arena.core;

public final class Vector2
{
	private int x;
	private int y;
	
	public Vector2()
	{
		this(0, 0);
	}
	
	public Vector2(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 other)
	{
		x = other.x;
		y = other.y;
	}

	public final int getX()
	{
		return x;
	}

	public final void setX(int x)
	{
		this.x = x;
	}

	public final int getY()
	{
		return y;
	}

	public final void setY(int y)
	{
		this.y = y;
	}

	@Override
	public final String toString()
	{
		return "Position [x=" + x + ", y=" + y + "]";
	}
}