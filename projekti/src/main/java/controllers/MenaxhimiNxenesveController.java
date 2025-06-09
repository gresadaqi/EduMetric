package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Nxenesit;
import models.dto.create.CreateNxenesit;
import models.dto.update.UpdateNxenesit;
import services.NxenesitService;
import utils.LanguageHandler;
import utils.SceneLocator;

import javafx.scene.layout.VBox;
import models.User.Role;
import javafx.application.Platform;
import services.UserService;
import javafx.stage.Stage;
import utils.MenuUtils;
import utils.SceneNavigator;

import java.util.List;

public class MenaxhimiNxenesveController {
    @FXML
    private TextField txtId, txtEmri, txtMbiemri, txtDatelindja, txtGjinia, txtEmail, txtPhone, txtAdresa;
    private final NxenesitService service = new NxenesitService();
    @FXML private MenuButton menuLanguage;
    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;
    @FXML private Label statusLabel;
    @FXML private TableView<Nxenesit> tabelaNxenesve;
    @FXML private TableColumn<Nxenesit, Integer> colId;
    @FXML private TableColumn<Nxenesit, String> colEmri;
    @FXML private TableColumn<Nxenesit, String> colMbiemri;
    @FXML private TableColumn<Nxenesit, String> colDatelindja;
    @FXML private TableColumn<Nxenesit, String> colGjinia;
    @FXML private TableColumn<Nxenesit, String> colEmail;
    @FXML private TableColumn<Nxenesit, String> colPhone;


    @FXML
    public void initialize() {
        // Configure language menu
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.STUDENT_MANAGEMENT_PAGE);
        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        colMbiemri.setCellValueFactory(new PropertyValueFactory<>("mbiemri"));
        colDatelindja.setCellValueFactory(new PropertyValueFactory<>("datelindja"));
        colGjinia.setCellValueFactory(new PropertyValueFactory<>("gjinia"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mbushTabelen();
    }

    @FXML
    private void shtoNxenes() {
        int adresaId = service.getAdresaId(txtAdresa.getText());
        CreateNxenesit nx = new CreateNxenesit(
                txtEmri.getText(),
                txtMbiemri.getText(),
                txtDatelindja.getText(),
                txtGjinia.getText().charAt(0),
                txtEmail.getText(),
                txtPhone.getText(),
                adresaId
        );

        if (service.shto(nx)) {
            statusLabel.setText("✅Nxenesi u shtua me sukses.");
            showAlert("Sukses", "Nxënësi u shtua me sukses.");
            mbushTabelen();
        } else {
            showAlert("Gabim", "Nxënësi nuk u shtua.");
        }
    }

    @FXML
    private void fshijNxenes() {
        int id = Integer.parseInt(txtId.getText());
        if (service.fshij(id)) {
            statusLabel.setText("✅Nxenesi u fshi me sukses.");
            showAlert("Sukses", "Nxënësi u fshi me sukses.");
            mbushTabelen();
        } else {
            showAlert("Gabim", "Nxënësi nuk u fshi.");
        }
    }

    @FXML
    private void perditesoNxenes() {
        int adresaId = service.getAdresaId(txtAdresa.getText());
        UpdateNxenesit nx = new UpdateNxenesit(
                Integer.parseInt(txtId.getText()),
                txtEmri.getText(),
                txtMbiemri.getText(),
                txtDatelindja.getText(),
                txtGjinia.getText().charAt(0),
                txtEmail.getText(),
                txtPhone.getText(),
                adresaId
        );

        if (service.perditeso(nx)) {
            statusLabel.setText("✅Nxenesi u perditesua me sukses.");
            showAlert("Sukses", "Nxënësi u përditësua me sukses.");
            mbushTabelen();
        } else {
            showAlert("Gabim", "Përditësimi dështoi.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void mbushTabelen(){
        List<Nxenesit> nxenesit = service.merrTeGjitheNxenesit();
        ObservableList<Nxenesit> listaObservable = FXCollections.observableArrayList(nxenesit);
        tabelaNxenesve.setItems(listaObservable);
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
