package arena.core;

import arena.example.RandomAI;
import javafx.scene.paint.Color;

final class Game
{
	private Map map;
	private Player player1, player2;
	private PlayerAI agent1, agent2;
	private int round;
	
	public static final int ROUND_PER_STORM_ADVANCE = 10;
	public static final double MAP_WALL_DENSITY = 0.15;
	public static final double MAP_HEALTH_PACK_DENSITY = 0.02;
	
	public Game(int mapWidth, int mapHeight, Class<? extends PlayerAI> p1Class, Class<? extends PlayerAI> p2Class)
	{
		createAgents(p1Class, p2Class);
		map = new Map(mapWidth, mapHeight);
	}
	
	private final void createAgents(Class<? extends PlayerAI> p1Class, Class<? extends PlayerAI> p2Class)
	{
		//Creating AI from the specified classes
		try
		{
			agent1 = p1Class.getDeclaredConstructor().newInstance();
		}
		catch (Exception e)
		{
			agent1 = new RandomAI();
		}
		
		try
		{
			agent2 = p2Class.getDeclaredConstructor().newInstance();
		}
		catch (Exception e)
		{
			agent2 = new RandomAI();
		}
	}
	
	public final void generateMap()
	{
		round = 0;
		map.clear();
		map.generateWalls(MAP_WALL_DENSITY);
		map.generateHealthPacks(MAP_HEALTH_PACK_DENSITY);
		generatePlayers();
	}
	
	private final void generatePlayers()
	{
		// add players
		int x = (int) (Math.random() * map.getWidth());
		int y = (int) (Math.random() * map.getHeight());
		while(!map.canAddPlayer(x, y))
		{
			x = (int) (Math.random() * map.getWidth());
			y = (int) (Math.random() * map.getHeight());	
		}
		player1 = map.addPlayer(x, y, "Player1", Color.BLUE);
		player2 = map.addPlayer(map.getWidth() - x - 1, y, "Player2", Color.RED);
	}
	
	public final boolean isGameOver()
	{
		return player1.isDead() || player2.isDead();
	}
	
	public final void tick()
	{
		round++;
		agent1.playRound(this, player1);
		agent2.playRound(this, player2);
		map.tick();
		if(round % ROUND_PER_STORM_ADVANCE == 0)
		{
			map.advanceStorm();
		}
	}
	
	public final int getRoundsTillNextStormAdvance()
	{
		return ROUND_PER_STORM_ADVANCE - (round % ROUND_PER_STORM_ADVANCE);
	}

	public final Map getMap()
	{
		return map;
	}

	public final void setMap(Map map)
	{
		this.map = map;
	}

	public final int getRound()
	{
		return round;
	}

	public final void setRound(int round)
	{
		this.round = round;
	}

	public final Player getPlayer1()
	{
		return player1;
	}

	public final void setPlayer1(Player player1)
	{
		this.player1 = player1;
	}

	public final Player getPlayer2()
	{
		return player2;
	}

	public final void setPlayer2(Player player2)
	{
		this.player2 = player2;
	}

	public final PlayerAI getAgent1()
	{
		return agent1;
	}

	public final void setAgent1(PlayerAI agent1)
	{
		this.agent1 = agent1;
	}

	public final PlayerAI getAgent2()
	{
		return agent2;
	}

	public final void setAgent2(PlayerAI agent2)
	{
		this.agent2 = agent2;
	}
}