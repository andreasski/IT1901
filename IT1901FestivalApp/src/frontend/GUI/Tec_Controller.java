package frontend.GUI;

/**
 * Tec_Controller handles user interaction with the GUI.
 * It also generates fxml dynamically based on external data.
 * @author  Andreas Skifjeld
 * @version 1.0
 * @since   26.09.2017
 */

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

public class Tec_Controller implements Initializable{

  //CONSTANTS
  private final String INSTRUCTION_LABEL= "Se gjennom listen for å se oppdragene dine. Listen er kronologisk sortert, så det øverste oppdraget er ditt neste";

  @FXML
  private Label instructionBoxLbl;

  @FXML
  private VBox container;

  private int id;
  private ScrollPane workScrollPane = new ScrollPane();
  private VBox contents = new VBox();


  /**
   * The method creates fxml content based on external information that is collected with the method getWork(int id)
   * getWork(int id) is from an external class Tec_connector. The id is passed when logging in(running navLanding)
   * and is used to get the correct data for the user in getWork(int id)
   * @param id
   */
  public void navLanding(int id) {
    int listIndex = 1;
    this.id = id;
    String date = "";
    instructionBoxLbl.setText(INSTRUCTION_LABEL);
    List<String> workList = getWork(id);
    Label workHeader = new Label("Arbeid");
    workHeader.setId("headerScrollPane");
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
      workContainer.setLeftAnchor(sceneLabel, 14.0);
      workContainer.setRightAnchor(timeLabel, 50.0);
      contents.getChildren().add(workContainer);
      listIndex++;
    }
    workScrollPane.setContent(contents);
    workScrollPane.setId("scrollPane");
    workScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    container.getChildren().add(workScrollPane);
  }

  public List<String> getWork(int id) {
    Random rng = new Random();
    List<String> tempList = new ArrayList<>();
    for (int j = 0; j < 20; j++) {
      tempList.add((1+rng.nextInt(1)) + "." + (1 +rng.nextInt(1)) + ".201" + rng.nextInt(2)
          + " Scene" + rng.nextInt(10) + " " + rng.nextInt(23) + ":" + rng.nextInt(59) + "-"
          + rng.nextInt(23) + ":" + rng.nextInt(59));
    }
    return tempList;
  }

  //Method runs when fxml is loaded
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    navLanding(1);
  }
}
