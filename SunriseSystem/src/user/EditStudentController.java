package user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import objects.BusStudent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditStudentController implements Initializable {

    UserHomeModel userHomeModel = new UserHomeModel();


    @FXML private TextField studentName;
    @FXML private TextField fatherPhone;
    @FXML private TextField motherPhone;
    @FXML private TextField grade;
    @FXML private TextField path;
    @FXML private ComboBox busNumber;
    @FXML private ComboBox subscription;
    @FXML private Button saveChangesButton;
    @FXML private Button cancelEditButton;

    int busIndex,subscriptionIndex;






    private BusStudent busStudent;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setBusNumbersComboBox();
        subscription.getItems().add("ذهاب فقط");
        subscription.getItems().add("عودة فقط");
        subscription.getItems().add("ذهاب و عودة");
        subscription.getItems().add("اشتراك ملغي");

        //TODO: add a confirmation before saving changes


//        Stage stage = (Stage) this.saveChangesButton.getScene().getWindow();
//        busStudent = (BusStudent) stage.getUserData();
//        studentName.setText(busStudent.getName());



    }
    @FXML private void cancelEditButtonClicked(ActionEvent actionEvent){
        Stage stage = (Stage) this.cancelEditButton.getScene().getWindow();
        stage.close();

    }

    @FXML private void saveChangesButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        String error="";
        boolean[] checkChanges = new boolean[7];
        Arrays.fill(checkChanges,false);
        if (!busStudent.getName().equals(studentName.getText().toString())) {
            error +=  studentName.getText().toString()+"-اسم الطالب: " ;
            error += "\n";
            checkChanges[0]=true;
        }
        if (!busStudent.getFatherPhone().equals(fatherPhone.getText().toString())) {
            error += fatherPhone.getText().toString()+"-رقم هاتف الاب: " ;
            error += "\n";
            checkChanges[1]=true;
        }
        if (!busStudent.getMotherPhone().equals(motherPhone.getText().toString())) {
            error += motherPhone.getText().toString()+"-رقم هاتف الام: " ;
            error += "\n";
            checkChanges[2]=true;
        }

        if (!busStudent.getGrade().equals(grade.getText().toString())) {
            error +=  grade.getText().toString()+"-الصف: ";
            error += "\n";
            checkChanges[3]=true;
        }

        if (Integer.parseInt( busNumber.getValue().toString()) != busStudent.getBusNumber()) {
            error +=  busNumber.getValue().toString()+"-رقم الباص: ";
            error += "\n";
            checkChanges[4]=true;
        }

        if (!busStudent.getPath().equals(path.getText().toString())) {
            error += path.getText().toString()+"-المسار: ";
            error += "\n";
            checkChanges[5]=true;
        }

        if (!busStudent.getSubscription().equals(subscription.getValue().toString())) {
            error +=  subscription.getValue().toString()+"-نوع الاشتراك: " ;
            error += "\n";
            checkChanges[6]=true;
        }


        Alert alert = new Alert(Alert.AlertType.INFORMATION);  //CONFIRMATION
        alert.setTitle("التعديلات");
        alert.setHeaderText("أكد التعديلات التالية");
        alert.setContentText(error);

        ButtonType buttonTypeCancel = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeOne = new ButtonType("حفظ التعديلات");
        //ButtonType buttonTypeTwo = new ButtonType("Back");

        alert.getButtonTypes().setAll(buttonTypeCancel,buttonTypeOne);

        Optional<ButtonType> result = alert.showAndWait();

        //store information of the row

        if (result.get() == buttonTypeOne){

            if (!studentName.getText().equals(busStudent.getName())){
                userHomeModel.updateStudentName(busStudent.getStudentID(),studentName.getText());
            }
            if (!fatherPhone.getText().equals(busStudent.getFatherPhone())){
                userHomeModel.updateStudentFatherPhone(busStudent.getStudentID(),fatherPhone.getText());
            }
            if (!motherPhone.getText().equals(busStudent.getMotherPhone())){
                userHomeModel.updateStudentMotherPhone(busStudent.getStudentID(),motherPhone.getText());
            }
            if (!grade.getText().equals(busStudent.getGrade())){
                userHomeModel.updateStudentGrade(busStudent.getStudentID(),grade.getText());
            }
            if (!path.getText().equals(busStudent.getPath())){
                userHomeModel.updateStudentPath(busStudent.getStudentID(),path.getText());

            }
            if (!subscription.getValue().toString().equals(busStudent.getSubscription())){
                userHomeModel.updateStudentSubscription(busStudent.getStudentID(),subscription.getValue().toString());

            }
            if (!busNumber.getValue().toString().equals(busStudent.getBusNumber())){
                userHomeModel.updateStudentBusNumber(busStudent.getStudentID(),busNumber.getValue().toString());

            }
            //Stage stage = (Stage) this.saveChangesButton.getScene().getWindow();
            //stage.close();
            ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();


        }else {
            // ... user chose CANCEL or closed the dialog
        }


    }


    public void setData(BusStudent busStudent){
        this.busStudent=busStudent;
        studentName.setText(busStudent.getName());
        fatherPhone.setText(busStudent.getFatherPhone());
        motherPhone.setText(busStudent.getMotherPhone());
        grade.setText(busStudent.getGrade());
        path.setText(busStudent.getPath());
        busNumber.getSelectionModel().select(userHomeModel.getBusNumbers().indexOf(busStudent.getBusNumber()));
        if (busStudent.getSubscription().equals("ذهاب فقط"))
            subscriptionIndex=0;
        else if (busStudent.getSubscription().equals("عودة فقط"))
            subscriptionIndex =1;
        else if (busStudent.getSubscription().equals("ذهاب و عودة"))
            subscriptionIndex =2;
        else
            subscriptionIndex=3;
        subscription.getSelectionModel().select(subscriptionIndex);

    }

    //set the bus numbers for the bus number combo box
    private void setBusNumbersComboBox(){
        ArrayList<Integer> numbers =userHomeModel.getBusNumbers();
        busNumber.getItems().clear();
        for (int i=0; i<numbers.size();i++) {
            busNumber.getItems().add(numbers.get(i));
        }
    }
}
