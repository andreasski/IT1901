package frontend.GUI;

/**
 * Hva den gjør..
 *
 * @autor Magnus Eriksson
 */

import com.sun.xml.internal.ws.api.server.Container;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import backend.Bookingansvarlig;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * egenhjelp:
 * Bookingres skal ha tre nivåer med tilhørende metode:
 *
 *
 *
 *
 * showTechnicalNeeds: bruke Bookingansvarlig.getTechnicalNeeds(String band) for å få informasjon. Får da en ArrayList<String> i retur.
 *                      Vise disse tekniske behovene i en liste i GUI
 *
 * showKeyInformation: Bruke Bookingansvarlig.searchBands(String band) for å søke opp band. Hvis treff --> kan trykke på bandnavn
 *                      (nytt nivå) og få opp nøkkelinformasjon vha getBandInfo(String band) får da en String opp med keyInfo. Enkel
 *                      løsning er bare å vise denne strengen
 *
 *
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Bookres_Controller implements Initializable {

    private backend.Bookingansvarlig BookingRes;
    private VBox contents = new VBox();
    private ArrayList<String> searchResults = new ArrayList<String>();
    private String bandInfo = new String();

    private ScrollPane searchScrollPane;
    private ScrollPane bandScrollPane = new ScrollPane();
    private AnchorPane anchorPane;

    @FXML private VBox container;

    @FXML private TextField searchField;

    @FXML private Button btnSearch;

    @FXML
    private Label lvl1, lvl2, techNeed, prevConc, genInfo;


    public void searchBand() {
        String search = searchField.getText();
        btnSearch.setOnMouseClicked(event -> showSearchResult(search));
    }

    public void showSearchResult(String bandName) {
        contents.getChildren().clear();
        container.getChildren().clear();
        searchResults.clear();
        searchResults = BookingRes.searchBands(bandName);

        if (searchResults.size() == 0){
            contents.getChildren().add(new Label("Ingen treff på bandnavn"));
        }
        else{
            for (int i=0; i< searchResults.size();i++){
                Label lblResult = new Label(searchResults.get(i));
                lblResult.getStyleClass().add("listItem" + i % 2);
                lblResult.setOnMouseClicked(event -> bandInfo(lblResult.getText())); //eventhandler: ved trykk gå til neste nivå; bandInfo
                contents.getChildren().add(lblResult);
            }
        }
        searchScrollPane = new ScrollPane();
        searchScrollPane.setContent(contents);
        container.getChildren().add(searchScrollPane);
    }


    public void bandInfo(String bandName){
        //Header General info:
        contents.getChildren().clear();
        container.getChildren().clear();
        bandInfo = "";
        bandInfo = BookingRes.getInfoBand(bandName);
        genInfo = new Label(bandInfo);
        contents.getChildren().add(genInfo);

        container.getChildren().add(contents);
        showTechNeeds(bandName);
        showPrevConcert(bandName);


    }

    public void showTechNeeds(String band){
        ArrayList<String> techNeeds = BookingRes.getTechnicalNeeds(band);
        for(int i=0; i<techNeeds.size(); i++) {
            Label lblTechNeed = new Label(techNeeds.get(i));
            lblTechNeed.getStyleClass().add("ListItem" + i % 2);
            contents.getChildren().add(lblTechNeed);
        }

        bandScrollPane.setContent(contents);
        container.getChildren().add(bandScrollPane);
    }
    // Denne må endres. bookingansvarlig klassen returnerer kun en streng og ikke en liste.
    public void showPrevConcert(String BandName){
        ArrayList<String> prevConcert = BookingRes.getPreviousConcerts(BandName);
        for(int i=0; i<prevConcert.size(); i++) {
            Label lblPrevConc = new Label(prevConcert.get(i));
            lblPrevConc.getStyleClass().add("ListItem" + i % 2);
            contents.getChildren().add(lblPrevConc);
        }
        bandScrollPane.setContent(contents);
        container.getChildren().add(bandScrollPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BookingRes = new Bookingansvarlig();
        searchBand();
    }
}
