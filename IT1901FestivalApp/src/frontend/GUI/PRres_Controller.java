package frontend.GUI;

import backend.PRres;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PRres_Controller implements Initializable {

    private final String INSTRUCTION_LABEL = "En herlig oversikt over konserter";

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


        // A ArrayList containing concertID of all booked Concert
        ArrayList<Integer> bookedConcerts = prres.getConcertIdBooked();


        // For each booked concert, create a VBox with Id = concertId, label it with concertname and add it to contents


        for(int concert : bookedConcerts){
            VBox vb = new VBox();
            Label concertName = new Label(prres.getConcertName(concert));
            concertName.setId("concertName");
            vb.setId("concertBox");
            vb.getChildren().add(concertName);
            ArrayList<Integer> bookedBands = prres.getBookedBands(concert);
            for(int band : bookedBands){
                vb.getChildren().add(addBandDetail(band, concert));
            }
            bookedBands.clear(); //rett pos
            contents.getChildren().add(vb);
        }

        anchor.getChildren().add(contents);
        prScrollPane.setContent(anchor);
        container.getChildren().add(prScrollPane);
    }

    //Creates a Borderpane with all the info requested
    public BorderPane addBandDetail(int bandid, int concertid){
        String details = prres.getBandDetails(bandid, concertid);
        List <String> detailsList = new ArrayList<>(Arrays.asList(details.split(";"))); //bandname, phone, email, sales, bio, link

        Text bandName = new Text();
        bandName.setText("Navn: " + detailsList.get(0));

        Text bandConcertTime = new Text();
        bandConcertTime.setText("2000");

        Text bandConcertTicketSale = new Text();
        bandConcertTicketSale.setText("Billetter solgt: " + detailsList.get(3));

        Text bandContactinfo = new Text();
        bandContactinfo.setText("Tlf: " + detailsList.get(1) +" \nmail: "+ detailsList.get(2) + "\n" + "Omtale: " + detailsList.get(4) + "\nPresseomtale: ");


        Hyperlink review = new Hyperlink(detailsList.get(5));

        bandConcertTicketSale.setId("infotext");
        bandContactinfo.setId("infotext");
        bandName.setId("infotext");
        review.setId("infotext");

        BorderPane concertDetail = new BorderPane();
        concertDetail.setTop(bandName);
        concertDetail.setLeft(bandContactinfo);
        //concertDetail.setCenter(bandConcertTime);
        concertDetail.setRight(bandConcertTicketSale);
        concertDetail.setBottom(review);

        concertDetail.setId("bandBox");
        return concertDetail;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prres = new PRres();
        getConcertDetails();

    }

}
