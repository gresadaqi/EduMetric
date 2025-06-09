package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Lenda;
import models.dto.create.CreateLenda;
import repositories.LendaRepository;
import services.LendaService;
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

public class LendaController {

    @FXML private TextField txtId;
    @FXML private TextField txtEmri;
    @FXML private TextField txtDrejtimi;
    @FXML private TextField txtPerioda;
    @FXML private TextField txtMesuesi;
    @FXML private TableView<Lenda> tblLenda;
    @FXML private TableColumn<Lenda, Integer> colId;
    @FXML private TableColumn<Lenda, String> colEmri, colDrejtimi, colPeriudha, colMesuesi;

    private final LendaRepository repo = new LendaRepository();
    private final LendaService service = new LendaService();
    @FXML
    private MenuButton menuLanguage;

    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;
    @FXML
    private Label statusLabel;
    @FXML
    public void initialize() {
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.SUBJECT_MANAGEMENT_PAGE);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        colDrejtimi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDrejtimi().getEmri()));
        colPeriudha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerioda().getEmri()));
        colMesuesi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesuesi().getEmri() + " " + cellData.getValue().getMesuesi().getMbiemri()));
        mbushTabelen();

        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }

    @FXML
    private void shtoLenda() {
        try {
            if (txtEmri.getText().isEmpty() || txtDrejtimi.getText().isEmpty() || txtPerioda.getText().isEmpty() || txtMesuesi.getText().isEmpty()) {
                showAlert(false,"Gabim","Të dhëna të paplotësuara", "Ju lutem plotësoni të gjitha fushat!");
                return;
            }
            String emri = txtEmri.getText();
            int drejtimiId = service.lookupId("drejtimi", "emri", txtDrejtimi.getText());
            int periodaId = service.lookupId("perioda", "emri", txtPerioda.getText());
            int mesuesiId = service.lookupId("mesuesi", "emri", txtMesuesi.getText());
            CreateLenda lenda = new CreateLenda(emri, drejtimiId, periodaId, mesuesiId);
            boolean success = service.shto(lenda);
            if (success) {
                mbushTabelen();
                statusLabel.setText("✅Lenda u shtua me sukses.");
            } else {
                showAlert(success, "Shtim", "Lënda u shtua me sukses!", "Shtimi dështoi!");
            }
        }catch(NumberFormatException e){
            showAlert(false,"Gabim", "Të dhëna", "Ju lutem shkruani numra!");
        } catch (Exception e) {
            showAlert(false, "Gabim", "Ndodhi një gabim gjatë ruajtjes: " + e.getMessage(), "Ruajtja dështoi!");
        }
    }

    @FXML
    private void fshijLenda() {
        try {
            int id = Integer.parseInt(txtId.getText());
            System.out.println("📌 ID për fshirje: " + id);
            boolean success = repo.fshij(id);
            showAlert(success, "Fshirje", "Lënda u fshi me sukses!", "Fshirja dështoi!");
            if (success) mbushTabelen();
            statusLabel.setText("✅Lenda u fshij me sukses.");
        } catch (NumberFormatException e) {
            showAlert(false, "Gabim", "ID e pavlefshme", "Ju lutem shkruani një ID të saktë.");
        }
    }


    private void showAlert(boolean success, String title, String msgSuccess, String msgFail) {
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(success ? msgSuccess : msgFail);
        alert.showAndWait();
    }
    private void mbushTabelen(){
        List<Lenda> lendat = service.gjejTeGjitha();
        ObservableList<Lenda> listaObservable = FXCollections.observableArrayList(lendat);
        tblLenda.setItems(listaObservable);
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