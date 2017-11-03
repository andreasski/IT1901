package frontend.GUI;

/**
 * Arr_Controller handles user interaction with the GUI.
 * It also generates fxml dynamically based on external data.
 * @author  Andreas Skifjeld
 * @version 1.0
 * @since   24.09.2017
 */

import backend.Organizer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.LightBase;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class Arr_Controller implements Initializable{

  private VBox contents = new VBox();
  private ScrollPane scrollPane;
  private String dateSave = "00.00.0000";
  private Label headerScrollPane;

  @FXML
  private VBox container;

  @FXML
  private Label lvl1, lvl2, lvl3, Spacing_lvl1_lvl2, Spacing_lvl2_lvl3, instructionBoxLbl;

  @FXML
  private Button btnAddConc;

  //Constants
  private final String LANDING_INFO_TEXT            = "Klikk på en av datoene for å se oversikten over konserter på denne datoen";
  private final String CONCERTS_INFO_TEXT           = "Klikk på en av konsertene i listen for å se flere detaljer";
  private final String CONCERT_DETAILS_INFO_TEXT    = "Les av listen for å se hvilke teknikere som jobber under denne konserten";
  private final String[] INFO_TEXT_ARRAY            = {LANDING_INFO_TEXT, CONCERTS_INFO_TEXT, CONCERT_DETAILS_INFO_TEXT};
  private final String[] SUB_HEADER_TEXT_ARRAY      = {"Dato", "Konserter", "Konsert detaljer"};

  /**
   * Resets the container VBox to the empty version of the page.
   * I.e, changing labels and clearing the old contents
   * @param n Page index/deapth. 1 is landing, 2 is concerts and 3 is concertDetails
   */
  public void resetContainer(int n) {
    headerScrollPane = new Label(SUB_HEADER_TEXT_ARRAY[n]);
    headerScrollPane.setId("headerScrollPane");
    Label[] lblSubHeaderArray = {lvl1, lvl2, lvl3};
    for (int i = 0; i < SUB_HEADER_TEXT_ARRAY.length; i++) {lblSubHeaderArray[i].setText((i <= n) ? SUB_HEADER_TEXT_ARRAY[i] : "");}
    scrollPane = new ScrollPane();
    scrollPane.setId("scrollPane");
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    container.getChildren().clear();
    contents.getChildren().clear();
    instructionBoxLbl.setText(INFO_TEXT_ARRAY[n]);
    container.getChildren().add(headerScrollPane);
  }

  /**
   * Loads data for "landing" from an external source, using an external class Arr_connector
   * The data is stored in the ArrayList<String> dates variable with the method getDates()
   * The method then adds the data in fxml form to the container
   */
  public void navLanding() {
    resetContainer(0);
    Spacing_lvl1_lvl2.setText("");
    Spacing_lvl2_lvl3.setText("");
    List<String> dates = Organizer.getDate();
    for (int i = 0; i < dates.size(); i++) {
      Label lblDate = new Label(dates.get(i));
      lblDate.getStyleClass().add("listItem" + ((i + 1) % 2));
      lblDate.setOnMouseClicked(event -> navConcerts(Organizer.getConcerts(lblDate.getText()), lblDate.getText()));
      contents.getChildren().add(lblDate);
    }
    scrollPane.setContent(contents);
    container.getChildren().add(scrollPane);
  }

  /**
   * Loads data for "concerts" from an external source, using an external class Arr_connector
   * The data is stored in the ArrayList<String> dates variable with the method getConcerts(String date)
   * The method then adds the data in fxml form to the container
   */
  public void navConcerts(List<String> concerts, String date) {
    dateSave = date;
    resetContainer(1);
    headerScrollPane.setText(headerScrollPane.getText() + " " + date);
    Spacing_lvl1_lvl2.setText(" > ");
    Spacing_lvl2_lvl3.setText("");
    for (int i = 0; i < concerts.size(); i++) {
      String[] tempArray = concerts.get(i).split(";");
      Label lblConc = new Label(tempArray[1]);
      Label lblStage = new Label("Scene: " + tempArray[0]);
      AnchorPane lblConcert = new AnchorPane(lblConc, lblStage);
      lblConcert.getStyleClass().add("listItem" + ((i + 1) % 2));
      lblConcert.setOnMouseClicked(event -> navConcertDetails(tempArray[0], tempArray[1], date));
      lblConcert.setLeftAnchor(lblConc, 14.0);
      lblConcert.setTopAnchor(lblConc, 0.0);
      lblConcert.setRightAnchor(lblStage, 14.0);
      lblConcert.setTopAnchor(lblStage, 0.0);
      contents.getChildren().add(lblConcert);
    }
    scrollPane.setContent(contents);
    container.getChildren().add(scrollPane);
  }

  /**
   * Loads data for "concertDetails" from an external source, using an external class Arr_connector
   * The data is stored in the ArrayList<String> dates variable with the method getTechnicians(String date, String band, String time)
   * * The method then adds the data in fxml form to the container
   */
  public void navConcertDetails(String stage, String concertName, String date) {
    resetContainer(2);
    Spacing_lvl1_lvl2.setText(" > ");
    Spacing_lvl2_lvl3.setText(" > ");
    List<String> technicians = Organizer.getTechnicians(concertName);

    Label lblStage = new Label("Scene: " + stage);
    Label lblConcert = new Label("Konsert: " + concertName);
    Label lblDate = new Label(date);

    for (int i = 0; i < technicians.size(); i++) {
      Label technichianLabel = new Label(technicians.get(i));
      technichianLabel.getStyleClass().add("technicianListItem" + ((i + 1) % 2));
      contents.getChildren().add(technichianLabel);
    }
    Label technicianHeader = new Label("Teknikere");
    technicianHeader.getStyleClass().add("technicianHeader");
    ScrollPane technichiansScrollPane = new ScrollPane(contents);
    technichiansScrollPane.getStyleClass().add("technichiansScrollPane");
    VBox technichiansScrollPaneWrapper= new VBox(technicianHeader, technichiansScrollPane);

    List<String> techNeeds = Organizer.getTechnicalNeeds(concertName);
    VBox techNeedsContents = new VBox();
    for (int i = 0; i < techNeeds.size(); i++) {
      Label lblTechNeed = new Label(techNeeds.get(i));
      lblTechNeed.getStyleClass().add("technicianListItem" + ((i + 1) % 2));
      techNeedsContents.getChildren().add(lblTechNeed);
    }
    Label technicalNeedsHeader = new Label("Tekniske behov");
    technicalNeedsHeader.getStyleClass().add("technicianHeader");
    ScrollPane techNeedsScrollPane = new ScrollPane(techNeedsContents);
    techNeedsScrollPane.getStyleClass().add("technichiansScrollPane");
    VBox techNeedsScrollPaneWrapper= new VBox(technicalNeedsHeader, techNeedsScrollPane);
    VBox techDetailsContainer = new VBox(technichiansScrollPaneWrapper, techNeedsScrollPaneWrapper);

    List<String> bookingOffers = Organizer.getBookingOffers(concertName);
    Label headerBookingOffers = new Label("Program");
    headerBookingOffers.setId("headerBookingOffers");
    VBox contentsBO = new VBox();
    for (int i = 0; i < bookingOffers.size(); i++) {
      String[] bookingOfferDetails = bookingOffers.get(i).split(";");
      Label lblBand = new Label(bookingOfferDetails[0]);
      Label lblTime = new Label(bookingOfferDetails[1]);
      AnchorPane lblBookingOffer = new AnchorPane(lblBand, lblTime);
      lblBookingOffer.setLeftAnchor(lblBand, 14.0);
      lblBookingOffer.setRightAnchor(lblTime, 14.0);
      lblBookingOffer.getStyleClass().add("lisItemBO" + ((i + 1) % 2));
      contentsBO.getChildren().add(lblBookingOffer);
    }
    ScrollPane bookingOffersScrollPane = new ScrollPane(contentsBO);
    bookingOffersScrollPane.getStyleClass().add("scrollPane");
    VBox boContainer = new VBox(headerBookingOffers, bookingOffersScrollPane);

    AnchorPane concertDetailsWrapper = new AnchorPane(lblStage, lblConcert, lblDate,techDetailsContainer, boContainer);
    concertDetailsWrapper.setLeftAnchor(lblConcert, 14.0);
    concertDetailsWrapper.setTopAnchor(lblConcert, 14.0);
    concertDetailsWrapper.setLeftAnchor(lblStage, 14.0);
    concertDetailsWrapper.setTopAnchor(lblStage, 28.0);
    concertDetailsWrapper.setRightAnchor(lblDate, 178.0);
    concertDetailsWrapper.setTopAnchor(lblDate, 14.0);
    concertDetailsWrapper.setRightAnchor(techDetailsContainer, 0.0);
    concertDetailsWrapper.setTopAnchor(techDetailsContainer, 0.0);
    concertDetailsWrapper.setLeftAnchor(boContainer, 0.0);
    concertDetailsWrapper.setTopAnchor(boContainer, 56.0);
    concertDetailsWrapper.setId("concertDetailsWrapper");
    container.getChildren().add(concertDetailsWrapper);
  }

  public void navAddConc() {
    resetContainer(1);
    Spacing_lvl1_lvl2.setText(" > ");
    lvl2.setText("Legg til konsert");
    container.getChildren().clear();
    Label headerAddConc = new Label("Legg til konsert");
    headerAddConc.setId("headerScrollPane");

    Label lblName = new Label("Navn: ");
    TextField inpName = new TextField();
    inpName.getStyleClass().add("inpField");
    VBox nameCont = new VBox(lblName, inpName);
    Label lblDate = new Label("Dato: (dd.mm.åååå)");
    TextField inpDate = new TextField();
    inpDate.getStyleClass().add("inpField");
    VBox dateCont = new VBox(lblDate, inpDate);

    Label lblStagee = new Label("Scene: ");
    TextField inpStagee = new TextField();
    inpName.getStyleClass().add("inpField");
    VBox stageCont = new VBox(lblStagee, inpStagee);

    Button btnAdd = new Button("Legg til");
    Label lblFeedback = new Label();
    Label lblStage = new Label("Søk etter en scene: ");
    TextField inpStage = new TextField();
    inpStage.getStyleClass().add("searchStage");
    Button btnSearchStage = new Button("Søk");
    btnSearchStage.getStyleClass().add("searchStage");
    ScrollPane stageScrollPane = new ScrollPane();
    stageScrollPane.getStyleClass().add("scrollPane");
    VBox searchStage = new VBox(lblStage, inpStage, btnSearchStage, stageScrollPane);
    btnSearchStage.setOnAction(event -> {
      List<String> stages = Organizer.getStage(inpStage.getText());
      if (stages.size() == 0) { stageScrollPane.setContent(new Label("Ingen treff"));}
      VBox stagesContents = new VBox();
      for (int i = 0; i < stages.size(); i++) {
        Label stage = new Label(stages.get(i));
        stage.getStyleClass().add("listItemStage" + ((i + 1) % 2));
        stage.setOnMouseClicked(event1 -> inpStagee.setText(stage.getText()));
        stagesContents.getChildren().add(stage);
      }
      stageScrollPane.setContent(stagesContents);
    });
    btnAdd.setOnAction(event -> {
      if (!validateDate(inpDate.getText())) {
        lblFeedback.setText("Vennligst fyll inn en gyldig dato");
      } else {
        lblFeedback.setText(Organizer.addConcert(inpName.getText(), inpDate.getText(), inpStagee.getText()));
      }
    });
    AnchorPane addConcContents = new AnchorPane(nameCont, dateCont, stageCont, searchStage, btnAdd, lblFeedback);
    addConcContents.setId("concertDetailsWrapper");
    addConcContents.getStyleClass().add("margin");
    addConcContents.setLeftAnchor(nameCont, 14.0);
    addConcContents.setTopAnchor(nameCont, 14.0);
    addConcContents.setLeftAnchor(dateCont, 14.0);
    addConcContents.setTopAnchor(dateCont, 58.0);
    addConcContents.setLeftAnchor(stageCont, 14.0);
    addConcContents.setTopAnchor(stageCont, 102.0);
    addConcContents.setLeftAnchor(btnAdd, 14.0);
    addConcContents.setTopAnchor(btnAdd, 146.0);
    addConcContents.setLeftAnchor(lblFeedback, 14.0);
    addConcContents.setTopAnchor(lblFeedback, 182.0);
    addConcContents.setBottomAnchor(lblFeedback, 14.0);
    addConcContents.setRightAnchor(searchStage, 14.0);
    addConcContents.setTopAnchor(searchStage, 14.0);
    container.getChildren().addAll(headerAddConc, addConcContents);
  }

  private int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  public boolean validateDate(String date) {
    String[] ddmmyyyy = date.split("\\.");
    if (ddmmyyyy.length != 3 || ddmmyyyy[0].length() != 2 || ddmmyyyy[1].length() != 2 || ddmmyyyy[2].length() != 4) { return false;}
    for (int i = 0; i < ddmmyyyy.length; i++) {
      for (int j = 0; j < ddmmyyyy[i].length(); j++) { if (!Character.isDigit(ddmmyyyy[i].charAt(j))) { return false;}}
    }
    int day = (ddmmyyyy[0].charAt(0) == 0) ? ddmmyyyy[0].charAt(0) : Integer.parseInt(ddmmyyyy[0]);
    int month = (ddmmyyyy[1].charAt(0) == 0) ? ddmmyyyy[1].charAt(0) : Integer.parseInt(ddmmyyyy[1]);
    int yS = -1;
    for (int i = 0; i < ddmmyyyy[2].length(); i++) { if (yS == -1 && ddmmyyyy[2].charAt(i) != 0) { yS = i;}}
    int year = (yS == -1) ? 0 : Integer.parseInt(ddmmyyyy[2].substring(yS, 4)) ;
    if (month < 1 || month > 12) { return false;}
    else if ((day < 1 || day > days[month - 1])) {
      return day == 29 && month == 2 && ((year % 400 == 0 || year % 4 == 0) && !(year % 100 == 0));
    } return true;
  }

  //Method runs when fxml is loaded
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Organizer.init();
    lvl1.setOnMouseClicked(event -> navLanding());
    lvl2.setOnMouseClicked(event -> navConcerts(Organizer.getConcerts(dateSave), dateSave));
    btnAddConc.setOnMouseClicked(event -> navAddConc());
    navLanding();
  }
}
