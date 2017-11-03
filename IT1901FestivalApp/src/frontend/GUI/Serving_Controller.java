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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Serving_Controller implements Initializable {

    @FXML
    private VBox container;
    private ComboBox<String> comboBoxDates;
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


    public navInfo(String concert) {

        serving.getInfo(concert);

        ArrayList<String> times = serving.getTime();
        HashMap<String, String> genre = serving.getGenre();
        int capacity = serving.getCapacity();
        int estAudience = serving.getEstAudience();
        HashMap<String, Integer> estBooze = serving.getEstBooze();

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
            buttons.get(i).setOnAction((event) -> {
                navInfo(concerts.get(i));
            });
        }

    }




    @java.lang.Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        serving = new Serving();
        navServing();

    }
}
