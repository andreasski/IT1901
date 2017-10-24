package frontend.GUI;
import backend.BookingBoss;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.nio.charset.CharacterCodingException;
import java.util.List;
import java.util.ResourceBundle;

public class BoB_Controller implements Initializable {
  private BookingBoss bob;
  @FXML
  private Label lvl1, lvl2, Spacing_lvl1_lvl2, instructionBoxLabel;

  @FXML
  private VBox container;

  @FXML
  private Button setPrice;
  private final String concInfoBoxText = "Velg en konsert i listen for å se på rapporten til denne konserten. Trykk på 'Sett pris' for å sette pris på oppkommende konserter";
  private final String concDetInfoBoxText = "Du finner nøkkelinformasjoenen til konserten i boksen til høyre";
  private final String[] infoBoxText = {concInfoBoxText, concDetInfoBoxText};
  private final String[] levelText = {"Konserter", "Konsert rapport"};


  public void resetContainer(int lvl) {
    container.getChildren().clear();
    Spacing_lvl1_lvl2.setText((lvl < 1) ? "" : " > ");
    lvl1.setText((0 <= lvl) ? levelText[0] : "");
    lvl2.setText((1 <= lvl) ? levelText[1] : "");
    instructionBoxLabel.setText(infoBoxText[lvl]);
  }

  public void navLanding() {
    resetContainer(0);
    List<String> concerts = bob.getConcerts();
    Label concHeader = new Label("Tidligere konserter");
    concHeader.getStyleClass().add("headerScrollPane");
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setId("scrollPane");
    VBox contents = new VBox();
    for (int i = 0; i < concerts.size(); i++) {
      String[] concDetArr = concerts.get(i).split(";");
      Label concName = new Label(concDetArr[1]);
      Label concDate = new Label(concDetArr[2]);
      AnchorPane concContainer = new AnchorPane(concName, concDate);
      concContainer.getStyleClass().add("listItem" + ((i + 1) % 2));
      concContainer.setId(concDetArr[0]);
      concContainer.setOnMouseClicked(event -> navReport(Integer.parseInt(concContainer.getId())));
      concContainer.setLeftAnchor(concName, 14.0);
      concContainer.setRightAnchor(concDate, 14.0);
      contents.getChildren().add(concContainer);
      }
    scrollPane.setContent(contents);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    container.getChildren().addAll(concHeader, scrollPane);
  }

  public void navReport(int concId) {
    resetContainer(1);
    String concDet = bob.getConcert(concId);
    String[] concDetArr = concDet.split(";");
    Label concReportHeader = new Label("Konsert rapport " + concDetArr[0]);
    Label concName = new Label("Konsert: " + concDetArr[0]);
    Label concPrice = new Label("Pris: " + concDetArr[1]);
    Label concCrowd = new Label("Publikum: " + concDetArr[2]);
    Label concExpenses = new Label("Utgifter: " + concDetArr[3]);
    Label concProfit = new Label("Overskudd: " + concDetArr[4]);
    concReportHeader.getStyleClass().add("headerScrollPane");
    AnchorPane concertReport = new AnchorPane();
    concertReport.getStyleClass().add("margin");
    concertReport.setId("concertReport");
    concertReport.getChildren().addAll(concName, concPrice, concCrowd, concExpenses, concProfit);
    concertReport.setLeftAnchor(concName, 14.0);
    concertReport.setTopAnchor(concName, 0.0);
    concertReport.setLeftAnchor(concPrice, 14.0);
    concertReport.setTopAnchor(concPrice, 14.0);
    concertReport.setLeftAnchor(concCrowd, 14.0);
    concertReport.setTopAnchor(concCrowd, 28.0);
    concertReport.setLeftAnchor(concExpenses, 14.0);
    concertReport.setTopAnchor(concExpenses, 42.0);
    concertReport.setLeftAnchor(concProfit, 14.0);
    concertReport.setTopAnchor(concProfit, 56.0);
    concertReport.setBottomAnchor(concProfit, 0.0);
    container.getChildren().addAll(concReportHeader, concertReport);
  }

  public void navOffers() {
    resetContainer(1);
    lvl2.setText("Tilbud");
    List<String> offers = bob.getOffers();
    Label offersHeader = new Label("Ubehandlede tilbud");
    offersHeader.getStyleClass().add("headerScrollPane");
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    VBox contents = new VBox();
    for (int i = 0; i < offers.size(); i++) {
      String[] offerArr = offers.get(i).split(";");
      Label bandName = new Label("Band: " + offerArr[1]);
      Label concName = new Label("Konsert: " + offerArr[2]);
      Label stageName = new Label("Scene: " + offerArr[3]);
      Label date = new Label(offerArr[4]);
      Label time = new Label(offerArr[5]);
      Label lblTicPrice = new Label("Billettpris");
      TextField inpTicPrice = new TextField();
      Button btnSetPrice = new Button("Autogenerer pris");
      btnSetPrice.setOnAction(event -> inpTicPrice.setText("" + bob.generateTicketPrice(bob.getConcertId(offerArr[2]))));
      Button btnAccept = new Button("Godkjenn");
      Button btnDecline = new Button("Avvis");
      btnAccept.getStyleClass().add("btnBookingOffers");
      btnDecline.getStyleClass().add("btnBookingOffers");
      btnAccept.setOnAction(event -> {
        if (authNum(inpTicPrice.getText())) {
          bob.setPrice(bob.getConcertId(offerArr[2]), Integer.parseInt(inpTicPrice.getText()));
          bob.updateOffer(Integer.parseInt(offerArr[0]),1);
          navOffers();
        } else {
          lblTicPrice.setText("Pris må være et positivt heltall");
        }
      });
      btnDecline.setOnAction(event -> {
        bob.updateOffer(Integer.parseInt(offerArr[0]),-1);
        navOffers();
      });
      AnchorPane offerContainer = new AnchorPane(bandName, concName, stageName, date, time, lblTicPrice, inpTicPrice, btnSetPrice, btnAccept, btnDecline);
      offerContainer.getStyleClass().add("listItem" + ((i + 1) % 2));
      offerContainer.setTopAnchor(bandName, 0.0);
      offerContainer.setLeftAnchor(bandName, 14.0);
      offerContainer.setTopAnchor(concName, 14.0);
      offerContainer.setLeftAnchor(concName, 14.0);
      offerContainer.setTopAnchor(stageName, 28.0);
      offerContainer.setLeftAnchor(stageName, 14.0);
      offerContainer.setTopAnchor(date, 0.0);
      offerContainer.setRightAnchor(date, 14.0);
      offerContainer.setTopAnchor(time, 14.0);
      offerContainer.setRightAnchor(time, 14.0);
      offerContainer.setTopAnchor(lblTicPrice, 0.0);
      offerContainer.setLeftAnchor(lblTicPrice, 200.0);
      offerContainer.setTopAnchor(inpTicPrice, 14.0);
      offerContainer.setLeftAnchor(inpTicPrice, 200.0);
      offerContainer.setTopAnchor(btnSetPrice, 40.0);
      offerContainer.setLeftAnchor(btnSetPrice, 200.0);
      offerContainer.setTopAnchor(btnAccept, 70.0);
      offerContainer.setLeftAnchor(btnAccept, 0.0);
      offerContainer.setTopAnchor(btnDecline, 70.0);
      offerContainer.setRightAnchor(btnDecline, 0.0);
      contents.getChildren().add(offerContainer);
    }
    scrollPane.setContent(contents);
    container.getChildren().addAll(offersHeader, scrollPane);
    }

    public boolean authNum(String price) {
      for (int i = 0; i < price.length(); i++) { if (!Character.isDigit(price.charAt(i))) { return false;}}
      int priceInt = Integer.parseInt(price);
      if (priceInt < 0) { return false;}
      return true;
    }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    bob = new BookingBoss();
    lvl1.setOnMouseClicked(event -> navLanding());
    setPrice.setOnMouseClicked(event -> navOffers());
    navLanding();
  }
}
