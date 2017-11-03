package frontend.GUI;

import backend.Serving;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.*;


public class Serving_Controller implements Initializable {

    @FXML
    private VBox container;
    private ComboBox<String> comboBoxDates = null;
    private ObservableList<String> allDates = FXCollections.observableArrayList();
    private Serving serving;
    private ArrayList<String> concerts = null;
    private List<Button> buttons = null;

    public void handleConcerts(String date) {
        concerts = serving.getConcerts(date);
        buttons = null;

        for (int i = 0; i < concerts.size(); i++) {
            buttons.add(new Button(concerts.get(i)));
            container.getChildren().addAll(buttons);
        }
    }


    public void navInfo(String concert) {

        container.getChildren().clear();

        serving.getInfo(concert);

        ArrayList<String> times = serving.getTime();
        HashMap<String, ArrayList<String>> genre = serving.getGenre();
        int capacity = serving.getCapacity();
        int estAudience = serving.getEstAudience();
        HashMap<String, Integer> estBooze = serving.getEstBooze();

        String textTitle = "Informasjon om " + concert + ":";
        String textTime = "Konserten foregår i tidsrommet: ";
        String textGenre = "Her spiller ";
        String textCapacity = "Scenen har plass til: " + capacity + " mennesker.";
        String textBooze = "På konserten regnes det med å drikkes: ";

        for (int i = 0; i < times.size(); i++) {
            textTime += times.get(i);
            if (i == (times.size() - 1)) {
                textTime += ".";
            }
            else if (i == (times.size() - 2)) {
                textTime += " og ";
            }
            else {
                textTime += ", ";
            }
        }

        Set<String> keySet = genre.keySet();

        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {

            String band = it.next(); // it.next gir key verdi
            ArrayList<String> y = genre.get(band);
            textGenre += band + " som spiller innenfor sjanger ";

            for (int j = 0; j < y.size(); j++) {
                textGenre += y.get(j);

                if (j == ((y.size()) - 2)) {
                    textGenre += " og ";
                } else if (j < (y.size() - 2)) {
                    textGenre += ", ";
                }
            }
            textGenre += ".";
            if (it.hasNext()) {
                textGenre += " ";
            }
        }

        String olEst = Integer.toString(estBooze.get("Øl"));
        String vinEst = Integer.toString(estBooze.get("Vin"));
        String mineralEst = Integer.toString(estBooze.get("Mineralvann"));

        textBooze += "Øl: " + olEst + "L    Vin: " + vinEst + "L    Mineralvann" + mineralEst + "L.";

        Label title = new Label(textTitle);
        Label showTime = new Label(textTime);
        Label showGenre = new Label(textGenre);
        Label showCapacity = new Label(textCapacity);
        Label showBooze = new Label(textBooze);

        Button back = new Button("Tilbake");

        container.getChildren().addAll(title, showTime, showGenre, showCapacity, showBooze, back);

        back.setOnMouseClicked(event -> navServing());
    }


    public void navServing() {

        container.getChildren().clear();
        buttons = null;

        Label dato = new Label("Velg en dato:");

        ArrayList<String> addDates = serving.getAllDates();
        allDates.addAll(addDates);
        comboBoxDates.setItems(allDates);

        /*comboBoxDates.setCellFactory((comboBox) -> {
            return new ListCell<String>() {
                //@Override
                protected void upDateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
        });*/

        container.getChildren().addAll(dato, comboBoxDates);

        comboBoxDates.setOnAction((event) -> {
            String date = comboBoxDates.getSelectionModel().getSelectedItem();
            handleConcerts(date);
        });

        /*ChoiceBox<String> choiceBox = new ChoiceBox<>();

        choiceBox.getItems().addAll(allDates);

        container.getChildren().addAll(dato, choiceBox);

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number newValue) {
                String date = allDates.get(java.lang.Number.intValue(newValue));
                handleConcerts(date);
            }
        });*/

        for (int i = 0; i < buttons.size(); i++) {
            String concert = concerts.get(i);
            buttons.get(i).setOnAction(event -> navInfo(concert));
        }


    }




    @java.lang.Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        serving = new Serving();
        navServing();

    }
}
