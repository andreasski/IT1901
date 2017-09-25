package arrGUI;

/**
 * Arr_Controller handles user interaction with the GUI.
 * It also generates fxml dynamically based on external data.
 * @author  Andreas Skifjeld
 * @version 1.0
 * @since   24.09.2017
 */

/*
* TODO
* - Move the Spacing_lvl1_lvl2 and Spacing_lvl2_lvl3 renaming to the resetContainer method
* */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class Arr_Controller implements Initializable{

  private Random rng = new Random();
  private VBox contents = new VBox();
  private ScrollPane scrollPane;
  private String dateSave = "00.00.0000";

  @FXML
  private VBox container;

  @FXML
  private Label lvl1, lvl2, lvl3, Spacing_lvl1_lvl2, Spacing_lvl2_lvl3, instructionBoxLbl;



  //Constants
  private final double LEFT_ANCHOR_CONCERTS       = 16;
  private final double RIGHT_ANCHOR_CONCERTS      = 50;
  private final String LANDING_INFO_TEXT          = "Klikk på en av datoene for å se oversikten over konserter på denne datoen";
  private final String CONCERTS_INFO_TEXT         = "Klikk på en av konsertene i listen for å se flere detaljer";
  private final String CONCERT_DETAILS_INFO_TEXT  = "Les av listen for å se hvilke teknikere som jobber under denne konserten";
  private final String[] INFO_TEXT_ARRAY          = {LANDING_INFO_TEXT, CONCERTS_INFO_TEXT, CONCERT_DETAILS_INFO_TEXT};
  private final String[] SUB_HEADER_TEXT_ARRAY    = {"Dato", "Konserter", "Konsert detaljer"};

  public ArrayList<Integer> getContent(int n) {
    ArrayList<Integer> dbDates = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      dbDates.add(rng.nextInt(30) + 1);
    } return dbDates;
  }


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
    ArrayList<Integer> dates = getContent(50);
    for (int i = 0; i < dates.size(); i++) {
      Label lbl = new Label("" + dates.get(i) + ".09.2017");
      lbl.getStyleClass().add("lblListItem");
      StackPane dateButton= new StackPane();
      dateButton.getStyleClass().add("listItem" + i % 2);
      dateButton.getChildren().add(lbl);
      dateButton.setOnMouseClicked(event -> navConcerts(lbl.getText()));
      contents.getChildren().add(dateButton);
    }
    scrollPane.setContent(contents);
    container.getChildren().add(scrollPane);
  }
  /**
   * Loads data for "concerts" from an external source, using an external class Arr_connector
   * The data is stored in the ArrayList<String> dates variable with the method getConcerts(String date)
   * The method then adds the data in fxml form to the container
   */
  public void navConcerts(String date) {
    dateSave = date;
    resetContainer(1);
    Spacing_lvl1_lvl2.setText(" > ");
    Spacing_lvl2_lvl3.setText("");
    ArrayList<String> testData = new ArrayList<>();

    for (int i = 0; i < 50; i++) {
      int day = rng.nextInt(5);
      String fullDate = day + ".09.2017";
      String fullStrObject = fullDate + " Scene" + rng.nextInt(10) + " Band" + rng.nextInt(5) + " " + rng.nextInt(16) + ":00-" + rng.nextInt(23) + ":00";
      testData.add(fullStrObject);
    }

    String scene = "";
    int concertIndex = 0;

    for (int i = 0; i < testData.size(); i++) {
      String[] tmpArray = testData.get(i).split(" ");
      AnchorPane concertButton = new AnchorPane();

      if (date.equals(tmpArray[0])) {
        if (!(scene.equals(tmpArray[1]))) {
          scene = tmpArray[1];
          Label sceneHeader = new Label(scene);
          sceneHeader.getStyleClass().add("concertsSceneHeader");
          contents.getChildren().add(sceneHeader);
          concertIndex = 1;
        }
        Label bandLabel = new Label(tmpArray[2]);
        Label timeLabel = new Label(tmpArray[3]);
        concertButton.getChildren().addAll(bandLabel, timeLabel);
        concertButton.setLeftAnchor(bandLabel, LEFT_ANCHOR_CONCERTS);
        concertButton.setRightAnchor(timeLabel, RIGHT_ANCHOR_CONCERTS);
        concertButton.getStyleClass().add("listItem" + concertIndex++ % 2);
        concertButton.setOnMouseClicked(event ->navConcertDetails("01.01.2000", "Band1", "18:00-20:00"));
      }
      contents.getChildren().add(concertButton);
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
    String[] technicianArray = getTechnicians(date, band, time).split("\\.");
    AnchorPane wrapper = new AnchorPane();
    VBox detailsWrapper = new VBox();

    Label dateLabel = new Label("Dato: " + date);
    dateLabel.getStyleClass().add("concertDetailsLabel");
    Label bandLabel = new Label("Band: " + band);
    bandLabel.getStyleClass().add("concertDetailsLabel");
    Label timeLabel = new Label("Klokka: " + time);
    timeLabel.getStyleClass().add("concertDetailsLabel");

    detailsWrapper.getChildren().addAll(dateLabel, bandLabel, timeLabel);
    ScrollPane technichiansScrollPane = new ScrollPane();
    technichiansScrollPane.setId("technichiansScrollPane");

    for (int i = 0; i < technicianArray.length; i++) {
      Label technichianLabel = new Label(technicianArray[i]);
      technichianLabel.getStyleClass().add("technicianListItem" + i % 2);
      contents.getChildren().add(technichianLabel);
    }

    Label technicianHeader = new Label("Teknikere");
    technicianHeader.setId("technicianHeader");
    technichiansScrollPane.setContent(contents);
    VBox technichiansScrollPaneWrapper= new VBox(technicianHeader, technichiansScrollPane);

    wrapper.getChildren().addAll(detailsWrapper, technichiansScrollPaneWrapper);
    wrapper.setId("concertDetailsWrapper");
    wrapper.setLeftAnchor(detailsWrapper, LEFT_ANCHOR_CONCERTS);
    wrapper.setRightAnchor(technichiansScrollPaneWrapper, 0.0);
    wrapper.setTopAnchor(detailsWrapper, 14.0);
    wrapper.setBottomAnchor(detailsWrapper, 14.0);

    container.getChildren().add(wrapper);
  }

  public String getTechnicians(String date, String band, String time) {
    String[] testData = {"01.01.2000 Scene1 Band1 18:00-20:00 tech1.tech2.tech11.tech32", "01.01.2001 Scene2 Band3 16:00-20:00 tech1.tech2.tech22.tech23", "01.01.2001 Scene2 Band3 16:00-20:30 tech5.tech3.tech01.tech0"};
    for (int i = 0; i < testData.length; i++) {
      String[] concertArray = testData[i].split(" ");
      if (date.equals(concertArray[0]) && band.equals(concertArray[2]) && time.equals(concertArray[3])) {
        return concertArray[4];
      }
    } return "";
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvl1.setOnMouseClicked(event -> navLanding());
    lvl2.setOnMouseClicked(event -> navConcerts(dateSave));
    navLanding();
  }
}
