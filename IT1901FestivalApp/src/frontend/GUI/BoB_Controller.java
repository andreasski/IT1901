package frontend.GUI;

import backend.BookingBoss;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BoB_Controller implements Initializable {
  private BookingBoss bob;
  @FXML
  private Label lvl1, lvl2, Spacing_lvl1_lvl2, instructionBoxLabel;

  @FXML
  private VBox container;

  private final String concInfoBoxText = "Velg en konsert i listen for å se på rapporten til denne konserten. Trykk på 'Sett pris' for å sette pris på oppkommende konserter";
  private final String concDetInfoBoxText = "??? hmmm ??? ??? hmmm ??? ??? hmmm ??? ??? hmmm ??? ??? hmmm ??? ??? hmmm ??? ??? hmmm ??? ??? hmmm ??? ??? hmmm ??? ??? hmmm ??? ??? hmmm ???";
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
    for (String item : concDetArr) {
      System.out.println(item);
    }
    Label concReportHeader = new Label("Konsert rapport " + concDetArr[0]);
    concReportHeader.getStyleClass().add("headerScrollPane");
    AnchorPane concertReport = new AnchorPane();
    concertReport.getStyleClass().add("margin");
    concertReport.setId("concertReport");
    container.getChildren().addAll(concReportHeader, concertReport);
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Stage stage = new Stage();
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/tec_landing.fxml"));

    Parent root = null;
    try {
      root = (Parent)fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Tec_Controller controller = fxmlLoader.<Tec_Controller>getController();
    controller.setTechId(2);
    Scene scene = new Scene(root, 800,600);

    stage.setScene(scene);

    controller.initi();

    stage.show();
    bob = new BookingBoss();
    lvl1.setOnMouseClicked(event -> navLanding());
    lvl2.setOnMouseClicked(event -> navLanding());
    navLanding();
  }
}
