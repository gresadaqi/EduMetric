package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.User;
import models.dto.create.CreateUser;
import services.UserService;
import utils.LanguageHandler;
import utils.SceneLocator;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import models.User.Role;
import javafx.application.Platform;
import javafx.stage.Stage;
import utils.MenuUtils;
import utils.SceneNavigator;

public class ShtoUserController {

    @FXML
    private TextField txtEmri;
    @FXML
    private TextField txtMbiemri;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPerdoruesi;
    @FXML
    private PasswordField txtFjalekalimi;
    @FXML
    private PasswordField txtFjalekalimi2;
    @FXML
    private RadioButton radioDrejtor;
    @FXML
    private RadioButton radioMesues;
    @FXML
    private RadioButton radioNxenes;

    private final ToggleGroup roleGroup = new ToggleGroup();
    private final UserService userService = new UserService();

    @FXML
    private MenuButton menuLanguage;

    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;

    @FXML
    public void initialize() {
        radioDrejtor.setToggleGroup(roleGroup);
        radioMesues.setToggleGroup(roleGroup);
        radioNxenes.setToggleGroup(roleGroup);
        roleGroup.selectToggle(null);

        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.ADD_USER_PAGE);
        Platform.runLater(() -> {
            User currentUser = UserService.getCurrentUser();
            Stage stage = (Stage) root.getScene().getWindow();

            if (currentUser != null) {
                Role role = currentUser.getRole();
                MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
            } else {
                System.err.println("❌ Gabim: Përdoruesi nuk është i kyçur!");
                SceneNavigator.switchScene(stage, SceneLocator.LOGIN_PAGE);
            }
        });
    }


    @FXML
    private void handleRegister() {
        if (!validateInputs()) return;

        String emri = txtEmri.getText();
        String mbiemri = txtMbiemri.getText();
        String email = txtEmail.getText();
        String perdoruesi = txtPerdoruesi.getText();
        String fjalekalimi = txtFjalekalimi.getText();
        String roli = ((RadioButton) roleGroup.getSelectedToggle()).getText();

        User.Role role = switch (roli) {
            case "Principal" -> User.Role.PRINCIPAL;
            case "Teacher" -> User.Role.TEACHER;
            case "Student" -> User.Role.STUDENT;
            case "Drejtor" -> User.Role.DREJTOR;
            case "Mësues" -> User.Role.MESUES;
            case "Nxënës" -> User.Role.NXENES;
            default -> User.Role.ADMIN;
        };

        CreateUser user = new CreateUser(emri, mbiemri, email, perdoruesi, fjalekalimi, role);

        boolean uShtua = userService.register(user);

        if (uShtua) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Përdoruesi u regjistrua me sukses!");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Dështim", "Përdoruesi nuk u regjistrua!");
        }
    }


    private boolean validateInputs() {
        if (txtEmri.getText().isEmpty() || txtMbiemri.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || txtPerdoruesi.getText().isEmpty() ||
                txtFjalekalimi.getText().isEmpty() || txtFjalekalimi2.getText().isEmpty() ||
                roleGroup.getSelectedToggle() == null) {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Të dhëna të paplotësuara");
            return false;
        }

        if (!txtFjalekalimi.getText().equals(txtFjalekalimi2.getText())) {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Fjalëkalimet nuk përputhen");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    private void clearFields() {
        txtEmri.clear();
        txtMbiemri.clear();
        txtEmail.clear();
        txtPerdoruesi.clear();
        txtFjalekalimi.clear();
        txtFjalekalimi2.clear();
        roleGroup.selectToggle(null);
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
