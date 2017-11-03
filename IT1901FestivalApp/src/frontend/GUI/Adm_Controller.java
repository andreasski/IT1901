package frontend.GUI;

import backend.Admin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class Adm_Controller implements Initializable{

  Admin admin = new Admin();

  @FXML
  VBox container;

  public void navLanding() {
    container.getChildren().clear();
    Map<String, List<String>> users = admin.getUsers();
    Label lblHeader = new Label("Bruker forespørsler");
    lblHeader.setId("headerScrollPane");
    VBox contents = new VBox();
    contents.setId("contents");
    List<String> usersLi = new ArrayList<>(users.keySet());
    Collections.sort(usersLi);
    for (int i = 0; i < usersLi.size(); i++) {
      Label lblName = new Label(usersLi.get(i));
      String roles = "";
      for (int j = 0; j < users.get(usersLi.get(i)).size(); j++) {
        roles += " " + users.get(usersLi.get(i)).get(j);
      }
      Label lblRoles = new Label("Roller:" + roles);
      Button btnAccept = new Button("Godkjenn");
      btnAccept.setOnAction(event -> {
        admin.handleUser(1, lblName.getText());
        navLanding();
      });
      btnAccept.getStyleClass().add("btnHandleUser");
      Button btnReject = new Button("Avvis");
      btnReject.setOnAction(event -> {
        admin.handleUser(-1, lblName.getText());
        navLanding();
      });
      btnReject.getStyleClass().add("btnHandleUser");
      AnchorPane userContainer = new AnchorPane(lblName, lblRoles, btnAccept, btnReject);
      userContainer.setLeftAnchor(lblName, 14.0);
      userContainer.setTopAnchor(lblName, 14.0);
      userContainer.setLeftAnchor(lblRoles, 14.0);
      userContainer.setTopAnchor(lblRoles, 28.0);
      userContainer.setLeftAnchor(btnAccept, 0.0);
      userContainer.setTopAnchor(btnAccept, 60.0);
      userContainer.setLeftAnchor(btnReject, 250.0);
      userContainer.setTopAnchor(btnReject, 60.0);
      userContainer.getStyleClass().add("listItem" + ((i + 1) % 2));
      contents.getChildren().add(userContainer);

    }
    ScrollPane scrollPane = new ScrollPane(contents);
    scrollPane.setId("scrollPane");
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    container.getChildren().addAll(lblHeader, scrollPane);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    navLanding();
  }
}
