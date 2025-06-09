package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuButton;
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

public class MesuesiStatistikatController {

    @FXML
    private LineChart<String, Number> lineChartKlasa;

    @FXML
    private LineChart<String, Number> lineChartGjinia;

    @FXML
    private MenuButton menuLanguage;

    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;

    @FXML
    public void initialize() {
        setupKlasaChart();
        setupGjiniaChart();
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.HELP_PAGE);
        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }

    private void setupKlasaChart() {
        CategoryAxis xAxis = (CategoryAxis) lineChartKlasa.getXAxis();
        NumberAxis yAxis = (NumberAxis) lineChartKlasa.getYAxis();

        xAxis.setLabel("Klasa");
        yAxis.setLabel("Numri i Nxënësve");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nxënësit sipas klasës");

        series.getData().add(new XYChart.Data<>("Klasa 9", 48));
        series.getData().add(new XYChart.Data<>("Klasa 10", 42));
        series.getData().add(new XYChart.Data<>("Klasa 11", 53));
        series.getData().add(new XYChart.Data<>("Klasa 12", 39));

        lineChartKlasa.getData().clear();
        lineChartKlasa.getData().add(series);
    }

    private void setupGjiniaChart() {
        CategoryAxis xAxis = (CategoryAxis) lineChartGjinia.getXAxis();
        NumberAxis yAxis = (NumberAxis) lineChartGjinia.getYAxis();

        xAxis.setLabel("Gjinia");
        yAxis.setLabel("Numri i Nxënësve");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nxënësit sipas gjinise");

        series.getData().add(new XYChart.Data<>("Mashkull", 102));
        series.getData().add(new XYChart.Data<>("Femër", 108));

        lineChartGjinia.getData().clear();
        lineChartGjinia.getData().add(series);
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

