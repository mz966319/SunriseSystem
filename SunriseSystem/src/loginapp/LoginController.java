package loginapp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import net.sf.jasperreports.engine.JasperReport;
import user.UserHomeController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dbUtil.dbTables;

public class LoginController implements Initializable {

    LoginModel loginModel = new LoginModel();

    @FXML private Label dbstatus;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<option> comboBox;
    @FXML private Button loginButton;
    @FXML private Label loginStatus;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (this.loginModel.isDatabaseConnected()){
            this.dbstatus.setText("Connected");
            this.dbstatus.setTextFill(Color.web("#2EFF00"));
        }
        else{
            this.dbstatus.setText("Disconnected");
            this.dbstatus.setTextFill(Color.web("#FF3333"));
        }

        this.comboBox.setItems(FXCollections.observableArrayList(option.values()));

        usernameField.setText("m");
        passwordField.setText("m");
        comboBox.getSelectionModel().select(1);

    }
    //login request method to check for login info
    @FXML public void Login(ActionEvent event) throws IOException{

        try {
            if (this.loginModel.isLogin(this.usernameField.getText().toString(),this.passwordField.getText().toString(),((option)this.comboBox.getValue()).toString())){
                Stage stage = (Stage) this.loginButton.getScene().getWindow();
                stage.close();
                switch (((option)this.comboBox.getValue()).toString()) {
                    case "Admin":
                        adminLogin();
                        break;
                    case "User":
                        userLogin(event);
                        break;
                }
            }
            else {
                this.loginStatus.setText("Wrong Credentials");

            }

        } catch (Exception e) {
            e.printStackTrace();
            this.loginStatus.setText("Entry missing");
        }

    }

    public void adminLogin(){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane) loader.load(getClass().getResource("../admin/adminHome.fxml").openStream());

            //UserHomeController userHomeController = (UserHomeController) loader.getController();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Store Home");
            stage.setResizable(false);
            stage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void userLogin(ActionEvent event){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane) loader.load(getClass().getResource("../user/userHomeFXML.fxml").openStream());

            //UserHomeController userHomeController = (UserHomeController) loader.getController();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Home");
            stage.setResizable(false);
            stage.show();


//            Parent parent = FXMLLoader.load(getClass().getResource("../user/userHomeFXML.fxml"));
//            Scene scene = new Scene(parent);
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(scene);
//            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
