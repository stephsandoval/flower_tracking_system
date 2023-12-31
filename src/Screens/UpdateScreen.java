package Screens;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.UpdateController;
import Events.Event;
import Json.JsonParser;
import Packages.PackagePreview;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class UpdateScreen extends ScreenChanger implements Initializable {
    
    @FXML
    private TextField screenPackageID, screenLocation, screenCompany, screenDescription;
    @FXML
    private ChoiceBox <String> screenStatus;
    @FXML
    private Button addDescriptionButton, finishUpdateButton, searchButton;
    @FXML
    private TableView <PackagePreview> packageTable;
    @FXML
    private DatePicker screenDate;
    @FXML
    private Pane packagePreview;
    @FXML
    private TableColumn <PackagePreview, String> itemColumn, contentColumn;

    private JsonParser items = JsonParser.getInstance();

    private UpdateController controller;
    private ArrayList <String> description = new ArrayList<>();
    private String location, company, status;
    private LocalDate date;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        screenStatus.getItems().setAll(items.getStatus());
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
    }

    public void addDescription (){
        String text = screenDescription.getText();
        if (!text.equals("")){
            description.add(text);
        }
        screenDescription.clear();
    }

    public void finishUpdate (){
        String message = "At least one of the fields contains invalid info";
        location = screenLocation.getText();
        company = screenCompany.getText();
        status = screenStatus.getValue();
        date = screenDate.getValue();
        if (validateFields()){
            controller.sendInfo(location, company, status, date, description);
        } else {
            FTSAlert.getInstance().showAlert(message);
        }
    }

    public void searchID (){
        boolean validInput = validateIDInput();
        if (!validInput){
            screenPackageID.clear();
            return;
        }
        int packageID = Integer.valueOf(screenPackageID.getText());
        controller = new UpdateController(packageID);
        boolean validID = validateID();
        if (validID){
            populateTable();
            clearFields();
            disactivateFields(false);
        }
    }

    private boolean validateID (){
        String message = "Invalid package ID";
        if (!controller.packageExists()){
            screenPackageID.clear();
            clearFields();
            disactivateFields(true);
            FTSAlert.getInstance().showAlert(message);
            return false;
        }
        return true;
    }

    private boolean validateIDInput (){
        String message = "ID should be a number";
        try {
            Integer.valueOf(screenPackageID.getText());
        } catch (Exception e){
            clearFields();
            disactivateFields(true);
            FTSAlert.getInstance().showAlert(message);
            return false;
        }
        return true;
    }

    private boolean validateFields (){
        return (!(location.equals("")) && !(company.equals("")) && (status != null) && (date != null) && (description != null));
    }

    private void disactivateFields (boolean condition){
        screenDate.setDisable(condition);
        screenCompany.setDisable(condition);
        screenLocation.setDisable(condition);
        screenDescription.setDisable(condition);
        screenStatus.setDisable(condition);
        addDescriptionButton.setDisable(condition);
        finishUpdateButton.setDisable(condition);
    }

    private void clearFields (){
        screenDate.setValue(null);
        screenCompany.clear();
        screenLocation.clear();
        screenDescription.clear();
    }

    private void populateTable (){
        ArrayList <PackagePreview> preview = controller.getPreview();
        ObservableList <PackagePreview> tableItems = packageTable.getItems();
        for (PackagePreview itemPreview : preview){
            tableItems.add(itemPreview);
        }
        packageTable.setItems(tableItems);
    }
}