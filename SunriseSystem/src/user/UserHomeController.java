package user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import objects.*;
import dbUtil.dbConnection;


import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class UserHomeController implements Initializable {

    private static int tableFilter =0;
    private  int updateList =0;




    UserHomeModel userHomeModel = new UserHomeModel();
    ArrayList<Integer> busNumbersList; //contains a list of all bus numbers
    ArrayList<String>subscriptionList;


    //students tab
    @FXML private TextField studentName;
    @FXML private TextField studentFatherPhone;
    @FXML private TextField studentMotherPhone;
    @FXML private TextField studentGrade;
    @FXML private ComboBox studentBusNumber;
    @FXML private TextField studentPath;
    @FXML private ComboBox studentSubscription;
    @FXML private VBox addStudentVBox;
    @FXML private VBox searchVBox;
    private boolean addStudentSectionActive;

    private boolean searchSectionActive;



    @FXML private TableView<BusStudent> studentTableView;
    @FXML private TableColumn<BusStudent,String> studentNameColumn;
    @FXML private TableColumn<BusStudent,String> studentFatherPhoneColumn;
    @FXML private TableColumn<BusStudent,String> studentMotherPhoneColumn;
    @FXML private TableColumn<BusStudent,String> studentGradeColumn;
    @FXML private TableColumn<BusStudent,String> studentBusNumberColumn;
    @FXML private TableColumn<BusStudent,String> studentPathColumn;
    @FXML private TableColumn<BusStudent,String> studentIDColumn;  //TODO: maybe int?
    @FXML private TableColumn<BusStudent,String> studentSubscriptionColumn;


    //drivers tab
    @FXML private TextField driverName;
    @FXML private TextField driverPhone;
    @FXML private TextField driverBusNumber;
    @FXML private TextField driverArea;

    @FXML private TableView<Bus> driverTableView;
    @FXML private TableColumn<Bus,String> driverTableIDColumn;
    @FXML private TableColumn<Bus,String> driverNameColumn;
    @FXML private TableColumn<Bus,String> driverBusNumberColumn;
    @FXML private TableColumn<Bus,String> driverPhoneColumn;
    @FXML private TableColumn<Bus,String> driverAreaColumn;


//    BusStudent busStudent;

    private  dbConnection db;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addStudentSectionActive = false;
        addStudentVBox.setVisible(false);
        addStudentVBox.setDisable(true);
        addStudentVBox.setPrefHeight(1);

        searchSectionActive=false;
        searchVBox.setVisible(false);
        searchVBox.setDisable(true);
        searchVBox.setPrefHeight(1);




        //TODO: change bus numbers and subscription to a list
        setBusNumbersComboBox();
        subscriptionList=new ArrayList<String>();
        subscriptionList.add("ذهاب فقط");
        subscriptionList.add("عودة فقط");
        subscriptionList.add("ذهاب و عودة");
        subscriptionList.add("اشتراك ملغي");
        for(int i=0;i<subscriptionList.size();i++){
            studentSubscription.getItems().add(subscriptionList.get(i));
        }



        //set students table view columns
        studentIDColumn.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentFatherPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("fatherPhone"));
        studentMotherPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("motherPhone"));
        studentGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        studentBusNumberColumn.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        studentPathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        studentSubscriptionColumn.setCellValueFactory(new PropertyValueFactory<>("subscription"));

        showStudentTableData();



        //set driver table view columns
        driverTableIDColumn.setCellValueFactory(new PropertyValueFactory<>("busID"));
        driverNameColumn.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        driverBusNumberColumn.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        driverPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("driverPhoneNumber"));
        driverAreaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        driverTableView.setItems(userHomeModel.getAllDrivers());
    }

    @FXML private void createPDFFile(ActionEvent actionEvent) throws FileNotFoundException, JRException {
        /* Convert List to JRBeanCollectionDataSource */

        /* Map to hold Jasper report Parameters */
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("SCHOOL_NAME", "Sunrise");

        ArrayList<BusStudent> list=new ArrayList<BusStudent>();
             for (int i=0;i<50;i++){
                 list.add(new BusStudent(1, "name","fatherPhone","motherPhone", "grade",  1,  1,  "path",  "subscription"));
             }


            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);//userHomeModel.getAllStudents()

        //read jrxml file and creating jasperdesign object
        InputStream input = new FileInputStream(new File("C:\\Users\\moaaz mahmoud\\JaspersoftWorkspace\\SunriseSystem\\Students_Bus_List_A4.jrxml"));

        JasperDesign jasperDesign = JRXmlLoader.load(input);

        /*compiling jrxml with help of JasperReport class*/
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        /* Using jasperReport object to generate PDF */
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        /*call jasper engine to display report in jasperviewer window*/
        JasperViewer.viewReport(jasperPrint);

    }

    private void hideAddStudentSection(){
        addStudentVBox.setVisible(false);
        addStudentVBox.setDisable(true);
        addStudentVBox.setPrefHeight(20);
        addStudentSectionActive=false;
    }
    @FXML private void hideAddStudentSectionButton(ActionEvent actionEvent){
        hideAddStudentSection();
    }
    //add a new student to the database
    @FXML private void addNewStudent(ActionEvent actionEvent) throws SQLException{

        if(!addStudentSectionActive) {
            addStudentVBox.setVisible(true);
            addStudentVBox.setDisable(false);
            addStudentVBox.setPrefHeight(310);
            addStudentSectionActive=true;
            hideSearchSection();
        }
        else{//(addStudentSectionActive) {//TODO: replace it with ELSE

            String name = studentName.getText().toString();
            String motherPhone = studentMotherPhone.getText().toString();
            String fatherPhone = studentFatherPhone.getText().toString();
            String grade = studentGrade.getText().toString();
            String path = studentPath.getText().toString();
            String subscription = studentSubscription.getValue().toString();
            int busNumber = -1;
            String error = "";
            if (name.equals("")) {
                error += "- Student name\n";
            }
            if (motherPhone.equals("")) {
                error += "- Mother's Phone number\n";
            }
            if (fatherPhone.equals("")) {
                error += "- Father's Phone number\n";
            }
            if (grade.equals("")) {
                error += "- Grade\n";
            }
            if (path.equals("")) {
                error += "- Path\n";
            }
            if (subscription.equals("")) {
                error += "- Subscription\n";
            }
            if (studentBusNumber.getValue().toString().equals("")) {
                error += "- Bus number";
            } else {
                busNumber = Integer.parseInt(studentBusNumber.getValue().toString());

            }


            if (error.equals("")) {
                userHomeModel.addToStudentHasBusTable(name, motherPhone, fatherPhone, grade, busNumber, path, subscription);
                showStudentTableData();

                studentName.clear();
                studentFatherPhone.clear();
                studentMotherPhone.clear();
                studentGrade.clear();
                studentBusNumber.getSelectionModel().clearSelection();
                studentPath.clear();
                studentSubscription.getSelectionModel().clearSelection();

                //ObservableList<BusStudent> students = userHomeModel.getAllExpenses();

            } else {
                //System.out.println("adding student");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Entry or more are missing");
                alert.setContentText(error);

                alert.showAndWait();
            }
        }

    }

    private void hideSearchSection(){
        searchVBox.setVisible(false);
        searchVBox.setDisable(true);
        searchVBox.setPrefHeight(20);
        searchSectionActive=false;
    }
    @FXML private void hideSearchSectionButton(ActionEvent actionEvent){
        hideSearchSection();
    }
    @FXML private void searchButtonClicked(ActionEvent actionEvent) {
        if(!searchSectionActive) {
            searchVBox.setVisible(true);
            searchVBox.setDisable(false);
            searchVBox.setPrefHeight(200);
            searchSectionActive=true;
            hideAddStudentSection();
        }
        else{
            //rest of code here
        }
    }




        // add a new driver to the database
    @FXML private void addNewBus(ActionEvent actionEvent) throws SQLException {
        //TODO: check if driver already added

        String name = driverName.getText().toString();
        String phone = driverPhone.getText().toString();
        String busNumber = driverBusNumber.getText().toString();
        String area = driverArea.getText().toString();

        //TODO: add an error warning here

        if (!name.equals("") && !phone.equals("") && !busNumber.equals("") && !area.equals("")) {
            userHomeModel.addToBussesTable(name, phone, busNumber, area);
            driverTableView.setItems(userHomeModel.getAllDrivers());

            setBusNumbersComboBox();


        }
    }

    //show options when a row is clicked in student table
    @FXML public void studentTableClickItem(MouseEvent event) throws SQLException {
        if (event.getClickCount() == 2) //Checking double click
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);  //CONFIRMATION
            alert.setTitle("الخيارات");
            alert.setHeaderText("تعديل معلومات الطالب او حذفها");
//            alert.setContentText("Choose your option.");

            ButtonType buttonTypeOne = new ButtonType("تعديل");
            ButtonType buttonTypeTwo = new ButtonType("حذف");
            //ButtonType buttonTypeThree = new ButtonType("Three");
            ButtonType buttonTypeCancel = new ButtonType("اٍلغاء", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();

            BusStudent busStudent=studentTableView.getSelectionModel().getSelectedItem();

            //store information of the row
            int studentID=studentTableView.getSelectionModel().getSelectedItem().getStudentID();

            if (result.get() == buttonTypeOne){
                studentEditDialog(new ActionEvent(),busStudent);
                //studentEditDialog(new ActionEvent(),busStudent);

                // ... user chose "One"
            } else if (result.get() == buttonTypeTwo) {
                userHomeModel.removeStudentHasBus(studentID);
                showStudentTableData();

            }else {
                // ... user chose CANCEL or closed the dialog
            }

        }
    }

    //show options when a row is clicked in drivers table
    @FXML public void driverTableClickItem(MouseEvent event) throws SQLException {
        if (event.getClickCount() == 2) //Checking double click
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);  //CONFIRMATION
            alert.setTitle("Options");
            alert.setHeaderText("Edit or remove a driver");
            alert.setContentText("Choose your option.");

            ButtonType buttonTypeOne = new ButtonType("Edit");
            ButtonType buttonTypeTwo = new ButtonType("Delete");
            //ButtonType buttonTypeThree = new ButtonType("Three");
            ButtonType buttonTypeCancel = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            Bus bus=driverTableView.getSelectionModel().getSelectedItem();
            if (result.get() == buttonTypeOne){
                driverEditDialog(new ActionEvent(),bus);
                //driverEditDialog(new ActionEvent());

                // ... user chose "One"
            } else if (result.get() == buttonTypeTwo) { //TODO: assign students in this bus to anther one
                userHomeModel.removeDriver(driverTableView.getSelectionModel().getSelectedItem().getBusID());
                driverTableView.setItems(userHomeModel.getAllDrivers());
                setBusNumbersComboBox();
                showStudentTableData();

            }else {
                // ... user chose CANCEL or closed the dialog
            }

        }
    }

    public void driverEditDialog(ActionEvent event, Bus bus){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane) loader.load(getClass().getResource("editDriverFXML.fxml").openStream());
            EditDriverController editDriver = loader.getController();
            editDriver.setData(bus);

            stage.setTitle("Edit driver information");
            stage.setScene(new Scene(root));

            stage.setResizable(false);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void studentEditDialog(ActionEvent event,BusStudent busStudent){
        try {
            Stage editStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane) loader.load(getClass().getResource("editStudentFXML.fxml").openStream());
            EditStudentController editStudent = loader.getController();
            editStudent.setData(busStudent);


            editStage.setTitle("Edit student information");
            editStage.setScene(new Scene(root));
            editStage.setUserData(busStudent);

            editStage.setResizable(false);
            editStage.show();


//            editStage.setOnCloseRequest( ev -> {
//                showStudentTableData();
//            });
            editStage.setOnHidden( ev -> {
                showStudentTableData();
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void driverEditDialog(Bus bus) throws SQLException {



        // Create the dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("تعديل");
        dialog.setHeaderText("تعديل معلومات السائق: "+bus.getDriverName());

        // Set the button types.
        ButtonType saveButtonType = new ButtonType("حفظ التغيرات", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);//was 10

        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 50));

        TextField editDriverName = new TextField();
        TextField editDriverPhone= new TextField();
        TextField editBusNumber= new TextField();
        TextField editArea= new TextField();



        editDriverName.setPromptText("اسم السائق");
        editDriverPhone.setPromptText("رقم الهاتف");
        editBusNumber.setPromptText("رقم الباص");
        editArea.setPromptText("المنطقة");


        editDriverName.setText(bus.getDriverName());
        editDriverPhone.setText(bus.getDriverPhoneNumber());
        editBusNumber.setText(String.valueOf(bus.getBusNumber()));
        editArea.setText(bus.getArea());


        grid.add(new Label("اسم السائق:"), 4, 0);     //0
        grid.add(editDriverName, 3, 0);
        grid.add(new Label("رقم الهاتف :"), 4, 1);   //1
        grid.add(editDriverPhone, 3, 1);
        grid.add(new Label("رقم الباص:"), 4, 2);   //2
        grid.add(editBusNumber, 3, 2);
        grid.add(new Label("المنطقة:"), 4, 3);           //3
        grid.add(editArea, 3, 3);


// Enable/Disable login button depending on whether a username was entered.
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        editDriverName.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
        });
        editDriverPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
        });
        editBusNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
        });
        editArea.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
        });



        dialog.getDialogPane().setContent(grid);
        Optional<Pair<String, String>> result = dialog.showAndWait();

        int checkChanges=0;

        if (result.isPresent()){//TODO: why this button doesnot work

            if (!editDriverName.getText().equals(bus.getDriverName())) {
                userHomeModel.updateBusDriverName(bus.getBusID(), editDriverName.getText());
                checkChanges++;
            }
            if (!editDriverPhone.getText().equals(bus.getDriverPhoneNumber())) {
                userHomeModel.updateBusDriverPhoneNumber(bus.getBusID(), editDriverPhone.getText());
                checkChanges++;
            }
            if (!editBusNumber.getText().equals(bus.getBusNumber())) {  //TODO: assign students in this bus to anther one
                userHomeModel.updateBusNumber(bus.getBusID(), editBusNumber.getText());
                checkChanges++;
            }
            if (!editArea.getText().equals(bus.getArea())) {
                userHomeModel.updateBusArea(bus.getBusID(), editArea.getText());
                checkChanges++;
            }

            if (checkChanges > 0) {
                driverTableView.setItems(userHomeModel.getAllDrivers());
                showStudentTableData();
            }

        }
    }

//    public void studentEditDialog(BusStudent busStudent) throws SQLException {
//        // Create the dialog.
//        Dialog<Pair<String, String>> dialog = new Dialog<>();
//        dialog.setTitle("تعديل معلومات الطالب");
//        dialog.setHeaderText("تعديل معلومات الطالب: "+busStudent.getName());
//
//        // Set the button types.
//        ButtonType saveButtonType = new ButtonType("حفظ التغيرات", ButtonBar.ButtonData.OK_DONE);
//        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);//was 10
//
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20, 20, 10, 50));
//
//        TextField editStudentName = new TextField();
//        TextField editFatherPhone= new TextField();
//        TextField editMotherPhone= new TextField();
//        TextField editGrade= new TextField();
//        TextField editPath= new TextField();
//        ComboBox editBusNumber= new ComboBox();
//        ComboBox editSubscription = new ComboBox();
//
//        editBusNumber.setMinWidth(160);
//        editSubscription.setMinWidth(160);
//
//        editStudentName.setPromptText("اسم الطالب");
//        editFatherPhone.setPromptText("رقم هاتف الاب");
//        editMotherPhone.setPromptText("رقم هاتف الام");
//        editGrade.setPromptText("الصف");
//        editPath.setPromptText("المسار");
//        editBusNumber.setPromptText("رقم الباص");
//        editSubscription.setPromptText("نوع الاشتراك");
//
//        editStudentName.setText(busStudent.getName());
//        editFatherPhone.setText(busStudent.getFatherPhone());
//        editMotherPhone.setText(busStudent.getMotherPhone());
//        editGrade.setText(busStudent.getGrade());
//        editPath.setText(busStudent.getPath());
//        editBusNumber.getItems().clear();
//        for (int i=0; i<busNumbersList.size();i++) {
//            editBusNumber.getItems().add(busNumbersList.get(i));
//        }
//        editBusNumber.getSelectionModel().select(busNumbersList.indexOf(busStudent.getBusNumber()));
//
//        for (int i=0; i<subscriptionList.size();i++) {
//            editSubscription.getItems().add(subscriptionList.get(i));
//        }
//        editSubscription.getSelectionModel().select(subscriptionList.indexOf(busStudent.getSubscription()));
//
//        grid.add(new Label("اسم الطالب:"), 4, 0);     //0
//        grid.add(editStudentName, 3, 0);
//        grid.add(new Label("رقم هاتف الاب:"), 4, 1);   //1
//        grid.add(editFatherPhone, 3, 1);
//        grid.add(new Label("رقم هاتف الام:"), 4, 2);   //2
//        grid.add(editMotherPhone, 3, 2);
//        grid.add(new Label("الصف:"), 4, 3);           //3
//        grid.add(editGrade, 3, 3);
//        grid.add(new Label("المسار:"), 4, 4);         //4
//        grid.add(editPath, 3, 4);
//        grid.add(new Label("رقم الباص:"), 4, 5);      //5
//        grid.add(editBusNumber, 3, 5);
//        grid.add(new Label("نوع الاشتراك:"), 4, 6);    //6
//        grid.add(editSubscription, 3, 6);
//
//
//// Enable/Disable login button depending on whether a username was entered.
//        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
//        saveButton.setDisable(true);
//
//// Do some validation (using the Java 8 lambda syntax).
//        editStudentName.textProperty().addListener((observable, oldValue, newValue) -> {
//            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
//        });
//        editFatherPhone.textProperty().addListener((observable, oldValue, newValue) -> {
//            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
//        });
//        editMotherPhone.textProperty().addListener((observable, oldValue, newValue) -> {
//            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
//        });
//        editGrade.textProperty().addListener((observable, oldValue, newValue) -> {
//            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
//        });
//        editPath.textProperty().addListener((observable, oldValue, newValue) -> {
//            saveButton.setDisable(newValue.equals(oldValue)||newValue.trim().isEmpty());
//        });
//
//        editBusNumber.valueProperty().addListener((observable, oldValue, newValue) -> {
//            saveButton.setDisable(newValue.equals(oldValue));
//        });
//        editSubscription.valueProperty().addListener((observable, oldValue, newValue) -> {
//            saveButton.setDisable(newValue.equals(oldValue));
//        });
//
//
//        dialog.getDialogPane().setContent(grid);
//        Optional<Pair<String, String>> result = dialog.showAndWait();
//
//
//        int checkChanges=0;
//        if (result.isPresent()){
//            if (!editStudentName.getText().equals(busStudent.getName())){
//                userHomeModel.updateStudentName(busStudent.getStudentID(),editStudentName.getText());
//                checkChanges++;
//            }
//            if (!editFatherPhone.getText().equals(busStudent.getFatherPhone())){
//                userHomeModel.updateStudentFatherPhone(busStudent.getStudentID(),editFatherPhone.getText());
//                checkChanges++;
//            }
//            if (!editMotherPhone.getText().equals(busStudent.getMotherPhone())){
//                userHomeModel.updateStudentMotherPhone(busStudent.getStudentID(),editMotherPhone.getText());
//                checkChanges++;
//            }
//            if (!editGrade.getText().equals(busStudent.getGrade())){
//                userHomeModel.updateStudentGrade(busStudent.getStudentID(),editGrade.getText());
//                checkChanges++;
//            }
//            if (!editPath.getText().equals(busStudent.getPath())){
//                userHomeModel.updateStudentPath(busStudent.getStudentID(),editPath.getText());
//                checkChanges++;
//
//            }
//            if (!editSubscription.getValue().toString().equals(busStudent.getSubscription())){
//                userHomeModel.updateStudentSubscription(busStudent.getStudentID(),editSubscription.getValue().toString());
//                checkChanges++;
//
//            }
//            if (!editBusNumber.getValue().toString().equals(busStudent.getBusNumber())){
//                userHomeModel.updateStudentBusNumber(busStudent.getStudentID(),editBusNumber.getValue().toString());
//                checkChanges++;
//
//            }
//
//            if (checkChanges>0){
//                showStudentTableData();
//            }
//        }
//    }

    //set the bus numbers for the bus number combo box
    private void setBusNumbersComboBox(){
        busNumbersList =userHomeModel.getBusNumbers();
        studentBusNumber.getItems().clear();
        for (int i=0; i<busNumbersList.size();i++) {
            studentBusNumber.getItems().add(busNumbersList.get(i));
        }
    }

    public void showStudentTableData(){
        if (tableFilter==0){
            studentTableView.setItems(userHomeModel.getAllStudents());

        }

    }




}
