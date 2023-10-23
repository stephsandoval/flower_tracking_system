package Screens;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.HistoryController;
import Events.Event;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoryScreen extends ScreenChanger implements Initializable {
    
    @FXML
    private TextField screenPackageID;
    @FXML
    private Button searchButton;
    @FXML
    private TableView <Event> historyTable;
    @FXML
    private TableColumn <Event, String> dateColumn, companyColumn, locationColumn, statusColumn, descriptionColumn;

    private HistoryController controller;

    public void searchID (){
        boolean validInput = validateIDInput();
        if (!validInput){
            screenPackageID.clear();
            return;
        }
        int packageID = Integer.valueOf(screenPackageID.getText());
        controller = new HistoryController(packageID);
        boolean validID = validateID();
        if (validID) {
            populateTable();
        }
    }

    private boolean validateID (){
        String message = "Invalid package ID";
        if (!controller.packageExists()){
            screenPackageID.clear();
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
            FTSAlert.getInstance().showAlert(message);
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void populateTable (){
        ArrayList <Event> events = controller.getEvents();
        ObservableList <Event> tableEvents = historyTable.getItems();
        for (Event event : events) {
            tableEvents.add(event);
        }
        historyTable.setItems(tableEvents);
    }
}