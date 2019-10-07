package arena;

import arena.core.ArenaGUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BattleArena extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception
	{
		ArenaGUI pane = new ArenaGUI(20, 20, 32);
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.show();
	}
}
