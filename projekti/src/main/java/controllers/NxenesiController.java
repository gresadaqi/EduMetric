package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import services.NxenesitService;
import utils.LanguageHandler;
import utils.SceneLocator;
import utils.SceneNavigator;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import models.User.Role;
import javafx.application.Platform;
import services.UserService;
import javafx.stage.Stage;
import utils.MenuUtils;
import utils.SceneNavigator;

public class NxenesiController {



    @FXML private ListView<String> raportiNxenesve;

    @FXML private MenuButton menuLanguage;
    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;

    @FXML public void initialize() {
        // Configure language menu
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.STUDENT_PAGE);
        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }


    @FXML
    private void logout(ActionEvent event) {
        SceneNavigator.logout((Node) event.getSource());
    }
    private final NxenesitService service = new NxenesitService();

    @FXML
    private void onOpenSettings(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), SceneLocator.SETTINGS_PAGE);
    }
    @FXML
    private void onOpenNotat(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), SceneLocator.STUDENT_GRADES_PAGE);
    }
    private void mbushRaportinENxenesve() {
        raportiNxenesve.getItems().clear();
        var nxenesit = service.merrTeGjitheNxenesit();
        for (var n : nxenesit) {
            String rreshti = "ID: " + n.getId()
                    + " | Emër: " + n.getEmri()
                    + " " + n.getMbiemri()
                    + " | Datëlindja: " + n.getDatelindja()
                    + " | Gjinia: " + n.getGjinia()
                    + " | Email: " + n.getEmail()
                    + " | Tel: " + n.getPhone()
                    + " | Adresa: " + n.getAdresa().getRruga();
            raportiNxenesve.getItems().add(rreshti);
        }
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

