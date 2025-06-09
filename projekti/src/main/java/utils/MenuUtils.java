package utils;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;
import models.User.Role;

public class MenuUtils {

    private static final Map<Role, List<String>> allowedViewsPerRole = Map.ofEntries(
            Map.entry(Role.ADMIN, List.of("menaxhimiShkolles.fxml", "menaxhimiDrejtoreve.fxml")),
            Map.entry(Role.DREJTOR, List.of("MenaxhimiIMesuesit.fxml", "MenaxhimiKlaseve.fxml", "MenaxhimiLendeve.fxml", "StatistikatDrejtor.fxml")),
            Map.entry(Role.PRINCIPAL, List.of("MenaxhimiMesuesit.fxml", "MenaxhimiKlaseve.fxml", "MenaxhimiLendeve.fxml", "StatistikatDrejtor.fxml")),
            Map.entry(Role.MESUES, List.of("MenaxhimiNxenesve.fxml", "MenaxhimiINotave.fxml","MenaxhimiMungesave.fxml", "statistikaMesuesi.fxml")),
            Map.entry(Role.TEACHER, List.of("MenaxhimiNxenesve.fxml", "MenaxhimiINotave.fxml","MenaxhimiMungesave.fxml", "statistikaMesuesi.fxml")),
            Map.entry(Role.NXENES, List.of("NotatNxenesi.fxml", "Mungesat.fxml")),
            Map.entry(Role.STUDENT, List.of("NotatNxenesi.fxml", "Mungesat.fxml"))
    );

    public static void populateOpenSubMenu(Menu openMenu, Role role, Stage stage) {
        openMenu.getItems().clear();

        List<String> views = allowedViewsPerRole.getOrDefault(role, List.of());

        for (String view : views) {
            if (view == null || view.isBlank()) continue;

            String title = formatTitleFromFxml(view);
            MenuItem item = new MenuItem(title);

            item.setOnAction(e -> {
                openConditionalView(role, view, title, stage);
            });

            openMenu.getItems().add(item);
        }
    }


    public static void openConditionalView(Role role, String fxmlName, String title, Stage stage) {
        List<String> allowedViews = allowedViewsPerRole.getOrDefault(role, List.of());

        if (allowedViews.contains(fxmlName)) {
            openView("/views/" + fxmlName, title, stage);
        } else {
            showError("⛔ Nuk lejohet të hapet ky view nga ky rol.");
        }
    }

    public static void openView(String fxmlPath, String title, Stage stage) {
        SceneNavigator.switchScene(stage, fxmlPath);  // përdor Navigatorin
    }

    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gabim");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static String formatTitleFromFxml(String fxmlName) {
        return fxmlName
                .replace(".fxml", "")
                .replace("Menaxhimi", "Menaxhimi i")
                .replace("Statistikat", "Statistikat")
                .replace("statistika", "Statistika")
                .replaceAll("([A-Z])", " $1")
                .trim();
    }

    public static void registerShortcuts(Scene scene) {
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN),
                () -> performUndo(scene)
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN),
                () -> {
                    System.exit(0);
                }
        );
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN),
                () -> performRedo(scene)
        );

        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN),
                () -> performCut(scene)
        );

        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN),
                () -> performCopy(scene)
        );

        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN),
                () -> performPaste(scene)
        );

        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN),
                () -> performSelectAll(scene)
        );
    }

    private static TextInputControl getFocusedTextInput(Scene scene) {
        if (scene.getFocusOwner() instanceof TextInputControl input) {
            return input;
        }
        return null;
    }

    public static void performUndo(Scene scene) {
        TextInputControl focused = getFocusedTextInput(scene);
        if (focused != null) focused.undo();
    }

    public static void performRedo(Scene scene) {
        TextInputControl focused = getFocusedTextInput(scene);
        if (focused != null) focused.redo();
    }

    public static void performCut(Scene scene) {
        TextInputControl focused = getFocusedTextInput(scene);
        if (focused != null) focused.cut();
    }

    public static void performCopy(Scene scene) {
        TextInputControl focused = getFocusedTextInput(scene);
        if (focused != null) focused.copy();
    }

    public static void performPaste(Scene scene) {
        TextInputControl focused = getFocusedTextInput(scene);
        if (focused != null) focused.paste();
    }

    public static void performSelectAll(Scene scene) {
        TextInputControl focused = getFocusedTextInput(scene);
        if (focused != null) focused.selectAll();
    }

}


