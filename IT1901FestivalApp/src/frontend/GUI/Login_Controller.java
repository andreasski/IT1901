package frontend.GUI;

import backend.Login;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.xml.soap.Text;
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
    private ArrayList<String> roleId;

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

    public void checkRegister(TextField username, PasswordField password, PasswordField repeatPassword, String mail, String phone, List<CheckBox> checkBoxes){
        roleId = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                roleId.add("" + (i + 1));
            }
        }

        if (password.getText().length() < 3) {
            registerText.getStyleClass().add("red");
            registerText.setText("Passordet må være lengre!");
        } else if (!validatePhone(phone)) {
            registerText.getStyleClass().add("red");
            registerText.setText("Telefon nummer må bestå av åtte siffer");
        } else if (!validateMail(mail)) {
            registerText.getStyleClass().add("red");
            registerText.setText("Epost må være på formatet \"Ola123@mail.no\"");
        } else if (!(roleId.size() > 0)) {
            registerText.getStyleClass().add("red");
            registerText.setText("Vennligst velg minst en rolle");
        } else if (password.getText().equals(repeatPassword.getText())) {
            handleRegister(login.register(username.getText(), password.getText(), mail, phone, roleId));
        } else {
            registerText.getStyleClass().add("red");
            registerText.setText("Passordene må være like!");
        }
    }

    public static boolean validatePhone(String number) {
        if (number.length() == 8) {
            for (int i = 0; i < number.length(); i++) {
                if (!Character.isDigit(number.charAt(i))) {
                     return false;
                }
            } return true;
        } return false;
    }

    public static boolean validateMail(String mail) {
        String[] splitAt = mail.split("@"); // asd lel.no
        String[] splitDot;
        if (splitAt.length == 2) {
            splitDot = splitAt[1].split("\\."); // lel no
            for (int i = 0; i < splitAt[0].length(); i++) {
                if (!Character.isDigit(splitAt[0].charAt(i)) && !Character.isLetter(splitAt[0].charAt(i))) {
                    return false;
                }
            }
        } else {
            return false;
        }


        if (splitDot.length == 2) {
            for (int j = 0; j < splitDot[0].length(); j++) {
                if (!Character.isLetter(splitDot[0].charAt(j))) {
                    return false;
                }
            }
            for (int k = 0; k < splitDot[1].length(); k++) {
                if (!Character.isLetter(splitDot[1].charAt(k))) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public void handleRegister(boolean reg) {
        if (reg) {
            //Navigerer til navigasjonshub
            name = login.getUsername();
            password = login.getPassword();
            nameId = login.getPersonId();
            roleId = login.getRoleId();
            navLogin();
        }
        else {
            registerText.getStyleClass().add("red");
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

        TextField usernameNav = new TextField();
        usernameNav.getStyleClass().add("textField");
        PasswordField passwordNav = new PasswordField();
        passwordNav.getStyleClass().add("textField");
        PasswordField repeatPasswordNav = new PasswordField();
        repeatPasswordNav.getStyleClass().add("textField");

        usernameNav.getStyleClass().add("inpDefault");
        passwordNav.getStyleClass().add("inpDefault");
        repeatPasswordNav.getStyleClass().add("inpDefault");

        Label lblMail = new Label("Epost: ");
        TextField inpMail = new TextField();
        Label lblPhone = new Label("Telefon: ");
        TextField inpPhone = new TextField();

        inpMail.getStyleClass().addAll("textField", "inpDefault");
        inpPhone.getStyleClass().addAll("textField", "inpDefault");

        Button register = new Button("Registrer deg");
        Button btnLogin = new Button("Tilbake til logg inn");
        btnLogin.setOnAction(event -> navLogin());

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

        VBox inpContainer = new VBox(name, usernameNav, pwd, passwordNav, rptpwd, repeatPasswordNav, lblMail, inpMail, lblPhone, inpPhone);
        VBox rolesContainer = new VBox(roles, tech, booker, manager, organizer, bookerMan);
        rolesContainer.getStyleClass().add("margin");
        HBox regInfContainer = new HBox(inpContainer, rolesContainer);
        HBox btnContainer = new HBox(register, btnLogin);

        container.getChildren().addAll(registerText, regInfContainer, btnContainer);

        register.setOnMouseClicked(event -> checkRegister(usernameNav, passwordNav, repeatPasswordNav, inpMail.getText(), inpPhone.getText(), checkBoxes));
    }

    public void navLogin() {

        container.getChildren().clear();

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

        HBox buttons = new HBox();
        buttons.getChildren().addAll(logIn, register);

        container.setPadding(new Insets(15, 12, 15, 12));
        container.setSpacing(5);
        container.getChildren().addAll(loginText, name, username, pwd, password, buttons);

        register.setOnMouseClicked(event -> navRegister());
        logIn.setOnMouseClicked(event -> handleLogin(login.checkLogin(username.getText(), password.getText())));

    }

    @java.lang.Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        login = new Login();
        navLogin();

    }
}