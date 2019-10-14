package arena.core;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

final class ArenaGUI extends BorderPane
{
	
	private Button btnPlayNPause;
	private Button btnGenerate;
	private ImageView imageViewPlayNPause;
	private ImageView imageViewGenerate;
	
	private GridPane tilesGridPane;
	
	private final ArenaInfoPanel arenaInfoPanel;
	
	private VBox top;
	
	private Tile[][] tiles;
	private Timeline gameLoopTimeline;
	private ImageDatabase imageDatabase;
	private Game game;
	public static final double SECS_PER_TICK = 0.25;
	
 	public ArenaGUI(Stage stage, int mapWidth, int mapHeight, int pixelSize, Class<? extends PlayerAI> p1Class, Class<? extends PlayerAI> p2Class)
	{
		super();
		
		imageDatabase = new ImageDatabase();
		game = new Game(mapWidth, mapHeight, p1Class, p2Class);
		
		arenaInfoPanel = new ArenaInfoPanel(stage, game, imageDatabase);
		
		top = new VBox(10);
		setTop(top);
		
		initializeTiles(pixelSize);
		initializeControlButtons();
		
		top.getChildren().add(arenaInfoPanel);
		
		generate();
	}
 	
	private void redisplay()
	{
		for(int y = 0; y < game.getMap().getHeight(); y++)
		{
			for(int x = 0; x < game.getMap().getWidth(); x++)
			{
				Entity entity = game.getMap().getEntity(x, y);
				tiles[x][y].setEntity(entity);
			}
		}
		
		arenaInfoPanel.onRedisplay();
	}
	
	private void initializeControlButtons()
	{
		// Generate
		btnGenerate = new Button();
		btnGenerate.setTooltip(new Tooltip("Generates a new map."));
		imageViewGenerate = new ImageView(imageDatabase.replay);
		imageViewGenerate.setFitWidth(32);
		imageViewGenerate.setFitHeight(32);
		btnGenerate.setGraphic(imageViewGenerate);
		btnGenerate.relocate(50, 0);
		btnGenerate.setOnAction(e -> onGenerateClicked());
		
		// Play & Pause
		btnPlayNPause = new Button();
		btnPlayNPause.setTooltip(new Tooltip("Plays or pauses the simulation."));
		imageViewPlayNPause = new ImageView(imageDatabase.play);
		imageViewPlayNPause.setFitWidth(32);
		imageViewPlayNPause.setFitHeight(32);
		btnPlayNPause.setGraphic(imageViewPlayNPause);
		btnPlayNPause.relocate(150, 0);
		btnPlayNPause.setOnAction(e -> onPlayNPauseClicked());
		
		HBox hBox = new HBox(20, createSeparator(), btnGenerate, createSeparator(), btnPlayNPause, createSeparator());
		hBox.setPadding(new Insets(10, 0, 10, 0));
		top.getChildren().add(hBox);
	}
	
	private Separator createSeparator()
	{
		Separator separator = new Separator(Orientation.HORIZONTAL);
		separator.setVisible(false);
		HBox.setHgrow(separator, Priority.ALWAYS);
		return separator;
	}
	
	private void initializeTiles(int pixelSize)
	{
		tilesGridPane = new GridPane();
		Map map = game.getMap();
		
		tiles = new Tile[map.getWidth()][map.getHeight()];
		for(int y = 0; y < map.getHeight(); y++)
		{
			for(int x = 0; x < map.getWidth(); x++)
			{
				Entity entity = map.getEntity(x, y);
				Tile tile = new Tile(imageDatabase, entity, pixelSize);
				tiles[x][y] = tile;
				tilesGridPane.add(tile, x, y);
			}
		}
		
		setBottom(tilesGridPane);
	}
	
	private void onGenerateClicked()
	{
		generate();
	}
	
	private void onPlayNPauseClicked()
	{
		if(isRunning()) // Running, so pause
		{
			stopGameLoop();
		}
		else // Not running, so start
		{
			startGameLoop();
		}
	}
	
	private void stopGameLoop()
	{
		if(gameLoopTimeline != null)
		{
			gameLoopTimeline.stop();
			gameLoopTimeline = null;
		}
		
		imageViewPlayNPause.setImage(imageDatabase.play);
	}
	
	private void startGameLoop()
	{
		gameLoopTimeline = new Timeline();
		
		gameLoopTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(SECS_PER_TICK), e ->
		{
			game.tick();
			redisplay();
			
			if(game.isGameOver())
			{
				stopGameLoop();
			}
		}));
		
		gameLoopTimeline.setCycleCount(Timeline.INDEFINITE);
		gameLoopTimeline.play();
		
		imageViewPlayNPause.setImage(imageDatabase.pause);
	}
	
	private void generate()
	{
		stopGameLoop();
		game.generateMap();
		game.setRound(0);
		redisplay();
	}
	
	private boolean isRunning()
	{
		if(gameLoopTimeline == null)
			return false;
		
		return gameLoopTimeline.getStatus() == Status.RUNNING;
	}
}