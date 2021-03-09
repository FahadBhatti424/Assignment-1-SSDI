package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {
    @FXML
    private TableView tabview1;
    @FXML
    private TableColumn File;
    @FXML
    private TableColumn Type;
    @FXML
    private TableColumn SpamProbability;



    @FXML
    private void initialize(){
        File.setCellValueFactory(new PropertyValueFactory<>("filename"));
        Type.setCellValueFactory(new PropertyValueFactory<>("actualClass"));
        SpamProbability.setCellValueFactory(new PropertyValueFactory<>("spamProbability"));

    }

}
