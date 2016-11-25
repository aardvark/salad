package ru.latuhin.salad.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Layout extends Application {

    private List<Image> tiles = new ArrayList<>();
    private List<Path> lastLoadedTiles = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(500);
        borderPane.setPrefHeight(500);

        double width = 4.0;
        double height = 4.0;
        TilesGroup group = new TilesGroup(width, height, tiles);
        borderPane.setCenter(group.getTilesGroup());

        Label widthLabel = new Label("Width");
        TextField widthTextField = new TextField(String.valueOf((int) width));
        widthTextField.setPrefWidth(50);

        Label heightLabel = new Label("Height");
        TextField heightTextField = new TextField(String.valueOf((int) height));
        heightTextField.setPrefWidth(50);

        final FileChooser fileChooser = new FileChooser();
        Button fileChooserButton = new Button("Choose tiles");
        fileChooserButton.setOnAction(e -> {
            List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
            if (list != null && !list.isEmpty()) {
                lastLoadedTiles.clear();

                for (File file : list) {
                    Path path = file.toPath();
                    lastLoadedTiles.add(path);
                }

                redrawTiles(borderPane, group, widthTextField, heightTextField);
            }
        });

        Button redrawButton = new Button("Redraw");
        redrawButton.setOnAction(e -> redrawTiles(borderPane, group, widthTextField, heightTextField));

        VBox vBox = new VBox();
        vBox.getChildren().add(fileChooserButton);
        vBox.getChildren().add(redrawButton);
        vBox.getChildren().add(createBoxWith(widthLabel, widthTextField));
        vBox.getChildren().add(createBoxWith(heightLabel, heightTextField));
        vBox.setSpacing(5.0);
        vBox.setPadding(new Insets(5.0));

        borderPane.setLeft(vBox);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Salad");
        primaryStage.show();
    }

    private HBox createBoxWith(Label label, TextField inputField) {
        HBox widthLine = new HBox(5.0, label, inputField);
        widthLine.setAlignment(Pos.CENTER);
        return widthLine;
    }

    private void redrawTiles(BorderPane borderPane, TilesGroup group, TextField widthTextField, TextField heightTextField) {
        tiles.clear();
        for (Path path : lastLoadedTiles) {
            try {
                getTilesFromPath(path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Group fxGroup = group.resizeAndExport(widthTextField.getText(), heightTextField.getText(), tiles).getTilesGroup();
        borderPane.setCenter(fxGroup);
    }

    private void getTilesFromPath(Path path) throws IOException {
        InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
        tiles.add(new Image(is));
        is.close();
    }
}
