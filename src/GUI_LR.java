import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class GUI_LR extends Application{


  /*--- GLOBAL VARIABLES ---*/
  Stage window;
  private final int LANDING_PAGE_WIDTH = 300;
  private final int LANDING_PAGE_HEIGHT = 180;
  /*--- GLOBAL VARIABLES ---*/

  public static void main (String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    window = primaryStage;
    window.setTitle("Velkommen");

  /*REGISTER PAGE*/
    BorderPane register_layout = new BorderPane();
    Scene register_page = new Scene(register_layout, LANDING_PAGE_WIDTH, LANDING_PAGE_HEIGHT);
  /*REGISTER PAGE*/

  /*LANDING_PAGE*/
    /*--- HEADER ---*/
    HBox header = new HBox();
    header.setId("header");
    Label title = new Label("Velkommen!");
    title.setId("title");
    header.getChildren().add(title);
    /*--- HEADER ---*/

    /*--- MAIN ---*/
    VBox main = new VBox();
    main.setId("main");

    //Username
    HBox username = new HBox();
    Label lbl_username = new Label("Brukernavn: ");
    lbl_username.getStyleClass().add("inp_lbl");
    TextField inp_username = new TextField();
    username.getChildren().addAll(lbl_username, inp_username);

    //Password
    HBox password = new HBox();
    Label lbl_password = new Label("Passord: ");
    lbl_password.getStyleClass().add("inp_lbl");
    TextField inp_password = new TextField();
    password.getChildren().addAll(lbl_password, inp_password);

    //Buttons
    HBox buttons = new HBox();
    Button btn_login = new Button("Logg inn");
    btn_login.setOnAction(evt -> Login.Authenticate(inp_username.getText(), inp_password.getText()));
    btn_login.getStyleClass().add("button");

    Button btn_register = new Button("Registrer deg");
    btn_register.getStyleClass().add("button");
    btn_register.setOnAction(evt -> window.setScene(register_page));
    buttons.getChildren().addAll(btn_login, btn_register);
    buttons.setId("buttons");

    main.getChildren().addAll(username, password, buttons);
    /*--- MAIN ---*/

    /*--- FOOTER ---*/
      HBox footer = new HBox();
    /*--- FOOTER ---*/

    /*--- BorderPane ---*/
    BorderPane landing_layout = new BorderPane();
    /*--- BorderPane ---*/

    /*--- LAYOUT ---*/
    landing_layout.setTop(header);
    landing_layout.setCenter(main);
    landing_layout.setBottom(footer);
    /*--- LAYOUT ---*/

    /*--- SCENE ---*/
    Scene landing_page = new Scene(landing_layout, LANDING_PAGE_WIDTH, LANDING_PAGE_HEIGHT);
    landing_page.getStylesheets().add("./Assets/css/style.css");
    /*--- SCENE ---*/
  /*LANDING_PAGE*/

    /*--- Window ---*/
    window.setScene(landing_page);
    window.show();
    /*--- Window ---*/
  }
}
