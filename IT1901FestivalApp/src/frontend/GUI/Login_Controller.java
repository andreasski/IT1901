package frontend.GUI;

import backend.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.LightBase;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class for login in containing the frontend functions and variables. Is called from the main function at the beginning
public class Login_Controller implements Initializable {

    // Private variables that are used in the functions of the class
    @FXML
    // Container that contains all the user interactive things like labels, buttons, textfields, etc.
    private VBox container;

    // A Login-class to connect to backend
    private Login login;

    // Label at the top of the window when logging in
    private Label loginText = new Label();

    // Label at the top of the window when registering
    private Label registerText = new Label();

    // A list of the role-ID's of the person logging in. Used in the navigation window
    private ArrayList<String> roleId;
    private final String[] fxmlRef = {"tec_landing", "bookres_landing", "man_landing", "arr_landing", "bob_landing"};
    private final String[] roleRef = {"Tekniker", "Booking ansvarlig", "Manager", "Arrangør", "Booking sjef"};


    // Function is called from function navLogin with function checkLogin from backend. If checkLogin returns true, it starts function navNav that lets the user navigate after login in.
    // If checkLogin returns false, it changes the label at the top to red and tells the user the name and / or password was wrong
    public void handleLogin(boolean success) {
        if (success) {
            navNav();
        } else {
            loginText.getStyleClass().add("red");
            loginText.setText("Feil brukernavn og/eller passord, vennligst prøv igjen");
        }
    }

    // Function is called from function navRegister and checks if all the textfields when registering contains what is asked for.
    // If false, it changes the labels to tell the user what is wrong, if true calls function handleRegister
    public void checkRegister(TextField username, PasswordField password, PasswordField repeatPassword, String mail, String phone, List<CheckBox> checkBoxes){

        // Creates an ArrayList for all the role-ID's, and adds the ID if the checkBox connected to that ID is selected. The role-ID's go from 1-5 with one number connected to one role
        roleId = new ArrayList<>();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                roleId.add("" + (i + 1));
            }
        }
        // If the password has a length less than 3, changes the label to red and tells the user to write longer passwords
        if (password.getText().length() < 3) {
            registerText.getStyleClass().add("red");
            registerText.setText("Passordet må være lengre!");
        }
        // Calls the function validatePhone to check if the phonenumber is correct. If not the label is changed to tell the user to write a number with 8 digits
        else if (!validatePhone(phone)) {
            registerText.getStyleClass().add("red");
            registerText.setText("Telefon nummer må bestå av åtte siffer");
        }
        // Calls the function validateMail to check if the e-mail has correct format. If not the label changes to tell the user hat format is expected
        else if (!validateMail(mail)) {
            registerText.getStyleClass().add("red");
            registerText.setText("Epost må være på formatet \"Ola123@mail.no\"");
        }
        // Checks if the checkboxes for roles have been selected. If no checkboxes have been signed, the label is changed to tell the user to select at least one role
        else if (!(roleId.size() > 0)) {
            registerText.getStyleClass().add("red");
            registerText.setText("Vennligst velg minst en rolle");
        }
        // Checks if the password and the repeated password are equal. If true, then the function handleRegister is called with function register from backend. The backend function will
        // check if the name is taken or not. If not, it adds the name, password, phone, email, and roles to the database and returns true to handleLogin
        else if (password.getText().equals(repeatPassword.getText())) {
            handleRegister(login.register(username.getText(), password.getText(), mail, phone, roleId));
        }
        // If the passwords are not equal the label is changed to tell the user to correct the passwords
        else {
            registerText.getStyleClass().add("red");
            registerText.setText("Passordene må være like!");
        }
    }


    // Function is called from checkRegister and checks if the phonenumber contains 8 digits and only digits. Returns false otherwise.
    public static boolean validatePhone(String number) {
        if (number.length() == 8) {
            for (int i = 0; i < number.length(); i++) {
                if (!Character.isDigit(number.charAt(i))) {
                     return false;
                }
            } return true;
        } return false;
    }


    // Function is called from checkRegister and checks if the email has the format expected from an email. Returns false otherwise.
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


    // Function is called from checkRegister with the function register from backend. if register returns true, the list of roles is assigned from the list of roles in backed,
    // and returns the user to the login-window. If register returns false, it means the name was already taken and the labels is updated to tell the user to choose another one.
    public void handleRegister(boolean reg) {
        if (reg) {
            roleId = login.getRoleId();
            navLogin();
        }
        else {
            registerText.getStyleClass().add("red");
            registerText.setText("Brukernavnet er allerede i bruk, vennligst velg et annet brukernavn:");
        }
    }

    // Function that is called from navLogin if the user pushes the register-button. Creates textfields and labels to enable the person to create a new user.
    public void navRegister() {

        // Clears the window to make room for new content where the user can register
        container.getChildren().clear();

        // Creates labels that helps the user to know what should be written in the textfields
        registerText.setText("Skriv inn nytt brukernavn, passord og hvilke(n) rolle(r) du har:");
        Label name = new Label("Nytt Brukernavn:");
        Label pwd = new Label("Passord:");
        Label rptpwd = new Label("Gjenta passord:");
        Label roles = new Label("Roller:");

        // Creates textfields where the user can write in username, email, phonenumber, password, and the repeat password.
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

        // Creates the buutons that initializes the register, or transports the user back to log in
        Button register = new Button("Registrer deg");
        Button btnLogin = new Button("Tilbake til logg inn");

        // If the button "back to log in" is clicked, the function navLogin is called which changes the window back to the log in.
        btnLogin.setOnAction(event -> navLogin());

        // Creates checkboxes for all the different roles. As a user can have several roles, more that one role can be checked.
        CheckBox tech = new CheckBox("Tekniker");
        CheckBox booker = new CheckBox("Bookingansvarlig");
        CheckBox manager = new CheckBox("Manager");
        CheckBox organizer = new CheckBox("Arrangør");
        CheckBox bookerMan = new CheckBox("Bookingsjef");
        CheckBox pr = new CheckBox("PR-Manager");
        CheckBox serving = new CheckBox("Servering");

        // adds the checkboxes to a list of checkboxes to make it easier to check if they are checked
        List<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(tech);
        checkBoxes.add(booker);
        checkBoxes.add(manager);
        checkBoxes.add(organizer);
        checkBoxes.add(bookerMan);
        checkBoxes.add(pr);
        checkBoxes.add(serving);

        // Creates two Vboxes that contains the labels and textfields, and the checkboxes- The Vboxes is added to a HBox while the buttons is added to another HBox
        VBox inpContainer = new VBox(name, usernameNav, pwd, passwordNav, rptpwd, repeatPasswordNav, lblMail, inpMail, lblPhone, inpPhone);
        VBox rolesContainer = new VBox(roles, tech, booker, manager, organizer, bookerMan, pr);
        rolesContainer.getStyleClass().add("margin");
        HBox regInfContainer = new HBox(inpContainer, rolesContainer);
        HBox btnContainer = new HBox(register, btnLogin);

        // The registerlabel that tells the user what to do (and updates if he does something wrong) and the HBoxes are added to the container
        container.getChildren().addAll(registerText, regInfContainer, btnContainer);

        // If the button "register" is clicked the function checkRegister is called that checks if all the input is done properly
        register.setOnMouseClicked(event -> checkRegister(usernameNav, passwordNav, repeatPasswordNav, inpMail.getText(), inpPhone.getText(), checkBoxes));
    }


    // Function that is called from main when the program starts. Helps the user login with a username and a password, or transports the user to a register page if needed
    public void navLogin() {

        // Clears the page for new content
        container.getChildren().clear();

        // Updates the login label to help the user to log in with username and password
        loginText.setText("Vennligst skriv inn brukernavn og passord:");

        // Labels that tell the user where to write the username and where to write the password
        Label name = new Label("Brukernavn:");
        Label pwd = new Label("Passord:");

        // Creates two textfields where the user can write their username and password to log in.
        TextField username = new TextField(); /* Lager tekstfelt */
        username.getStyleClass().add("textField");
        PasswordField password = new PasswordField(); /* Ditto */
        password.getStyleClass().add("textField");

        username.getStyleClass().add("inpDefault");
        password.getStyleClass().add("inpDefault");

        // Creates two buttons that either logs in the user or transports the user to the register window
        Button logIn = new Button("Logg inn"); /* Laget knapp, Logg inn */
        Button register = new Button("Registrer deg"); /* Skal sende deg til en side for registrering */

        // Creates a new HBox that adds the buttons
        HBox buttons = new HBox();
        buttons.getChildren().addAll(logIn, register);

        // Updates the visuals of the container and adds the labels, the textfields and the HBox
        container.setPadding(new Insets(15, 12, 15, 12));
        container.setSpacing(5);
        container.getChildren().addAll(loginText, name, username, pwd, password, buttons);

        // When the "register" button is clicked the function navRegister is called, which updates the window with the tools necessary to register a new user
        register.setOnMouseClicked(event -> navRegister());

        // When the button "log in" is clicked the function hadleLogin is called with the backend function checkLogin. If the username and password matches that in the
        // database the checkLogin returns true, if not it returns false.
        logIn.setOnMouseClicked(event -> handleLogin(login.checkLogin(username.getText(), password.getText())));
    }


    //Function is called from handleLogin if the username and password is accepted. The function updates the window to anable the user to navigate between the roles.
    public void navNav() {

        // Clears the window to make room for new content
        container.getChildren().clear();

        
        Map<String, String> fxmlRoleRef = new HashMap<>();
        for (int j = 0; j < fxmlRef.length; j++) { fxmlRoleRef.put(roleRef[j], fxmlRef[j]);}
        Label lblWelcome = new Label("Velkommen " + login.getUsername());
        Label lblLogout = new Label("Logg ut");
        lblLogout.getStyleClass().add("underline");
        lblLogout.setOnMouseClicked(event ->  navLogin());
        AnchorPane topBar = new AnchorPane(lblWelcome, lblLogout);
        topBar.setLeftAnchor(lblWelcome, 0.0);
        topBar.setRightAnchor(lblLogout, 28.0);
        container.getChildren().add(topBar);
        List<String> roles = login.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (fxmlRoleRef.containsKey(roles.get(i))) {
                final int ROLE_INDEX = i;
                Label lblRole = new Label(roles.get(i));
                lblRole.setOnMouseClicked(event -> {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/" + fxmlRoleRef.get(roles.get(ROLE_INDEX)) + ".fxml"));
                    Parent root = null;
                    try {
                        root = (Parent) fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (roles.get(ROLE_INDEX).equals("Tekniker")) {
                        Tec_Controller controller = fxmlLoader.<Tec_Controller>getController();
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                        controller.init(Integer.parseInt(login.getPersonId()));
                    } else if (roles.get(ROLE_INDEX).equals("Booking ansvarlig")) {
                        Bookres_Controller controller = fxmlLoader.<Bookres_Controller>getController();
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                    } else if (roles.get(ROLE_INDEX).equals("Manager")) {
                        Man_Controller controller = fxmlLoader.<Man_Controller>getController();
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                        controller.init(Integer.parseInt(login.getPersonId()));
                    } else if (roles.get(ROLE_INDEX).equals("Arrangør")) {
                        Arr_Controller controller = fxmlLoader.<Arr_Controller>getController();
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                    } else if (roles.get(ROLE_INDEX).equals("Booking sjef")) {
                        BoB_Controller controller = fxmlLoader.<BoB_Controller>getController();
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                    } else if (roles.get(ROLE_INDEX).equals("PR-Manager")) {
                        BoB_Controller controller = fxmlLoader.<BoB_Controller>getController();
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                    } else if (roles.get(ROLE_INDEX).equals("Servering")) {
                        BoB_Controller controller = fxmlLoader.<BoB_Controller>getController();
                        Scene scene = new Scene(root, 800, 600);
                        stage.setScene(scene);
                    }
                    stage.show();
                });
                lblRole.getStyleClass().add("btnNav");
                lblRole.setAlignment(Pos.CENTER);
                container.getChildren().add(lblRole);
            }

        }
    }

    @java.lang.Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        login = new Login();
        navLogin();

    }
}