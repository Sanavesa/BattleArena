package arena.core;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import arena.ChasingAI;
import arena.CowardAI;

public class ArenaGUI extends AnchorPane
{
	private final Map map;
	private Player player1, player2;
	private PlayerAI playerAI1 = new CowardAI();
	private PlayerAI playerAI2 = new ChasingAI();
	private VisualEntity[][] visualEntities;
	private Timeline timeline;
	private Timeline timeline2;
	
	public ArenaGUI(int width, int height, int pixelSize)
	{
		super();
		map = new Map(width, height);
		
		GridPane gridPane = new GridPane();
		visualEntities = new VisualEntity[map.getWidth()][map.getHeight()];
		for(int y = 0; y < map.getHeight(); y++)
		{
			for(int x = 0; x < map.getWidth(); x++)
			{
				Entity entity = map.getEntity(x, y);
				VisualEntity visualEntity = new VisualEntity(entity, pixelSize);
				visualEntities[x][y] = visualEntity;
				gridPane.add(visualEntity, x, y);
			}
		}
		getChildren().add(gridPane);
		gridPane.setPadding(new Insets(50, 20, 20, 20));
		
		Button btnGenerate = new Button("Generate");
		Button btnPlay = new Button("Play");
		Button btnStop = new Button("Stop");
		
		btnGenerate.relocate(50, 0);
		btnGenerate.setOnAction(e ->
		{
			if(timeline != null)
			{
				timeline.stop();
				timeline = null;
			}
			
			if(timeline2 != null)
			{
				timeline2.stop();
				timeline2 = null;
			}
			
			map.clear();
			map.generateWalls(0.15);
			map.generateHealthPacks(0.03);
			addPlayers();
			redisplay();
			
			btnPlay.setDisable(false);
			btnStop.setDisable(true);
		});
		
		btnPlay.relocate(150, 0);
		btnPlay.setDisable(true);
		btnPlay.setOnAction(e ->
		{
			timeline = new Timeline();
			timeline2 = new Timeline();
			
			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.2), e2 ->
			{
				playerAI1.playTurn(player1);
				playerAI2.playTurn(player2);
				map.tick();
				redisplay();
				
				if(player1.isDead() || player2.isDead())
				{
					if(timeline != null)
					{
						timeline.stop();
						timeline = null;
					}
					
					if(timeline2 != null)
					{
						timeline2.stop();
						timeline2 = null;
					}
				}
			}));
			
			timeline2.getKeyFrames().add(new KeyFrame(Duration.seconds(2.0), e2 ->
			{
				map.advanceStorm();
				redisplay();
			}));
			
			
//			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e2 ->
//			{
//				playerAI1.playTurn(player1);
//				map.tick();
//				redisplay();
//			}));
//			
//			timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), e2 ->
//			{
//				playerAI2.playTurn(player2);
//				map.tick();
//				redisplay();
//			}));
			
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
			
			timeline2.setCycleCount(Timeline.INDEFINITE);
			timeline2.play();
			
			btnPlay.setDisable(true);
			btnStop.setDisable(false);
		});
		
		btnStop.relocate(250, 0);
		btnStop.setDisable(true);
		btnStop.setOnAction(e ->
		{
			if(timeline != null)
			{
				timeline.stop();
				timeline = null;
			}
			
			if(timeline2 != null)
			{
				timeline2.stop();
				timeline2 = null;
			}
			
			btnStop.setDisable(true);
			btnPlay.setDisable(false);
		});
		
		getChildren().addAll(btnGenerate, btnPlay, btnStop);
	}
	
	private void redisplay()
	{
		for(int y = 0; y < map.getHeight(); y++)
		{
			for(int x = 0; x < map.getWidth(); x++)
			{
				Entity entity = map.getEntity(x, y);
				visualEntities[x][y].setEntity(entity);
			}
		}
	}
	
	private void addPlayers()
	{
		int x = (int) (Math.random() * map.getWidth());
		int y = (int) (Math.random() * map.getHeight());
		while(!map.canAddPlayer(x, y))
		{
			x = (int) (Math.random() * map.getWidth());
			y = (int) (Math.random() * map.getHeight());	
		}
		player1 = map.addPlayer(x, y, "P1", Color.BLUE);
		player2 = map.addPlayer(map.getWidth() - x - 1, y, "P2", Color.VIOLET);
	}
}

class VisualEntity extends StackPane
{
	private Entity entity = null;
	private Rectangle rectangle;
	private Label label;
	
	public VisualEntity(Entity entity, int size)
	{
		setMinSize(size, size);
		setMaxSize(size, size);
		setPrefSize(size, size);
		rectangle = new Rectangle(size, size);
//		rectangle = new Rectangle(size-2, size-2);
		label = new Label("");
		label.setTextFill(Color.WHITE);
		
		getChildren().addAll(rectangle, label);
//		setStyle("-fx-border-color: black; -fx-border-width: 1;");
		setEntity(entity);
	}
	
	final void update()
	{
		Color color = Color.PURPLE;
		String text = "";
		
		if(entity == null)
		{
			color = Color.GRAY;
		}
		else
		{
			if(entity instanceof Player)
			{
				Player player = (Player)entity;
				color = player.getColor();
				text = player.getName() + "-" + String.valueOf(player.getHealth());
			}
			else if(entity instanceof Wall)
			{
				color = Color.BLACK;
			}
			else if(entity instanceof Storm)
			{
				color = Color.rgb(255, 255, 0, 0.25);
//				color = Color.ORANGE;
			}
			else if(entity instanceof Projectile)
			{
				color = ((Projectile) entity).color;
//				color = Color.YELLOW;
			}
			else if(entity instanceof Mine)
			{
				color = Color.RED;
			}
			else if(entity instanceof HealthPack)
			{
				color = Color.GREEN;
			}
		}
		
		rectangle.setFill(color);
		label.setText(text);
	}

	final Entity getEntity()
	{
		return entity;
	}

	final void setEntity(Entity entity)
	{
		this.entity = entity;
		update();
	}
}