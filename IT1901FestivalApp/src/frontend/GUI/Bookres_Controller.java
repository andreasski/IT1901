package frontend.GUI;

/**
 * Hva den gjør..
 *
 * @autor Magnus Eriksson, Andreas Skifjeld
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import backend.Bookres;
import java.net.URL;
import java.util.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Bookres_Controller implements Initializable {

    private backend.Bookres BookingRes;
    private VBox searchContents = new VBox();
    private VBox techContents = new VBox();
    private VBox concContents = new VBox();
    private List<String> searchResults = new ArrayList<>();
    private ScrollPane searchScrollPane;
    private ScrollPane techNeedsScrollPane = new ScrollPane();
    private ScrollPane concertsScrollPane = new ScrollPane();
    private HBox info_techContainer = new HBox();

    @FXML
    private VBox container;

    @FXML
    private VBox aside;

    @FXML
    private VBox search;

    @FXML
    private ScrollPane genreScrollPane;

    public void navSearch() {
        container.getChildren().clear();
        Label headerSearch = new Label("Søk etter band");
        headerSearch.setId("headerScrollPane");
        TextField searchField = new TextField();
        searchField.setId("searchField");
        Button btnSearch = new Button("Søk");
        btnSearch.setId("btnSearch");
        HBox searchBarContainer = new HBox(searchField, btnSearch);
        search.getChildren().addAll(headerSearch, searchBarContainer);
        btnSearch.setOnMouseClicked(event -> showSearchResult(searchField.getText()));
        searchField.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent args) {
                showSearchResult(searchField.getText());
            }
        });
        List<String> genres = BookingRes.getGenre();
        VBox contents = new VBox();
        for (int i = 0; i < genres.size(); i++) {
            Label lblGenre = new Label(genres.get(i));
            lblGenre.getStyleClass().add("listItem" + ((i + 1) % 2));
            lblGenre.setOnMouseClicked(event -> navGenre(lblGenre.getText()));
            contents.getChildren().add(lblGenre);
        }
        genreScrollPane.setContent(contents);
    }

    public void navGenre(String genre) {
        container.getChildren().clear();
        search.getChildren().clear();
        VBox contents = new VBox();
        List<String> prevConcerts = BookingRes.getPubScene(genre);
        for (int i = 0; i < prevConcerts.size(); i++) {
            String[] prevConcDetails = prevConcerts.get(i).split(";");
            Label lblPrevConcStage = new Label("Scene: " + prevConcDetails[0]);
            Label lblPrevConcCap = new Label(("Kapasitet: " + prevConcDetails[1]));
            Label lblPrevConcAudience = new Label(("Publikummere: " + prevConcDetails[2]));
            AnchorPane prevConcContainer = new AnchorPane(lblPrevConcStage, lblPrevConcCap, lblPrevConcAudience);
            prevConcContainer.getStyleClass().add("margin");
            prevConcContainer.setLeftAnchor(lblPrevConcStage, 0.0);
            prevConcContainer.setTopAnchor(lblPrevConcStage, 0.0);
            prevConcContainer.setLeftAnchor(lblPrevConcCap, 0.0);
            prevConcContainer.setTopAnchor(lblPrevConcCap, 14.0);
            prevConcContainer.setLeftAnchor(lblPrevConcAudience, 0.0);
            prevConcContainer.setTopAnchor(lblPrevConcAudience, 28.0);
            prevConcContainer.setBottomAnchor(lblPrevConcAudience, 0.0);
            prevConcContainer.getStyleClass().add("listItem" + ((i + 1) % 2));
            contents.getChildren().add(prevConcContainer);
        }
        ScrollPane prevConcScrollPane = new ScrollPane(contents);
        prevConcScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        prevConcScrollPane.setId("scrollPane");
        Label prevConcHeader = new Label("Konserter i " + genre);
        prevConcHeader.setId("headerScrollPane");
        Label navSearch = new Label("Tilbake");
        navSearch.getStyleClass().add("underline");
        navSearch.setOnMouseClicked(event -> navSearch());
        container.getChildren().addAll(navSearch, prevConcHeader, prevConcScrollPane);

    }

    public void showSearchResult(String bandName) {
        searchContents.getChildren().clear();
        container.getChildren().clear();
        searchResults.clear();
        searchResults = BookingRes.searchBands(bandName);
        if (searchResults.size() == 0){
            searchContents.getChildren().add(new Label("Ingen treff på bandnavn"));
        } else{
            for (int i = 0; i < searchResults.size(); i++){
                Label lblResult = new Label(searchResults.get(i));
                lblResult.getStyleClass().add("listItem" + i % 2);
                lblResult.setOnMouseClicked(event -> bandInfo(lblResult.getText()));
                searchContents.getChildren().add(lblResult);
            }
        }
        searchScrollPane = new ScrollPane();
        searchScrollPane.setContent(searchContents);
        container.getChildren().add(searchScrollPane);
    }


    public void bandInfo(String band){
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
            Label ticPrice = new Label("Billett pris: " + concertDetails[4] + ",-");
            Label expenses = new Label("Utgifter: " + concertDetails[5] + ",-");
            Label revenue = new Label("Overskudd: " + ((Integer.parseInt(concertDetails[4]) * Integer.parseInt(concertDetails[3])) - Integer.parseInt(concertDetails[5])) + ",-");
            concertContainer.getChildren().addAll(concertName, stageName, date, audience, ticPrice, expenses, revenue);
            concertContainer.getStyleClass().add("concertOffer" + ((i + 1) % 2));
            concertContainer.setTopAnchor(concertName, 0.0);
            concertContainer.setLeftAnchor(concertName, 0.0);
            concertContainer.setTopAnchor(stageName, 14.0);
            concertContainer.setLeftAnchor(stageName, 0.0);
            concertContainer.setTopAnchor(date, 0.0);
            concertContainer.setRightAnchor(date, 14.0);
            concertContainer.setTopAnchor(audience, 28.0);
            concertContainer.setLeftAnchor(audience, 0.0);
            concertContainer.setTopAnchor(ticPrice, 42.0);
            concertContainer.setLeftAnchor(ticPrice, 0.0);
            concertContainer.setTopAnchor(expenses, 56.0);
            concertContainer.setLeftAnchor(expenses, 0.0);
            concertContainer.setTopAnchor(revenue, 70.0);
            concertContainer.setLeftAnchor(revenue, 0.0);
            concContents.getChildren().add(concertContainer);
        }
        concertsScrollPane.setContent(concContents);
        concertsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        concertsScrollPane.setId("concertsScrollPane");
        container.getChildren().addAll(concertsHeader, concertsScrollPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BookingRes = new Bookres();
        genreScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        navSearch();
    }
}
