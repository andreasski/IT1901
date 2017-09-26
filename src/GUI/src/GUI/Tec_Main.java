package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tec_Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception{
    Parent root = FXMLLoader.load(getClass().getResource("../fxml/tec_landing.fxml"));
    primaryStage.setTitle("FestivalArrangør  | Tekniker");
    Scene scene = new Scene(root, 800, 600);
    scene.getStylesheets().add("./css/tec.css");
    primaryStage.setScene(scene);
    primaryStage.show();
  }



  public static void main(String[] args) {
    launch(args);
  }
}