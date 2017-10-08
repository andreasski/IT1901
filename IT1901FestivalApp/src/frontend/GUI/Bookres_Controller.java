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
import java.util.*;

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
    private VBox searchContents = new VBox();
    private VBox techContents = new VBox();
    private VBox concContents = new VBox();
    private List<String> searchResults = new ArrayList<>();
    private ScrollPane searchScrollPane;
    private ScrollPane techNeedsScrollPane = new ScrollPane();
    private ScrollPane concertsScrollPane = new ScrollPane();
    private HBox info_techContainer = new HBox();

    @FXML private VBox container;

    @FXML private TextField searchField;

    @FXML private Button btnSearch;


    public void searchBand() {
        String search = searchField.getText();
        btnSearch.setOnMouseClicked(event -> showSearchResult(search));
    }

    public void showSearchResult(String bandName) {
        searchContents.getChildren().clear();
        container.getChildren().clear();
        searchResults.clear();
        searchResults = BookingRes.searchBands(bandName);

        if (searchResults.size() == 0){
            searchContents.getChildren().add(new Label("Ingen treff på bandnavn"));
        }
        else{
            for (int i=0; i< searchResults.size();i++){
                Label lblResult = new Label(searchResults.get(i));
                lblResult.getStyleClass().add("listItem" + i % 2);
                lblResult.setOnMouseClicked(event -> bandInfo(lblResult.getText())); //eventhandler: ved trykk gå til neste nivå; bandInfo
                searchContents.getChildren().add(lblResult);
            }
        }
        searchScrollPane = new ScrollPane();
        searchScrollPane.setContent(searchContents);
        container.getChildren().add(searchScrollPane);
    }


    public void bandInfo(String band){
        //Header General info:

        container.getChildren().clear();
        info_techContainer.getChildren().clear();
        String[] bandInfo = BookingRes.getInfoBand(band).split(";");
        AnchorPane bandInfoContainer = new AnchorPane();
        bandInfoContainer.setId("bandDetails");

        Label bandName = new Label("Band: " + band);
        Label streamingPop = new Label("Streaming popularitet: " + bandInfo[0]);
        Label albumSales = new Label("Album salg: " + bandInfo[1]);
        Label concertSales = new Label("Konsert billett salg: " + bandInfo[2]);

        bandInfoContainer.getChildren().addAll(bandName, streamingPop, albumSales, concertSales);

        bandInfoContainer.setTopAnchor(bandName, 0.0);
        bandInfoContainer.setLeftAnchor(bandName, 0.0);
        bandInfoContainer.setTopAnchor(streamingPop, 14.0);
        bandInfoContainer.setLeftAnchor(streamingPop, 0.0);
        bandInfoContainer.setTopAnchor(albumSales, 28.0);
        bandInfoContainer.setLeftAnchor(albumSales, 0.0);
        bandInfoContainer.setTopAnchor(concertSales, 42.0);
        bandInfoContainer.setLeftAnchor(concertSales, 0.0);
        info_techContainer.getChildren().add(bandInfoContainer);

        showTechNeeds(band);
        container.getChildren().add(info_techContainer);
        showPrevConcert(band);

    }

    public void showTechNeeds(String band){
        techContents.getChildren().clear();
        ArrayList<String> techNeeds = BookingRes.getTechnicalNeeds(band);
        VBox techNeedsContainer = new VBox();
        Label techNeedHeader = new Label("Tekniske behov");
        techNeedHeader.setId("headerScrollPaneAside");
        for (int i = 0; i < techNeeds.size(); i++) {
            Label lblTechNeed = new Label(techNeeds.get(i));
            lblTechNeed.getStyleClass().add("listItemAside" + ((i + 1) % 2));
            techContents.getChildren().add(lblTechNeed);
        }

        techNeedsScrollPane.setContent(techContents);
        techNeedsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        techNeedsScrollPane.setId("techNeedsScrollPane");
        techNeedsContainer.getChildren().addAll(techNeedHeader, techNeedsScrollPane);
        info_techContainer.getChildren().add(techNeedsContainer);
    }
    // Denne må endres. bookingansvarlig klassen returnerer kun en streng og ikke en liste.
    public void showPrevConcert(String BandName){
        concContents.getChildren().clear();
        ArrayList<String> prevConcert = BookingRes.getPreviousConcerts(BandName);
        Label concertsHeader = new Label("Konserter");
        concertsHeader.setId("headerScrollPane");
        for(int i=0; i < prevConcert.size(); i++) {
            AnchorPane concertContainer = new AnchorPane();
            String[] concertDetails = prevConcert.get(i).split(";");
            Label concertName = new Label(concertDetails[0]);
            Label stageName = new Label("Scene: " + concertDetails[1]);
            Label date = new Label("Dato: " + concertDetails[2]);
            Label audience = new Label("Publikum: " + concertDetails[3]);

            concertContainer.getChildren().addAll(concertName, stageName, date, audience);
            concertContainer.getStyleClass().add("concertOffer" + ((i + 1) % 2));

            concertContainer.setTopAnchor(concertName, 0.0);
            concertContainer.setLeftAnchor(concertName, 0.0);
            concertContainer.setTopAnchor(stageName, 14.0);
            concertContainer.setLeftAnchor(stageName, 0.0);
            concertContainer.setTopAnchor(date, 0.0);
            concertContainer.setRightAnchor(date, 14.0);
            concertContainer.setTopAnchor(audience, 28.0);
            concertContainer.setLeftAnchor(audience, 0.0);

            concContents.getChildren().add(concertContainer);
        }
        concertsScrollPane.setContent(concContents);
        concertsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        concertsScrollPane.setId("concertsScrollPane");
        container.getChildren().addAll(concertsHeader, concertsScrollPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BookingRes = new Bookingansvarlig();
        searchBand();
    }
}
