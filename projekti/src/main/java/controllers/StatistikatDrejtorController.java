package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import repositories.MesuesiRepository;
import repositories.MungesatRepository;
import repositories.NotatRepository;
import repositories.NxenesitRepository;
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

public class StatistikatDrejtorController {

    @FXML
    private PieChart outerPieChart;

    @FXML
    private PieChart innerPieChart;

    @FXML
    private Button btnRifresko;

    @FXML
    private Label lblTotalNxenesit;

    @FXML
    private Label lblMesuesit;

    @FXML
    private Label lblMungesat;

    private final NotatRepository notatRepo = new NotatRepository();
    private final MungesatRepository mungesatRepo = new MungesatRepository();
    private final MesuesiRepository mesuesitRepo = new MesuesiRepository();
    private final NxenesitRepository nxenesitRepo = new NxenesitRepository();
    @FXML
    private MenuButton menuLanguage;
    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;

    @FXML
    public void initialize() {
        ngarkoGrafiket();
        btnRifresko.setOnAction(e -> ngarkoGrafiket());
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.PRINCIPAL_STATISTICS_PAGE);
        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });
    }


    private void ngarkoGrafiket() {
        ObservableList<PieChart.Data> notat = FXCollections.observableArrayList(
                new PieChart.Data("Nota 1", notatRepo.numronNotat(1)),
                new PieChart.Data("Nota 2", notatRepo.numronNotat(2)),
                new PieChart.Data("Nota 3", notatRepo.numronNotat(3)),
                new PieChart.Data("Nota 4", notatRepo.numronNotat(4)),
                new PieChart.Data("Nota 5", notatRepo.numronNotat(5))
        );
        outerPieChart.setData(notat);
        outerPieChart.setLabelsVisible(true);
        outerPieChart.setLegendVisible(false);

        ObservableList<PieChart.Data> mungesat = FXCollections.observableArrayList(
                new PieChart.Data("Mungesat-Meshkuj", mungesatRepo.numriMungesavePerGjinine("M")),
                new PieChart.Data("Mungesat-Femra", mungesatRepo.numriMungesavePerGjinine("F"))
        );

        ObservableList<PieChart.Data> mungesatNormalizuara = normalizo(mungesat);
        innerPieChart.setData(mungesatNormalizuara);
        innerPieChart.setLabelsVisible(true);
        innerPieChart.setLegendVisible(false);

        int nxenesat = nxenesitRepo.merrNumrinENxenesve();
        lblTotalNxenesit.setText(String.valueOf(nxenesat));

        int mesueset = mesuesitRepo.merrNumrinEMesuesve();
        lblMesuesit.setText(String.valueOf(mesueset));

        int nrMungesat = mungesatRepo.numriMungesavePerGjinine("M") + mungesatRepo.numriMungesavePerGjinine("F");
        lblMungesat.setText(String.valueOf(nrMungesat));

        // ------------------------- STILIZIM + TOOLTIP -------------------------
        Platform.runLater(() -> {
            setPieColors(notat, new String[]{"#2980b9", "#27ae60", "#f1c40f", "#e67e22", "#c0392b"});
            setPieColors(mungesatNormalizuara, new String[]{"#1abc9c", "#e74c3c"});

            vendosTooltip(notat);
            vendosTooltip(mungesatNormalizuara);
        });
    }

    private ObservableList<PieChart.Data> normalizo(ObservableList<PieChart.Data> lista) {
        double total = lista.stream().mapToDouble(PieChart.Data::getPieValue).sum();
        ObservableList<PieChart.Data> normalizuar = FXCollections.observableArrayList();
        for (PieChart.Data d : lista) {
            double vleraNormale = (d.getPieValue() / total) * 100;
            normalizuar.add(new PieChart.Data(d.getName(), vleraNormale));
        }
        return normalizuar;
    }

    private void setPieColors(ObservableList<PieChart.Data> data, String[] colors) {
        for (int i = 0; i < data.size(); i++) {
            final PieChart.Data d = data.get(i);
            final String color = colors[i % colors.length];
            d.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-pie-color: " + color + ";");
                }
            });
        }
    }

    private void vendosTooltip(ObservableList<PieChart.Data> data) {
        for (PieChart.Data d : data) {
            Tooltip tooltip = new Tooltip(d.getName() + ": " + String.format("%.1f", d.getPieValue()) + "%");
            Tooltip.install(d.getNode(), tooltip);
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


