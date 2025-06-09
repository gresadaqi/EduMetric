package controllers;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Notat;
import models.dto.create.CreateNotat;
import repositories.*;
import services.NotatService;
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

public class NotatController {

    @FXML private TextField txtIdNxenesit, txtIdMesuesit, txtLenda, nota1, nota2;
    @FXML private ComboBox<String> cmbKlasa, cmbParalelja, cmbDrejtimi,comboPeriudha;
    @FXML private Label lblMesatarja, lblNotaFinale;
    @FXML private ListView<String> listaNotave;
    @FXML private Label statusLabel;
    @FXML private TableView<Notat> tabelaNotave;
    @FXML private TableColumn<Notat, String> colNxenesiEmri;
    @FXML private TableColumn<Notat, Integer> colNota1;
    @FXML private TableColumn<Notat, Integer> colNota2;

    private final LendaRepository lendaRepository = new LendaRepository();

    private final NotatService notatService = new NotatService();

    @FXML private MenuButton menuLanguage;
    @FXML private VBox root;
    @FXML private Menu menuOpen;
    @FXML private MenuItem menuCut, menuCopy, menuPaste, menuUndo, menuSelectAll, menuRedo;
    @FXML
    private TableColumn<Notat, String> colLenda;


    @FXML
    public void initialize() {

        colLenda.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getLenda().getEmri())
        );

        colNota1.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNotaPare()).asObject());
        colNota2.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNotaDyte()).asObject());
        colNxenesiEmri.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getNxenesi().getEmri() + " " + cellData.getValue().getNxenesi().getMbiemri()));
        LanguageHandler.configureLanguageMenu(menuLanguage, SceneLocator.GRADE_MANAGEMENT_PAGE);
        mbushRaportinMeNotatNgaDatabaza();
        Platform.runLater(() -> {
            Role role = UserService.getCurrentUser().getRole();
            Stage stage = (Stage) root.getScene().getWindow();
            MenuUtils.populateOpenSubMenu(menuOpen, role, stage);
        });

    }
    @FXML
    private void regjistroNota() {
        // Kontrolli i fushave bosh
        if (txtIdNxenesit.getText().isEmpty() || txtIdMesuesit.getText().isEmpty() ||
                txtLenda.getText().isEmpty() || nota1.getText().isEmpty() || nota2.getText().isEmpty() ||
                cmbDrejtimi.getValue() == null || cmbParalelja.getValue() == null ||
                cmbKlasa.getValue() == null || comboPeriudha.getValue() == null) {

            showAlert("Gabim", "Ju lutem plotësoni të gjitha fushat!");
            return;
        }

        try {
            // Leximi dhe validimi i ID-ve dhe notave
            int nxenesiId = Integer.parseInt(txtIdNxenesit.getText().trim());
            int mesuesiId = Integer.parseInt(txtIdMesuesit.getText().trim());
            int notaPare = Integer.parseInt(nota1.getText().trim());
            int notaDyte = Integer.parseInt(nota2.getText().trim());

            // Leximi i emrave nga ComboBox-et
            String emriLendes = txtLenda.getText().trim();
            String drejtimiEmri = cmbDrejtimi.getValue();
            String paraleljaEmri = cmbParalelja.getValue();
            Integer klasaEmri = Integer.valueOf(cmbKlasa.getValue());
            String periudhaEmri = comboPeriudha.getValue();

            // Kërkimi i ID-ve përmes emrave
            int lendaId = lendaRepository.getLendaIdByName(emriLendes);
            int drejtimiId = new DrejtimiRepository().getDrejtimiIdByName(drejtimiEmri);
            int paraleljaId = new ParaleljaRepository().getParaleljaIdByName(paraleljaEmri);
            int klasaId = new KlasaRepository().getKlasaIdByNiveli(klasaEmri);
            Integer periudhaId = PeriodaRepository.getPeriodaIdByName(periudhaEmri);

            // Kontrollo nëse ndonjë ID nuk është gjetur
            if (lendaId <= 0 || drejtimiId <= 0 || paraleljaId <= 0 || klasaId <= 0 || periudhaId == null || periudhaId <= 0) {
                showAlert("Gabim", "Një nga ID-të nuk u gjet në databazë. Kontrolloni saktësinë e të dhënave.");
                return;
            }


            // Krijimi i objektit të notës për regjistrim
            CreateNotat nota = new CreateNotat(
                    nxenesiId, lendaId, mesuesiId,
                    drejtimiId, klasaId, paraleljaId,
                    periudhaId, notaPare, notaDyte
            );

            boolean sukses = notatService.regjistroNota(nota);

            if (sukses) {
                showAlert("Sukses", "Nota u regjistrua me sukses!");
                mbushRaportinMeNotatNgaDatabaza();
                statusLabel.setText("✅Nota u shtua me sukses.");
                pastroFushat();
            } else {
                showAlert("Gabim", "Dështoi ruajtja e notës në databazë.");
            }

        } catch (NumberFormatException e) {
            showAlert("Gabim në format", "Sigurohu që ID-të dhe notat janë numra të vlefshëm.");
        } catch (Exception e) {
            showAlert("Gabim i papritur", "Ndodhi një gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    private void llogaritMesataren() {
        try {
            double n1 = Double.parseDouble(nota1.getText().trim());
            double n2 = Double.parseDouble(nota2.getText().trim());
            double mesatarja = (n1 + n2) / 2.0;

            lblMesatarja.setText(String.format("%.2f", mesatarja));

            String notaFinale;
            if (mesatarja >= 4) notaFinale = "5";
            else if (mesatarja >= 3) notaFinale = "4";
            else if (mesatarja >= 2) notaFinale = "3";
            else if (mesatarja >= 1) notaFinale = "2";
            else notaFinale = "1";

            lblNotaFinale.setText(notaFinale);

        } catch (NumberFormatException e) {
            showAlert("Gabim", "Ju lutem shkruani nota valide për të llogaritur mesataren.");
        }
    }

    @FXML
    private void pastroFushat() {
        txtIdNxenesit.clear();
        txtIdMesuesit.clear();
        txtLenda.clear();
        nota1.clear();
        nota2.clear();
        cmbDrejtimi.setValue(null);
        cmbParalelja.setValue(null);
        cmbKlasa.setValue(null);
        lblMesatarja.setText("");
        lblNotaFinale.setText("");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }





    private void mbushRaportinMeNotatNgaDatabaza() {
        List<Notat> notat = notatService.merrTeGjithaNotat();
        System.out.println("✅ Numri i notave nga databaza: " + notat.size());
        for (Notat n : notat) {
            System.out.println(" -> " + n.getNxenesi().getId() + ", " + n.getLenda().getEmri());
        }
        tabelaNotave.setItems(FXCollections.observableArrayList(notat));
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