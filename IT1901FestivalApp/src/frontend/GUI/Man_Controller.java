package frontend.GUI;

/**
 * Tec_Controller handles user interaction with the GUI.
 * It also generates fxml dynamically based on external data.
 * @author  Andreas Skifjeld
 * @version 1.0
 * @since   26.09.2017
 */

import backend.Organizer;
import backend.Technician;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Man_Controller implements Initializable{

  private backend.Manager manager;

  @FXML
  private Label instructionBoxLbl;

  @FXML
  private VBox container;

  VBox contents = new VBox();

  /**
   * The method creates fxml content based on external information that is collected with the method getWork(int id)
   * getWork(int id) is from an external class Tec_connector. The id is passed when logging in(running navLanding)
   * and is used to get the correct data for the user in getWork(int id)
   */
  public void navLanding() {
    List<String> bands = new ArrayList<>(/*manager.getBandNames()*/);
    bands.add("hennllo1");
    bands.add("hennllo2");
    bands.add("hennllo3");
    bands.add("hennllo4");
    bands.add("hennllo5");
    bands.add("hennllo6");
    bands.add("hennllo7");
    bands.add("hennllo8");
    bands.add("hennllo9");
    for (String band : bands) {
      Label lblBand = new Label(band);
      lblBand.getStyleClass().add("lblBand");
      contents.getChildren().add(lblBand);
    } container.getChildren().add(contents);
  }

  //Method runs when fxml is loaded
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    manager = new backend.Manager(1);
    navLanding();
  }
}
