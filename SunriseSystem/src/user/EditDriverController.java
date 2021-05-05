package user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objects.Bus;
import objects.BusStudent;

import java.net.URL;
import java.util.ResourceBundle;

public class EditDriverController implements Initializable {

//    @FXML AnchorPane mainAnchorPane;
    @FXML TextField driverNameField;
    @FXML TextField phoneField;
    @FXML TextField busNumberField;
    @FXML TextField areaField;
    @FXML Button saveChangesButton;
    @FXML Button cancelButton;


    private Bus bus;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML public void saveButtonClicked(ActionEvent actionEvent){


        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();


    }
    @FXML public void cancelButtonClicked(ActionEvent actionEvent){


        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();


    }

    public void setData(Bus bus){
        this.bus=bus;
        driverNameField.setText(bus.getDriverName());
        phoneField.setText(bus.getDriverPhoneNumber());
        busNumberField.setText(String.valueOf(bus.getBusNumber()));
        areaField.setText(bus.getArea());


    }
}
