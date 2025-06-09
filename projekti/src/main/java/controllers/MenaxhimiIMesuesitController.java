package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Mesuesi;
import models.dto.create.CreateMesuesi;
import models.dto.update.UpdateMesuesi;
import repositories.AdresaRepository;
import repositories.MesuesiRepository;
import services.MesuesiService;
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

public class MenaxhimiIMesuesitController {
    @FXML private TextField txtId, txtEmri, txtMbiemri, txtEmail, txtTel, txtRoli, txtAdresa;
    @FXML private TableView<Mesuesi> tabelaMesuesit;
    @FXML private TableColumn<Mesuesi, String> kolEmri, kolMbiemri, kolEmail, kolTel;
    @FXML private TableColumn<Mesuesi, Integer> kolId;
    @FXML private TextArea raportiMesuesve;

    private final MesuesiService mesuesiService = new MesuesiService();
    private final AdresaRepository adresaRepository = new AdresaRepository();
    private final MesuesiRepository repo= new MesuesiRepository();
    @FXML
    private MenuButton menuLanguage;
    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;

    @FXML
    private Label statusLabel;
    @FXML
    public void initialize() {
        kolId.setCellValueFactory(new PropertyValueFactory<>("id"));
        kolEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        kolMbiemri.setCellValueFactory(new PropertyValueFactory<>("mbiemri"));
        kolEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        kolTel.setCellValueFactory(new PropertyValueFactory<>("tel"));

        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.TEACHER_MANAGEMENT_PAGE);
        mbushTabelen();

        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }

    @FXML
    private void shtoMesues() {
        String emriAdresa = txtAdresa.getText();
        Integer adresaId = adresaRepository.gjejAdresaId(emriAdresa);

        if (adresaId == null) {
            shfaqAlert("Gabim", "Adresa nuk u gjet!", "Adresa \"" + emriAdresa + "\" nuk ekziston në databazë.", Alert.AlertType.ERROR);
            return;
        }

        CreateMesuesi m = new CreateMesuesi(
                txtEmri.getText(), txtMbiemri.getText(), txtEmail.getText(),
                txtTel.getText(), txtRoli.getText(), adresaId
        );

        boolean success = mesuesiService.shto(m);
        if (success) {
            mbushTabelen();
            shfaqAlert("Sukses", "Mësuesi u shtua!", "Të dhënat janë ruajtur me sukses.", Alert.AlertType.INFORMATION);
            statusLabel.setText("✅ Mësuesi u shtua me sukses.");
        } else {
            shfaqAlert("Gabim", "Dështoi shtimi!", "Nuk u arrit të ruhet mësuesi në databazë.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void perditesoMesues() {
        if (txtId.getText().isEmpty()) {
            shfaqAlert("Gabim", "ID mungon!", "Ju lutem shkruani ID-n e mësuesit që doni të përditësoni.", Alert.AlertType.WARNING);
            return;
        }

        String emriAdresa = txtAdresa.getText();
        Integer adresaId = adresaRepository.gjejAdresaId(emriAdresa);
        if (adresaId == null) {
            shfaqAlert("Gabim", "Adresa nuk u gjet!", "Adresa \"" + emriAdresa + "\" nuk ekziston në databazë.", Alert.AlertType.ERROR);
            return;
        }

        UpdateMesuesi m = new UpdateMesuesi(
                Integer.parseInt(txtId.getText()), txtEmri.getText(), txtMbiemri.getText(),
                txtEmail.getText(), txtTel.getText(), txtRoli.getText(), adresaId
        );

        boolean success = mesuesiService.perditeso(m);
        if (success) {
            shfaqAlert("Sukses", "Përditësimi u krye!", "Të dhënat u përditësuan me sukses.", Alert.AlertType.INFORMATION);
            mbushTabelen();
            statusLabel.setText("✅ Mesuesi u perditesua me sukses.");
        } else {
            shfaqAlert("Gabim", "Dështoi përditësimi!", "Nuk u arrit të përditësohen të dhënat në databazë.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void fshijMesues() {
        int id = Integer.parseInt(txtId.getText());
        try {
            System.out.println("📌 ID për fshirje: " + id);
            boolean success = repo.fshij(id);
            shfaqAlert("Sukses", "Fshirja u krye!", "Mësuesi me ID " + id + " u fshi me sukses.", Alert.AlertType.INFORMATION);
            if (success) mbushTabelen();
            statusLabel.setText("Mesuesi u fshi me sukses.");
        } catch (NumberFormatException e) {
            shfaqAlert("Gabim", "Dështoi fshirja!", "Nuk u arrit të fshihet mësuesi me ID " + id + ".", Alert.AlertType.ERROR);
        }
    }

    private void shfaqAlert(String titulli, String headeri, String mesazhi, Alert.AlertType tipi) {
        Alert alert = new Alert(tipi);
        alert.setTitle(titulli);
        alert.setHeaderText(headeri);
        alert.setContentText(mesazhi);
        alert.showAndWait();
    }
    private void mbushTabelen() {
        List<Mesuesi> klasat = mesuesiService.gjejTeGjitheMesuesit();
        ObservableList<Mesuesi> listaObservable = FXCollections.observableArrayList(klasat);
        tabelaMesuesit.setItems(listaObservable);
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