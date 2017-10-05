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


  //CONSTANTS
  private final String INSTRUCTION_LABEL= "Se gjennom listen for å se oppdragene dine. Listen er kronologisk sortert, så det øverste oppdraget er ditt neste";
  private final double LEFT_ANCHOR_WORK   = 14.0;
  private final double RIGHT_ANCHOR_WORK  = 50.0;
  @FXML
  private Label instructionBoxLbl;

  @FXML
  private VBox container;

  private int techId;
  private ScrollPane workScrollPane = new ScrollPane();
  private VBox contents = new VBox();

  /**
   * The method creates fxml content based on external information that is collected with the method getWork(int id)
   * getWork(int id) is from an external class Tec_connector. The id is passed when logging in(running navLanding)
   * and is used to get the correct data for the user in getWork(int id)
   */
  public void navLanding() {

  }

  //Method runs when fxml is loaded
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("HENLO");
    backend.Manager manager = new backend.Manager(1);

    navLanding();
  }
}
