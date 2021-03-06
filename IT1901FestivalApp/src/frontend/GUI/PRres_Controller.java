package frontend.GUI;

/**
 * PRres_Controller handles user interaction with the GUI.
 * @author Magnus Eriksson
 * @version 1.0
 * @since 28.10.2017
 */

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

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PRres_Controller implements Initializable {

    private final String INSTRUCTION_LABEL = "En herlig oversikt over konserter";

    @FXML
    private VBox instructionBoxContainer;

    @FXML
    private VBox container;

    private ScrollPane prScrollPane = new ScrollPane();

    private AnchorPane anchor = new AnchorPane();

    private VBox contents = new VBox();

    private backend.PRres prres;

    /**
     * Calls backend.PRres.getConcertIdBooked to get id for the booked concerts.
     * Creates a Vbox for each booked concert, calls addBandDetail to create a BorderPane with
     * information about each band for the concert. Calls getBookedBands(int) to get bandid for all the booked bands
     * @see #addBandDetail(int, int)
     * @see backend.PRres#getBookedBands(int)
     * @see backend.PRres#getConcertName(int)
     */
    public void getConcertDetails(){

        container.getChildren().clear();

        Label instructionBoxLbl = new Label(INSTRUCTION_LABEL);
        instructionBoxLbl.setId("instructionBoxLabel");
        instructionBoxContainer.getChildren().add(instructionBoxLbl);

        // A ArrayList containing concertID of all booked Concert
        ArrayList<Integer> bookedConcerts = prres.getConcertIdBooked();

        // For each booked concert, create a VBox with Id = concertId, label it with concertname. Cre and add it to contents
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
            bookedBands.clear();
            contents.getChildren().add(vb);
        }

        anchor.getChildren().add(contents);
        prScrollPane.setContent(anchor);
        container.getChildren().add(prScrollPane);
    }

    /**
     *  This method creates a BorderPane with information of bandname, phone, email, sales, bio, link which match the bandid and concertid
     * @param bandid band id
     * @param concertid concert id
     * @return BorderPane concertDetail
     * @see backend.PRres#getBandDetails(int, int)
     */
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
