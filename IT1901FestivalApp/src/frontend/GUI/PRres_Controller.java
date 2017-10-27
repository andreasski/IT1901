package frontend.GUI;

import backend.PRres;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PRres_Controller {

    private final String INSTRUCTION_LABEL = "En herlig oversikt over konserter"; //til

    @FXML
    private VBox instructionBoxContainer;

    private backend.PRres prres;

    @FXML
    private VBox container;

    private ScrollPane prScrollPane = new ScrollPane();

    private AnchorPane anchor = new AnchorPane();

    private VBox contents = new VBox();

    /**
     * container - scrollpane - anchorpane - VBox(contents) - VBox(concert) -
     * for each band in concert: Borderpane with a Label (date and name)
     * For each borderpane: Text(Band) Text(Time) Text(contactinfo) Text(Sales) text(Link) text(review)
     */

    public void getConcertDetails(){

        container.getChildren().clear();

        Label instructionBoxLbl = new Label(INSTRUCTION_LABEL);
        instructionBoxLbl.setId("instructionBoxLabel");
        instructionBoxContainer.getChildren().add(instructionBoxLbl);

        ArrayList<String> bookedBands = prres.getBookedBands();

        int numVbox = bookedBands.size();
        System.out.println(numVbox);
        VBox[] vBoxes = new VBox[numVbox];

        for(int i = 0; i<bookedBands.size(); i++){
            VBox vb = new VBox();
            Label concertName = new Label("getConcName(concid)");
            vb.getChildren().add(concertName);

            Text bandName = new Text();
            bandName.setText("Navn");

            Text bandConcertTime = new Text();
            bandConcertTime.setText("Tidspunkt");

            Text bandConcertTicketSale = new Text();
            bandConcertTicketSale.setText("salgstall");

            Text bandContactinfo = new Text();
            bandContactinfo.setText("tlf og email");

            Text bandReview = new Text();
            bandReview.setText("omtale + link til omtale");

            BorderPane concertDetail = new BorderPane();
            concertDetail.setTop(bandName);
            concertDetail.setLeft(bandConcertTime);
            concertDetail.setCenter(bandContactinfo);
            concertDetail.setRight(bandConcertTicketSale);
            concertDetail.setBottom(bandReview);

            vb.getChildren().add(concertDetail);

            contents.getChildren().add(vb);
        }

        anchor.getChildren().add(contents);
        prScrollPane.setContent(anchor);
        container.getChildren().add(prScrollPane);
    }


    public void initialize(URL location, ResourceBundle resources) {
        prres = new PRres();
        System.out.println(prres.getBookedBands().size());
        getConcertDetails();

    }

}
