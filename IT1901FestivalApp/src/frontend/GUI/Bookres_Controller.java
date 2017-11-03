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
import javafx.scene.control.*;
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
    private Label lvl1, lvl2, spacing1_2;

    @FXML
    private Button btnBookingOffer;

    @FXML
    private VBox search;

    @FXML
    private ScrollPane genreScrollPane;

    public void navSearch() {
        container.getChildren().clear();
        search.getChildren().clear();
        spacing1_2.setText("");
        lvl2.setText("");
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
        spacing1_2.setText(" > ");
        lvl2.setText(genre);
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
        container.getChildren().addAll(prevConcHeader, prevConcScrollPane);
    }

    public void navBookingOffer() {
        container.getChildren().clear();
        search.getChildren().clear();
        spacing1_2.setText(" > ");
        lvl2.setText("Boooking tilbud");
        VBox contents = new VBox();

        Label headerBookOffer = new Label("Booking tilbud");
        headerBookOffer.setId("headerScrollPane");

        Label lblTime = new Label("Klokkeslett: (tt:mm-tt:mm)");
        TextField inpTime = new TextField();
        Label lblConcert = new Label("Konsert: ");
        TextField inpConcert = new TextField();
        Label lblBand = new Label("Band: ");
        TextField inpBand = new TextField();
        Label lblPrice = new Label("Pris: ");
        TextField inpPrice = new TextField();
        VBox inpContainer = new VBox(lblTime, inpTime, lblConcert, inpConcert, lblBand, inpBand, lblPrice, inpPrice);

        Label lblSearchInstruction = new Label("Huk av alternativet du vil søke etter");
        lblSearchInstruction.getStyleClass().add("margin");

        ToggleGroup searchVar = new ToggleGroup();
        RadioButton radConc = new RadioButton("Konserter  ");
        radConc.setSelected(true);
        RadioButton radBand = new RadioButton("Band");
        radConc.setToggleGroup(searchVar);
        radBand.setToggleGroup(searchVar);
        HBox radBtnContainer = new HBox(radConc, radBand);
        radBtnContainer.getStyleClass().add("leftMargin");

        TextField inpSearch = new TextField();
        inpSearch.setId("inpSearch");
        Button btnSearch = new Button("Søk");
        btnSearch.setId("btnSearch");
        HBox searchInpContainer = new HBox(inpSearch, btnSearch);
        searchInpContainer.getStyleClass().add("topMargin");

        ScrollPane searchResScrollPane = new ScrollPane();
        searchResScrollPane.setId("searchResScrollPane");
        searchResScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        searchResScrollPane.setContent(new VBox());

        btnSearch.setOnAction(event -> {
            List<String> contentsList = new ArrayList<>();
            VBox scrollPaneContents = new VBox();
            if (radBand.isSelected()) {
                contentsList = BookingRes.searchBands(inpSearch.getText());
                if (contentsList.size() > 0) {
                    for (int i = 0; i < contentsList.size(); i++) {
                        Label bandName = new Label(contentsList.get(i));
                        bandName.getStyleClass().add("listItemBO" + ((i + 1) % 2));
                        bandName.setOnMouseClicked(event1 -> inpBand.setText(bandName.getText()));
                        scrollPaneContents.getChildren().add(bandName);
                    }
                } else {
                    scrollPaneContents.getChildren().add(new Label("Ingen treff"));
                }
            } else {
                contentsList = BookingRes.searchConcerts(inpSearch.getText());
                if (contentsList.size() > 0) {
                    for (int i = 0; i < contentsList.size(); i++) {
                        Label concName = new Label(contentsList.get(i));
                        concName.getStyleClass().add("listItemBO" + ((i + 1) % 2));
                        concName.setOnMouseClicked(event1 -> inpConcert.setText(concName.getText()));
                        scrollPaneContents.getChildren().add(concName);
                    }
                } else {
                    scrollPaneContents.getChildren().add(new Label("Ingen treff"));
                }
            } searchResScrollPane.setContent(scrollPaneContents);
        });

        Label addRes = new Label("");
        addRes.getStyleClass().add("topMargin");

        Button btnAdd = new Button("Opprett tilbud");
        btnAdd.setOnAction(event -> {
            if (inpBand.getText().length() > 0 && inpConcert.getText().length() > 0) {
  //              if (validateTime(inpTime.getText())) {
                if (validatePrice(inpPrice.getText())) {
                    addRes.setText(BookingRes.addBookingOffer(inpTime.getText(), inpConcert.getText(), inpBand.getText(), Integer.parseInt(inpPrice.getText())));
                } else {
                    addRes.setText("Pris må være et positivt heltall");
                }
             //else {
               // addRes.setText("Feil format på tid");
            //}
        } else {
                addRes.setText("Vennligst fyll inn alle feltene");
            }

        });

        VBox searchContainer = new VBox(lblSearchInstruction, radBtnContainer, searchInpContainer, searchResScrollPane);
        searchContainer.setId("searchContainer");


        AnchorPane bookingOfferContainer = new AnchorPane(inpContainer, searchContainer, btnAdd, addRes);
        bookingOfferContainer.setLeftAnchor(inpContainer, 0.0);
        bookingOfferContainer.setTopAnchor(inpContainer, 0.0);
        bookingOfferContainer.setRightAnchor(searchContainer, 0.0);
        bookingOfferContainer.setTopAnchor(searchContainer, 0.0);
        bookingOfferContainer.setLeftAnchor(btnAdd, 0.0);
        bookingOfferContainer.setRightAnchor(btnAdd, 0.0);
        bookingOfferContainer.setBottomAnchor(btnAdd, 0.0);
        bookingOfferContainer.setLeftAnchor(addRes, 0.0);
        bookingOfferContainer.setBottomAnchor(addRes, 28.0);
        bookingOfferContainer.setId("bookingOfferContainer");

        contents.getChildren().addAll(headerBookOffer, bookingOfferContainer);
        container.getChildren().add(contents);
    }

    public boolean validatePrice(String price) {
        if (price.length() > 0) {
            for (int i = 0; i < price.length(); i++) {
                if (!Character.isDigit(price.charAt(i))) { return false;}
            } return Integer.parseInt(price) > 0;
        } return false;
    }

/*    public boolean validateTime(String time) {
        String[] splitDash = time.split("-");
        if (splitDash.length != 2) { return false;}
        for (int i = 0; i < 2; i++) {
            String[] splitColon = splitDash[i].split(":");
            if (splitColon.length != 2) { return false;}
            for (int j = 0; j < 2; j++) { if (!Character.isDigit(splitColon[0].charAt(j)) || !Character.isDigit(splitColon[1].charAt(j))) { return false;}}
        }
        int h1 = Integer.parseInt(splitDash[0].substring(0,2));
        int m1 = Integer.parseInt(splitDash[0].substring(3,5));
        int h2 = Integer.parseInt(splitDash[1].substring(0,2));
        int m2 = Integer.parseInt(splitDash[1].substring(3,5));
        return (h2 > h1) || (h2 == h1 && m2 > m1);
    }
*/
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
        lvl1.setOnMouseClicked(event -> navSearch());
        btnBookingOffer.setOnAction(event -> navBookingOffer());
        genreScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        navSearch();
    }
}
