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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Man_Controller implements Initializable{

  //CONSTANTS
  private final String INSTRUCTION_LABEL = "Velg et av bandene i listen. Du kan så legge til eller fjerne tekniske behov.";


  @FXML
  private VBox instructionBoxContainer;

  @FXML
  private ScrollPane bandsScrollPane;

  @FXML
  private VBox container;

  private backend.Manager manager;
  private VBox contents = new VBox();

  /**
   * The method creates fxml content based on external information that is collected with the method getWork(int id)
   * getWork(int id) is from an external class Tec_connector. The id is passed when logging in(running navLanding)
   * and is used to get the correct data for the user in getWork(int id)
   */
  public void navLanding() {
    List<String> bands = new ArrayList<>(manager.getBandNames());
    bands.addAll(manager.getBandNames());
    for (int i = 0; i < 10; i++) {bands.add("Test Band " + i);}
    VBox bandContainer = new VBox();
    bandContainer.setId("bandContainer");
    for (int i = 1; i < bands.size(); i++) {
      Label lblBand = new Label(bands.get(i));
      lblBand.getStyleClass().add("listItemAside" + i % 2);
      lblBand.setOnMouseClicked(event -> getTechnicalDetails(lblBand.getText()));
      bandContainer.getChildren().add(lblBand);
    }
    Label instructionBoxLbl = new Label(INSTRUCTION_LABEL);
    instructionBoxLbl.setId("instructionBoxLabel");
    instructionBoxLbl.getStyleClass().add("textContainer");
    instructionBoxContainer.getChildren().add(instructionBoxLbl);
    bandsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    bandsScrollPane.setContent(bandContainer);
  }

  public void getTechnicalDetails (String bandName) {
    container.getChildren().clear();
    contents.getChildren().clear();
    Label lblSubHeader = new Label("Tekniske behov");
    lblSubHeader.setId("headerScrollPane");
    HBox userInputContainer = new HBox();
    TextField inpTechNeed = new TextField();
    inpTechNeed.setId("inpTechNeed");
    inpTechNeed.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent args) {
        manager.addTechNeed(inpTechNeed.getText(), bandName);
        getTechnicalDetails(bandName);
      }
    });
    Button btnTechNeed = new Button("Legg til");
    btnTechNeed.setId("btnTechNeed");
    btnTechNeed.setOnAction(event -> {
      manager.addTechNeed(inpTechNeed.getText(), bandName);
      getTechnicalDetails(bandName);
    });
    userInputContainer.getChildren().addAll(inpTechNeed, btnTechNeed);
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setId("scrollPane");
    container.getChildren().addAll(lblSubHeader, userInputContainer);
    List<String> techNeeds = manager.getTechNeeds(bandName);
    for (int i = 0; i < techNeeds.size(); i++) {
      Label lblTechNeed = new Label(techNeeds.get(i));
      lblTechNeed.getStyleClass().add("listItem" + i % 2);
      lblTechNeed.setOnMouseClicked(event -> {
        manager.removeTechNeed(lblTechNeed.getText(), bandName);
        getTechnicalDetails(bandName);
      });
      contents.getChildren().add(lblTechNeed);
    }
    scrollPane.setContent(contents);
    container.getChildren().add(scrollPane);
  }

  //Method runs when fxml is loaded
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    manager = new backend.Manager(1);
    navLanding();
  }
}
