package frontend.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BoB_Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception{
    Parent root = FXMLLoader.load(getClass().getResource("../fxml/bob_landing.fxml"));
    primaryStage.setTitle("FestivalArrangør  | Booking sjef");
    Scene scene = new Scene(root, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
