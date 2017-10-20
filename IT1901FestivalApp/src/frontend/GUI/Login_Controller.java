package frontend.GUI;

import backend.Login;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Login_Controller implements Initializable {

    @FXML
    private VBox container;
    private Login login;

    public void navRegister() {

        container.getChildren().clear();

        Label text = new Label("Skriv inn nytt brukernavn, passord og hvilke(n) rolle(r) du har");
        Label name = new Label("Nytt Brukernavn");
        Label pwd = new Label("Passord:");
        Label rptpwd = new Label("Gjenta passord:");
        Label roles = new Label("Roller:");

        TextField username = new TextField();
        TextField password = new TextField();
        TextField repeatPassword = new TextField();

        String[] testerino = {"Tekniker", "Bookingansvarlig", "Manager", "Arrangør", "Bookingsjef"};

        Button register = new Button("Registrer deg");

        List<String> test = Arrays.asList(testerino);
        List<CheckBox> supertest = new ArrayList<>();

        CheckBox tech = new CheckBox("Tekniker");
        CheckBox booker = new CheckBox("Bookingansvarlig");
        CheckBox manager = new CheckBox("Manager");
        CheckBox organizer = new CheckBox("Arrangør");
        CheckBox bookerMan = new CheckBox("Bookingsjef");

        container.getChildren().addAll(text, name, username, pwd, password, rptpwd, repeatPassword, roles, tech, booker, manager, organizer, bookerMan, register);

        ArrayList<String> roleList = new ArrayList<>(Arrays.asList("1", "5"));

        register.setOnMouseClicked(event -> login.register(username.getText(), password.getText(), roleList));
       /* for (int i = 0; i < 5; i++) {
            supertest.add(new CheckBox(testerino.next()));
        } */


        //(exp) ? true : false;

    }

    public void navLogin(boolean success) {

        String info;

        if (success) {
             info = "Vennligst skriv inn brukernavn og passord";
        }
        else {
            info = "Feil brukernavn og/eller passord, vennligst prøv på nytt";
        }

        Label text = new Label(info);
        Label name = new Label("Brukernavn:");
        Label pwd = new Label("Passord:");

        TextField username = new TextField(); /* Lager tekstfelt */
        PasswordField password = new PasswordField(); /* Ditto */

        Button logIn = new Button("Logg inn"); /* Laget knapp, Logg inn */
        Button register = new Button("Registrer deg"); /* Skal sende deg til en side for registrering */

        container.getChildren().addAll(text, name, username, pwd, password, logIn, register);

        register.setOnMouseClicked(event -> navRegister());
        logIn.setOnMouseClicked(event -> login.checkLogin(username.getText(), password.getText())); /*  */

        /* if (!login.getSuccess()) {
            navLogin(false);
        } */


    }

    @java.lang.Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        login = new Login(false);
        navLogin(true);

        while (!login.getSuccess()) {
            navLogin(false);
        }

        List<String> roles = login.getRoleId();
        String role = login.getRoleId().iterator().next();
        Iterator iter = roles.iterator();
        iter.next();

    }
}