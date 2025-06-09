package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Mesuesi;
import models.Shkolla;
import models.User;
import models.dto.create.CreateShkolla;
import models.dto.update.UpdateShkolla;
import repositories.AdresaRepository;
import repositories.ShkollaRepository;
import services.UserService;
import utils.LanguageHandler;
import utils.MenuUtils;
import utils.SceneLocator;
import utils.SceneNavigator;


import static utils.ZipUtils.gjejQytetinNgaZip;
import java.util.List;

public class MenaxhimiShkollesController {

    @FXML private TextField txtId;
    @FXML private TextField txtEmri;
    @FXML private TextField txtTel;
    @FXML private TextField txtAdresa;
    @FXML private TextField txtZip;
    @FXML private TableView<Shkolla> tabelaShkollave;
    @FXML private TableColumn<Shkolla, Integer> colId;
    @FXML private TableColumn<Shkolla, String> colEmri;
    @FXML private TableColumn<Shkolla, String> colTel;

    private final ShkollaRepository repo = new ShkollaRepository();
    private final AdresaRepository adresaRepo = new AdresaRepository();
    @FXML
    private MenuButton menuLanguage;

    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;
    @FXML
    private Label statusLabel;
    @FXML
    public void initialize() {
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.SCHOOL_MANAGEMENT_PAGE);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        mbushTabelen();

        Platform.runLater(() -> {
            User.Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }
    @FXML
    private void shto() {
        String rruga = txtAdresa.getText();
        String kodiPostar = txtZip.getText();
        String qyteti = gjejQytetinNgaZip(kodiPostar);

        if (qyteti == null) {
            showAlert(false, "Gabim", "Kodi postar i panjohur!", "Kontrolloni kodin postar.");
            return;
        }

        AdresaRepository adresaRepo = new AdresaRepository();
        Integer adresaId = adresaRepo.shtoAdrese(qyteti, rruga, kodiPostar);

        if (adresaId == null) {
            showAlert(false, "Gabim", "Adresa nuk u ruajt!", "Adresa dështoi.");
            return;
        }
        CreateShkolla shkolla=new CreateShkolla(
                txtEmri.getText(),
                txtTel.getText(),
                adresaId
        );

        ShkollaRepository shkollarepo=new ShkollaRepository();
        boolean success = shkollarepo.shtoShkollen(shkolla);
        if (success) {
            mbushTabelen();
            statusLabel.setText("✅Shkolla u shtua me sukses.");
        }


        showAlert(success, "Shtim", "Shkolla u shtua me sukses!", "Shtimi dështoi.");
    }

    @FXML
    private void perditeso() {
        int id = Integer.parseInt(txtId.getText());
        String rruga = txtAdresa.getText();
        String zip = txtZip.getText();
        Integer adresaId = 0;
        if (!rruga.isBlank() && !zip.isBlank()) {
            String qyteti = gjejQytetinNgaZip(zip);
            if (qyteti != null) {
                adresaId = adresaRepo.shtoAdrese(qyteti, rruga, zip);
            }
        }
        UpdateShkolla shkolla = new UpdateShkolla(
                id,
                txtEmri.getText().isBlank() ? null : txtEmri.getText(),
                txtTel.getText().isBlank() ? null : txtTel.getText(),
                adresaId
        );

        boolean success = repo.perditesoShkollen(shkolla);
        if(success){
            mbushTabelen();
            statusLabel.setText("Shkolla u perditesua me sukses.");
        }
        showAlert(success, "Përditësim", "Shkolla u përditësua me sukses!", "Përditësimi dështoi.");
    }

    @FXML
    private void fshij() {
        int id = Integer.parseInt(txtId.getText());
        boolean success = repo.fshijShkollen(id);

        if (success) {
            mbushTabelen();
            statusLabel.setText("Shkolla u fshi me sukses.");
        }

    }

    private void showAlert(boolean success, String title, String msgSuccess, String msgFail) {
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(success ? msgSuccess : msgFail);
        alert.showAndWait();
    }


    private void mbushTabelen() {
        List<Shkolla> shkollat = repo.gjejTeGjitha();
        ObservableList<Shkolla> listaObservable = FXCollections.observableArrayList(shkollat);
        tabelaShkollave.setItems(listaObservable);
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