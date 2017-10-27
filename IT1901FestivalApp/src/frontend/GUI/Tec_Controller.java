package frontend.GUI;

/**
 * Tec_Controller handles user interaction with the GUI.
 * It also generates fxml dynamically based on external data.
 * @author  Andreas Skifjeld
 * @version 1.0
 * @since   26.09.2017
 */

import backend.Technician;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Tec_Controller implements Initializable{

  //CONSTANTS
  private final String INSTRUCTION_LABEL  = "Se gjennom listen for å se oppdragene dine. Listen er kronologisk sortert, så det øverste oppdraget er ditt neste";
  private final double LEFT_ANCHOR_WORK   = 10.0;
  private final double RIGHT_ANCHOR_WORK  = 50.0;

  @FXML
  private VBox container;

  @FXML
  private VBox aside;

  private int techId;
  private ScrollPane workScrollPane = new ScrollPane();
  private VBox contents = new VBox();

  public void setTechId(int id) {
    techId = id;
  }

  /**
   * The method creates fxml content based on external information that is collected with the method getWork(int id)
   * getWork(int id) is from an external class Tec_connector. The id is passed when logging in(running navLanding)
   * and is used to get the correct data for the user in getWork(int id)
   * @param id
   */
  public void navLanding(int id) {
    Technician.init();
    int listIndex = 1;
    techId = id;
    String date = "";
    Label instructionBoxLbl = new Label(INSTRUCTION_LABEL);
    instructionBoxLbl.setId("instructionBoxLabel");
    instructionBoxLbl.getStyleClass().add("textContainer");
    List<String> workList = Technician.getWork(techId);
    Label workHeader = new Label("Arbeid");
    workHeader.setId("headerScrollPane");
    aside.getChildren().add(instructionBoxLbl);
    container.getChildren().add(workHeader);
    for (int i = 0; i < workList.size(); i++) {
      AnchorPane workContainer = new AnchorPane();
      String[] workArray = workList.get(i).split(" ");
      if (!date.equals(workArray[0])) {
        date = workArray[0];
        Label dateHeader = new Label(date);
        dateHeader.getStyleClass().add("workDateHeader");
        contents.getChildren().add(dateHeader);
        listIndex = 1;
      }
      Label sceneLabel = new Label(workArray[1]);
      Label timeLabel = new Label(workArray[2]);
      workContainer.getChildren().addAll(sceneLabel, timeLabel);
      workContainer.getStyleClass().add("listItem" + (listIndex % 2));
      workContainer.setLeftAnchor(sceneLabel, LEFT_ANCHOR_WORK);
      workContainer.setRightAnchor(timeLabel, RIGHT_ANCHOR_WORK);
      contents.getChildren().add(workContainer);
      listIndex++;
    }
    workScrollPane.setContent(contents);
    workScrollPane.setId("scrollPane");
    workScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    container.getChildren().add(workScrollPane);
  }

  //Method runs when fxml is loaded
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    navLanding(2);
  }

}
