package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Klasa;
import models.dto.create.CreateKlasa;
import repositories.KlasaRepository;
import services.KlasaService;
import utils.LanguageHandler;
import utils.SceneLocator;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import models.User.Role;
import javafx.application.Platform;
import services.UserService;
import javafx.stage.Stage;
import utils.MenuUtils;
import utils.SceneNavigator;

import java.util.List;

public class MenaxhimiKlaseveController {

    @FXML
    private TextField txtNiveli, txtShkolla ,txtParalelja, txtProfesori, txtDrejtimi, txtId;

    private final KlasaService klasaService = new KlasaService();
    private final KlasaRepository repo= new KlasaRepository();

    @FXML
    private MenuButton menuLanguage;
    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;
    @FXML
    private Label statusLabel;
    @FXML
    public void initialize() {
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.CLASS_MANAGEMENT_PAGE);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNiveli.setCellValueFactory(new PropertyValueFactory<>("niveli"));
        colShkolla.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getShkolla().getEmri()));
        colParalelja.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getParalelja().getEmri()));
        colMesuesi.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getMesuesi().getEmri() + " " + cellData.getValue().getMesuesi().getMbiemri()));
        colDrejtimi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDrejtimi().getEmri()));
        mbushTabelen();

        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }
    @FXML private TableView<Klasa> tabelaKlasave;
    @FXML private TableColumn<Klasa, Integer> colId;
    @FXML private TableColumn<Klasa, Integer> colNiveli;
    @FXML private TableColumn<Klasa, String> colShkolla;
    @FXML private TableColumn<Klasa, String> colParalelja;
    @FXML private TableColumn<Klasa, String> colMesuesi;
    @FXML private TableColumn<Klasa, String> colDrejtimi;


    @FXML
    private void shtoKlasa() {
        try {
            if (txtNiveli.getText().isEmpty() || txtShkolla.getText().isEmpty() || txtParalelja.getText().isEmpty()
                    || txtProfesori.getText().isEmpty() || txtDrejtimi.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Vërejtje", "Ju lutem plotësoni të gjitha fushat.");
                return;
            }

            int niveli = Integer.parseInt(txtNiveli.getText());
            int shkollaId = klasaService.lookupId("shkolla", "emri", txtShkolla.getText());
            int paraleljaId = klasaService.lookupId("paralelja", "emri", txtParalelja.getText());
            int profesoriId = klasaService.lookupId("mesuesi", "emri", txtProfesori.getText());
            int drejtimiId = klasaService.lookupId("drejtimi", "emri", txtDrejtimi.getText());

            if (shkollaId == -1 || paraleljaId == -1 || profesoriId == -1 || drejtimiId == -1) {
                showAlert(Alert.AlertType.ERROR, "Gabim", "Një nga entitetet nuk u gjet në databazë.");
                return;
            }

            CreateKlasa klasa = new CreateKlasa(niveli, shkollaId, paraleljaId, profesoriId, drejtimiId);
            boolean success = klasaService.shtoKlasa(klasa);
            if (success) {
                mbushTabelen();
                statusLabel.setText("✅Klasa u shtua me sukses.");
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Klasa u shtua me sukses.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gabim", "Nuk u arrit të ruhet klasa.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Fusha 'Niveli' duhet të jetë numër.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Ndodhi një gabim gjatë ruajtjes: " + e.getMessage());
        }
    }

    @FXML
    private void fshijKlasa() {
        int id = Integer.parseInt(txtId.getText());
        try {
            System.out.println("📌 ID për fshirje: " + id);
            boolean success = repo.fshij(id);
            showAlert(Alert.AlertType.INFORMATION,"Sukses" ,"Fshirja u krye!");
            if (success) mbushTabelen();
            statusLabel.setText(" Klasa u fshi me sukses.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Gabim", "Fshirja dështoi!");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    private void mbushTabelen() {
        List<Klasa> klasat = klasaService.gjejTeGjitha();
        ObservableList<Klasa> listaObservable = FXCollections.observableArrayList(klasat);
        tabelaKlasave.setItems(listaObservable);
    }

    @FXML public void handleLogout(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        SceneNavigator.switchScene(stage, SceneLocator.LOGIN_PAGE);
    }
    @FXML public void handleSettings(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        SceneNavigator.switchScene(stage, SceneLocator.SETTINGS_PAGE);
    }
    @FXML public void handleHelp(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        SceneNavigator.switchScene(stage, SceneLocator.HELP_PAGE);
    }
    @FXML public void handleQuit(ActionEvent actionEvent) {
        Platform.exit();
    }
    @FXML public void handleNew(ActionEvent actionEvent) {
        Stage stage = (Stage) root.getScene().getWindow();
        SceneNavigator.switchScene(stage, SceneLocator.ADD_USER_PAGE);
    }
    public void handleUndo(ActionEvent actionEvent) {
        MenuUtils.performUndo(menuUndo.getParentPopup().getOwnerWindow().getScene());
    }
    public void handleRedo(ActionEvent actionEvent) {
        MenuUtils.performRedo(menuRedo.getParentPopup().getOwnerWindow().getScene());
    }
    public void handleCut(ActionEvent actionEvent) {
        MenuUtils.performCut(menuCut.getParentPopup().getOwnerWindow().getScene());
    }
    public void handleCopy(ActionEvent actionEvent) {
        MenuUtils.performCopy(menuCopy.getParentPopup().getOwnerWindow().getScene());
    }
    public void handlePaste(ActionEvent actionEvent) {
        MenuUtils.performPaste(menuPaste.getParentPopup().getOwnerWindow().getScene());
    }

    public void handleSelectAll(ActionEvent actionEvent) {
        MenuUtils.performSelectAll(menuSelectAll.getParentPopup().getOwnerWindow().getScene());
    }


}