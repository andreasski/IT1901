package frontend.GUI;

/**
 * Tec_Controller handles user interaction with the GUI.
 * It also generates fxml dynamically based on external data.
 * @author  Andreas Skifjeld
 * @version 1.0
 * @since   26.09.2017
 */

import backend.Manager;
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
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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

  @FXML
  private Button btnBookingOffers;

  private backend.Manager manager;
  private VBox contents = new VBox();
  private int manId;

  public void setManId(int id) {
    manId = id;
  }

  /**
   * The method creates fxml content based on external information that is collected with the method getWork(int id)
   * getWork(int id) is from an external class Tec_connector. The id is passed when logging in(running navLanding)
   * and is used to get the correct data for the user in getWork(int id)
   */
  public void navLanding() {
    getNumOffers();
    btnBookingOffers.setOnMouseClicked(event -> {
      getBookingOffers();
      getNumOffers();
    });
    List<String> bands = new ArrayList<>(manager.getBandNames());
    VBox bandContainer = new VBox();
    bandContainer.setId("bandContainer");
    for (int i = 0; i < bands.size(); i++) {
      System.out.println(bands.get(i));
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
      Label lblDel = new Label("X");
      lblDel.setOnMouseClicked(event -> {
        manager.removeTechNeed(lblTechNeed.getText(), bandName);
        getTechnicalDetails(bandName);
      });
      lblDel.getStyleClass().add("underline");
      AnchorPane techNeedContainer = new AnchorPane(lblTechNeed, lblDel);
      techNeedContainer.getStyleClass().add("listItem" + ((i + 1) % 2));
      techNeedContainer.setLeftAnchor(lblTechNeed, 14.0);
      techNeedContainer.setTopAnchor(lblTechNeed, 0.0);
      techNeedContainer.setRightAnchor(lblDel, 14.0);
      techNeedContainer.setTopAnchor(lblDel, 0.0);

      contents.getChildren().add(techNeedContainer);
    }
    scrollPane.setContent(contents);
    container.getChildren().add(scrollPane);
  }

  public void getBookingOffers() {
    container.getChildren().clear();
    contents.getChildren().clear();
    Label lblBookingOffer = new Label("Booking tilbud");
    lblBookingOffer.setId("headerScrollPane");
    container.getChildren().add(lblBookingOffer);
    List<String> offers = manager.getOffers();
    for (int i = 0; i < offers.size(); i++) {
      AnchorPane offerContainer = new AnchorPane();
      offerContainer.getStyleClass().add("concertOffer" + ((i + 1) % 2));
      String[] offerDetails = offers.get(i).split(";");
      offerContainer.setId(offerDetails[0]);
      Label bandName = new Label("Band: " + offerDetails[1]);
      Label concertName = new Label(offerDetails[2]);
      Label stageName = new Label("Scene : " + offerDetails[3]);
      Label concertDate = new Label("Dato: " + offerDetails[4]);
      Label concertTime = new Label("Klokken: " + offerDetails[5]);
      Label concertExpense = new Label("Betaling: " + offerDetails[6] + ",-");
      Button btnAccept = new Button("Godkjenn");
      Button btnDecline = new Button("Avvis");
      btnAccept.getStyleClass().add("btnOffer");
      btnDecline.getStyleClass().add("btnOffer");
      btnAccept.setOnMouseClicked(event -> {
        manager.updateOffer(Integer.parseInt(offerContainer.getId()), 2);
        getNumOffers();
        getBookingOffers();
      });
      btnDecline.setOnMouseClicked(event -> {
        manager.updateOffer(Integer.parseInt(offerContainer.getId()), -1);
        getNumOffers();
        getBookingOffers();
      });
      offerContainer.getChildren().addAll(bandName, concertName, stageName, concertDate, concertTime, concertExpense, btnAccept, btnDecline);
      offerContainer.setLeftAnchor(bandName, 14.0);
      offerContainer.setTopAnchor(bandName, 18.0);
      offerContainer.setLeftAnchor(concertName, 14.0);
      offerContainer.setTopAnchor(concertName, 0.0);
      offerContainer.setTopAnchor(stageName, 18.0);
      offerContainer.setRightAnchor(stageName, 14.0);
      offerContainer.setLeftAnchor(concertDate, 200.0);
      offerContainer.setTopAnchor(concertDate, 0.0);
      offerContainer.setRightAnchor(concertTime, 14.0);
      offerContainer.setTopAnchor(concertTime, 0.0);
      offerContainer.setLeftAnchor(concertExpense, 200.0);
      offerContainer.setTopAnchor(concertExpense, 18.0);
      offerContainer.setTopAnchor(btnAccept, 42.0);
      offerContainer.setLeftAnchor(btnAccept, 0.0);
      offerContainer.setTopAnchor(btnDecline, 42.0);
      offerContainer.setRightAnchor(btnDecline, 0.0);
      contents.getChildren().add(offerContainer);
    }
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setId("scrollPane");
    scrollPane.setContent(contents);
    container.getChildren().add(scrollPane);
  }

  public void getNumOffers() {
    btnBookingOffers.setText("Nye booking tilbud: " + manager.getOffers().size());
  }

  public void init(int id) {
    setManId(id);
    manager = new Manager(id);
    getNumOffers();
    navLanding();
  }

  //Method runs when fxml is loaded
  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
}
