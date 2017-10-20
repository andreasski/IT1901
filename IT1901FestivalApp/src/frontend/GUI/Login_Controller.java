package frontend.GUI;

import backend.Login;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Login_Controller implements Initializable {

    @FXML
    private VBox container;
    private Login login;
    private Label loginText = new Label();
    private Label registerText = new Label();
    private ArrayList<String> roleId = new ArrayList<>();

    public void handleLogin(boolean success) {
        if (success) {
            //Nav
            }
        else {
            loginText.setText("Feil brukernavn og/eller passord, vennligst prøv igjen");
        }
    }

    public void handleRegister(boolean reg) {
        if (reg) {
            //Navigerer til navigasjonshub
            roleId = login.getRoleId();
        }
        else {
            registerText.setText("Brukernavnet er allerede i bruk, vennligst velg et annet brukernavn:");
        }
    }

    public void navRegister() {

        container.getChildren().clear();

        registerText.setText("Skriv inn nytt brukernavn, passord og hvilke(n) rolle(r) du har:");
        Label name = new Label("Nytt Brukernavn");
        Label pwd = new Label("Passord:");
        Label rptpwd = new Label("Gjenta passord:");
        Label roles = new Label("Roller:");

        TextField username = new TextField();
        PasswordField password = new PasswordField();
        PasswordField repeatPassword = new PasswordField();

        Button register = new Button("Registrer deg");

        CheckBox tech = new CheckBox("Tekniker");
        CheckBox booker = new CheckBox("Bookingansvarlig");
        CheckBox manager = new CheckBox("Manager");
        CheckBox organizer = new CheckBox("Arrangør");
        CheckBox bookerMan = new CheckBox("Bookingsjef");

        List<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(tech);
        checkBoxes.add(booker);
        checkBoxes.add(manager);
        checkBoxes.add(organizer);
        checkBoxes.add(bookerMan);

        int id = 1;
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                roleId.add(Integer.toString(id));
            }
            id++;
        }

        container.getChildren().addAll(registerText, name, username, pwd, password, rptpwd, repeatPassword, roles, tech, booker, manager, organizer, bookerMan, register);

        if (password.getText().equals(repeatPassword.getText()) && !password.getText().equals("")) {
            register.setOnMouseClicked(event -> handleRegister(login.register(username.getText(), password.getText(), roleId)));
        }
        else {
            register.setOnMouseClicked(event -> registerText.setText("Passordene må være like!"));
        }

    }

    public void navLogin() {

        loginText.setText("Vennligst skriv inn brukernavn og passord;");
        Label name = new Label("Brukernavn:");
        Label pwd = new Label("Passord:");

        TextField username = new TextField(); /* Lager tekstfelt */
        PasswordField password = new PasswordField(); /* Ditto */

        Button logIn = new Button("Logg inn"); /* Laget knapp, Logg inn */
        Button register = new Button("Registrer deg"); /* Skal sende deg til en side for registrering */

        container.getChildren().addAll(loginText, name, username, pwd, password, logIn, register);

        register.setOnMouseClicked(event -> navRegister());
        logIn.setOnMouseClicked(event -> handleLogin(login.checkLogin(username.getText(), password.getText()))); /*  */

    }

    @java.lang.Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        login = new Login();
        navLogin();

    }
}