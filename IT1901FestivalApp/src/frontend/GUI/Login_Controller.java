package frontend.GUI;

import backend.Login;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Login_Controller implements Initializable {

    @FXML
    private VBox container;
    private Login login;
    private Label loginText = new Label();
    private Label registerText = new Label();
    private String name;
    private String password;
    private String nameId;
    private ArrayList<String> roleId = new ArrayList<>();

    public void handleLogin(boolean success) {
        if (success) {
            //Nav
            name = login.getUsername();
            password = login.getPassword();
            nameId = login.getPersonId();
            roleId = login.getRoleId();

            System.out.print(name);
            System.out.print(password);
            System.out.print(nameId);
            System.out.print(roleId);
            }
        else {
            loginText.getStyleClass().add("red");
            loginText.setText("Feil brukernavn og/eller passord, vennligst prøv igjen");
        }
    }

    public void checkRegister(String username, String password, List<CheckBox> checkBoxes){

        int id = 1;
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                roleId.add(Integer.toString(id));
            }
            id++;
        }
        handleRegister(login.register(username, password, roleId));
    }

    public void handleRegister(boolean reg) {
        if (reg) {
            //Navigerer til navigasjonshub
            name = login.getUsername();
            password = login.getPassword();
            nameId = login.getPersonId();
            roleId = login.getRoleId();

            System.out.print(name);
            System.out.print(password);
            System.out.print(nameId);
            System.out.print(roleId);
        }
        else {
            registerText.getStyleClass().add("red");
            registerText.setText("Brukernavnet er allerede i bruk, vennligst velg et annet brukernavn:");
        }
    }

    public void navRegister() {

        container.getChildren().clear();
        //registerText.getStyleClass().add("black");

        registerText.setText("Skriv inn nytt brukernavn, passord og hvilke(n) rolle(r) du har:");
        Label name = new Label("Nytt Brukernavn");
        Label pwd = new Label("Passord:");
        Label rptpwd = new Label("Gjenta passord:");
        Label roles = new Label("Roller:");

        TextField username = new TextField();
        username.getStyleClass().add("textField");
        PasswordField password = new PasswordField();
        password.getStyleClass().add("textField");
        PasswordField repeatPassword = new PasswordField();
        repeatPassword.getStyleClass().add("textField");

        username.getStyleClass().add("inpDefault");
        password.getStyleClass().add("inpDefault");
        repeatPassword.getStyleClass().add("inpDefault");

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

        container.getChildren().addAll(registerText, name, username, pwd, password, rptpwd, repeatPassword, roles, tech, booker, manager, organizer, bookerMan, register);

        if (password.getText().length() < 3) {
            register.setOnMouseClicked(event -> registerText.getStyleClass().add("red"));
            register.setOnMouseClicked(event -> registerText.setText("Passordet må være lengre!"));
        }
        else if (password.getText().equals(repeatPassword.getText())) {
            register.setOnMouseClicked(event -> checkRegister(username.getText(), password.getText(), checkBoxes));
        }
        else {
            register.setOnMouseClicked(event -> registerText.getStyleClass().add("red"));
            register.setOnMouseClicked(event -> registerText.setText("Passordene må være like!"));
        }

    }

    public void navLogin() {

        loginText.setText("Vennligst skriv inn brukernavn og passord:");
        Label name = new Label("Brukernavn:");
        Label pwd = new Label("Passord:");

        TextField username = new TextField(); /* Lager tekstfelt */
        username.getStyleClass().add("textField");
        PasswordField password = new PasswordField(); /* Ditto */
        password.getStyleClass().add("textField");

        username.getStyleClass().add("inpDefault");
        password.getStyleClass().add("inpDefault");

        Button logIn = new Button("Logg inn"); /* Laget knapp, Logg inn */
        Button register = new Button("Registrer deg"); /* Skal sende deg til en side for registrering */

        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(logIn, register);

        container.setPadding(new Insets(15, 12, 15, 12));
        container.setSpacing(5);
        container.getChildren().addAll(loginText, name, username, pwd, password, buttons);

        register.setOnMouseClicked(event -> navRegister());
        logIn.setOnMouseClicked(event -> handleLogin(login.checkLogin(username.getText(), password.getText()))); /*  */

    }

    @java.lang.Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        login = new Login();
        navLogin();

    }
}