package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.dto.update.UpdateUser;
import services.UserService;
import utils.LanguageHandler;
import utils.MenuUtils;
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

import java.util.Locale;

public class RivendosFjalekaliminController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private PasswordField txtPassword1;
    @FXML private PasswordField txtPassword2;
    @FXML private MenuButton menuLanguage;
    private final UserService userService = new UserService();

    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;

    @FXML
    public void initialize() {
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.RESET_PASSWORD);
    }

    @FXML
    void handleChange(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String verificationCode = txtPassword.getText().trim();
        String newPassword = txtPassword1.getText().trim();
        String confirmPassword = txtPassword2.getText().trim();

        if (!validateInputs(username, verificationCode, newPassword, confirmPassword)) return;

        if (userService.verifyUser(username, verificationCode)) {
            UpdateUser updateUser = new UpdateUser(username, newPassword, verificationCode);

            if (userService.updatePassword(updateUser)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses!", "Fjalëkalimi u ndryshua me sukses!");
                Stage stage = (Stage) txtUsername.getScene().getWindow();
                SceneLocator.locate(stage, SceneLocator.LOGIN_PAGE);
            } else {
                showAlert(Alert.AlertType.ERROR, "Gabim!", "Ndryshimi i fjalëkalimit dështoi.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Gabim!", "Përdoruesi ose kodi i verifikimit është i pasaktë.");
        }
        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }

    private boolean validateInputs(String username, String verificationCode, String newPassword, String confirmPassword) {
        if (username.isEmpty() || verificationCode.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Gabim!", "Ju lutem plotësoni të gjitha fushat.");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Gabim!", "Fjalëkalimet nuk përputhen!");
            return false;
        }

        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            showAlert(Alert.AlertType.ERROR, "Gabim!", "Fjalëkalimi duhet të përmbajë shkronja të mëdha, të vogla dhe numra!");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleEnglishLanguage(ActionEvent event) {
        Locale.setDefault(new Locale("en"));

        Stage stage = (Stage) menuLanguage.getScene().getWindow();
        SceneLocator.locate(stage, SceneLocator.LOGIN_PAGE);
    }

    @FXML
    void handleAlbanianLanguage(ActionEvent event) {
        Locale.setDefault(new Locale("sq"));

        Stage stage = (Stage) menuLanguage.getScene().getWindow();
        SceneLocator.locate(stage, SceneLocator.LOGIN_PAGE);
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
