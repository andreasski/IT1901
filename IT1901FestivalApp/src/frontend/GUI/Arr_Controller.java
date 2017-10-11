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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Arr_Controller implements Initializable{

  private VBox contents = new VBox();
  private ScrollPane scrollPane;
  private String dateSave = "00.00.0000";

  @FXML
  private VBox container;

  @FXML
  private Label lvl1, lvl2, lvl3, Spacing_lvl1_lvl2, Spacing_lvl2_lvl3, instructionBoxLbl;

  //Constants
  private final double LEFT_ANCHOR_CONCERTS         = 16;
  private final double RIGHT_ANCHOR_CONCERTS        = 50;
  private final double LEFT_ANCHOR_DETAILS          = 16;
  private final double TOP_ANCHOR_DETAILS           = 16;
  private final double BOTTOM_ANCHOR_DETAILS        = 16;
  private final double RIGHT_ANCHOR_TECHNICIAN_LIST = 0;
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
    Label headerScrollPane = new Label(SUB_HEADER_TEXT_ARRAY[n]);
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
    Spacing_lvl1_lvl2.setText(" > ");
    Spacing_lvl2_lvl3.setText("");
    String stage = "";
    int concertIndex = 0;
    for (int i = 0; i < concerts.size(); i++) {
      String[] tempArray = concerts.get(i).split(";");
      AnchorPane btnConcert = new AnchorPane();
      if (!stage.equals(tempArray[0])) {
        stage = tempArray[0];
        Label sceneHeader = new Label(stage);
        sceneHeader.getStyleClass().add("concertsSceneHeader");
        contents.getChildren().add(sceneHeader);
        concertIndex = 1;
      }
      Label lblBand = new Label(tempArray[1]);
      Label lblTime = new Label(tempArray[2]);
      btnConcert.getChildren().addAll(lblBand, lblTime);
      btnConcert.setLeftAnchor(lblBand, LEFT_ANCHOR_CONCERTS);
      btnConcert.setRightAnchor(lblTime, RIGHT_ANCHOR_CONCERTS);
      btnConcert.getStyleClass().add("listItem" + ++concertIndex % 2);
      btnConcert.setOnMouseClicked(event -> { navConcertDetails(dateSave, lblBand.getText(), lblTime.getText());});
      contents.getChildren().add(btnConcert);
    }
    scrollPane.setContent(contents);
    container.getChildren().add(scrollPane);
  }

  /**
   * Loads data for "concertDetails" from an external source, using an external class Arr_connector
   * The data is stored in the ArrayList<String> dates variable with the method getTechnicians(String date, String band, String time)
   * * The method then adds the data in fxml form to the container
   */
  public void navConcertDetails(String date, String band, String time) {
    resetContainer(2);
    Spacing_lvl1_lvl2.setText(" > ");
    Spacing_lvl2_lvl3.setText(" > ");
    List<String> technicians = Organizer.getTechnicians(date, band, time);
    AnchorPane concertDetailsWrapper = new AnchorPane();
    VBox detailsWrapper = new VBox();
    Label lblDate = new Label("Dato: " + date);
    Label lblBand = new Label("Band: " + band);
    Label lblTime = new Label("Klokka: " + time);
    lblDate.getStyleClass().add("lblConcertDetails");
    lblBand.getStyleClass().add("lblConcertDetails");
    lblTime.getStyleClass().add("lblConcertDetails");
    detailsWrapper.getChildren().addAll(lblDate, lblBand, lblTime);
    ScrollPane technichiansScrollPane = new ScrollPane();
    technichiansScrollPane.setId("technichiansScrollPane");
    for (int i = 0; i < technicians.size(); i++) {
      Label technichianLabel = new Label(technicians.get(i));
      technichianLabel.getStyleClass().add("technicianListItem" + ((i + 1) % 2));
      contents.getChildren().add(technichianLabel);
    }
    Label technicianHeader = new Label("Teknikere");
    technicianHeader.setId("technicianHeader");
    technichiansScrollPane.setContent(contents);
    VBox technichiansScrollPaneWrapper= new VBox(technicianHeader, technichiansScrollPane);
    concertDetailsWrapper.getChildren().addAll(detailsWrapper, technichiansScrollPaneWrapper);
    concertDetailsWrapper.setId("concertDetailsWrapper");
    concertDetailsWrapper.setLeftAnchor(detailsWrapper, LEFT_ANCHOR_DETAILS);
    concertDetailsWrapper.setRightAnchor(technichiansScrollPaneWrapper, RIGHT_ANCHOR_TECHNICIAN_LIST);
    concertDetailsWrapper.setTopAnchor(detailsWrapper, TOP_ANCHOR_DETAILS);
    concertDetailsWrapper.setBottomAnchor(detailsWrapper, BOTTOM_ANCHOR_DETAILS);
    container.getChildren().add(concertDetailsWrapper);
  }

  //Method runs when fxml is loaded
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Organizer.init();
    lvl1.setOnMouseClicked(event -> navLanding());
    lvl2.setOnMouseClicked(event -> navConcerts(Organizer.getConcerts(dateSave), dateSave));
    navLanding();
  }
}
