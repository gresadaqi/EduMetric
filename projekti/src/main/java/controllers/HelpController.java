package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
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

public class HelpController {

    @FXML
    private TextArea txtContent;
    @FXML
    private MenuButton menuLanguage;

    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;

    @FXML
    public void initialize() {
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.HELP_PAGE);
        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }
    @FXML
    private void showPerdorimi() {
        txtContent.setStyle("-fx-text-fill: #3498db; -fx-font-size: 14px;");
        txtContent.setText("""
                ✅ Si mund ta përdorim aplikacionin?
                
                Për të përdorur aplikacionin në mënyrë të plotë, ndiqni këto hapa:
                
                1️⃣ Plotëso fushat me të dhënat e nxënësit, lëndën dhe emrin e mësuesit.
                2️⃣ Vendos notat përkatëse në fushat "Nota 1" dhe "Nota 2".
                3️⃣ Zgjidh periudhën për të cilën po bëhet regjistrimi i notave.
                4️⃣ Kliko në "Llogarit Mesataren" për të parë mesataren dhe notën përfundimtare.
                5️⃣ Kliko në "Regjistro Notat" për t'i ruajtur në raport.
                6️⃣ Të gjitha notat e regjistruara do të shfaqen në seksionin e poshtëm të aplikacionit.
                7️⃣ Për të pastruar fushat dhe të fillosh regjistrimin e ri, kliko "Pastro Fushat".
                """);
    }

    @FXML
    private void showLlogaritja() {
        txtContent.setText("""
                ✅ Si bëhet llogaritja e statistikave?
                
                Sistemi automatikisht llogarit mesataren e dy notave që vendosen.
                
                - Mesatarja paraqitet në formatin decimal.
                - Nota përfundimtare rrumbullakoset automatikisht në numër të plotë.
                - Nëse ka gabime gjatë futjes së notave, sistemi shfaq mesazhe paralajmëruese.
                """);
    }

    @FXML
    private void showRegjistrimi() {
        txtContent.setText("""
                ✅ Si regjistrohen notat në sistem?
                
                Pasi të plotësohen të gjitha fushat me të dhënat përkatëse, kliko "Regjistro Notat".
                
                - Informacionet e nxënësit, mësuesit, lëndës dhe notat ruhen në listën e raporteve.
                - Secili regjistrim përmban datën dhe orën e saktë të regjistrimit.
                """);
    }

    @FXML
    private void showPastrimi() {
        txtContent.setText("""
                ✅ Si pastrohen fushat në aplikacion?
                
                Kur klikohet butoni "Pastro Fushat":
                
                - Të gjitha fushat e të dhënave fshihen.
                - Lista e raporteve mbetet e pandryshuar.
                - Mund të filloni një regjistrim të ri pa problem.
                """);
    }

    @FXML
    private void showShfaqja() {
        txtContent.setText("""
                ✅ Si mund të shfaqen raportet e regjistruara?
                
                Të gjitha notat e regjistruara me sukses shfaqen në seksionin e poshtëm të aplikacionit.
                
                - Mund të shikohen me datë dhe orë të regjistrimit.
                - Secili regjistrim paraqitet me detaje të plota për identifikim të saktë.
                """);
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
