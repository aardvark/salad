package ru.latuhin.salad.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
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
import ru.latuhin.salad.tiles.TileMetadata;

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
    private List<TileMetadata> tileMetaDatas = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(500);
        borderPane.setPrefHeight(500);

        int width = 4;
        int height = 4;
        TilesGroup group = new TilesGroup(width, height, tiles);
        borderPane.setCenter(group.recreateTilesGroup());

        Label columnsLabel = new Label("Columns");
        TextField inputColumnsField = new TextField(String.valueOf(width));
        inputColumnsField.setPrefWidth(50);

        Label rowsLabel = new Label("Rows");
        TextField inputRowsField = new TextField(String.valueOf(height));
        inputRowsField.setPrefWidth(50);

        final FileChooser fileChooser = new FileChooser();
        Button fileChooserButton = new Button("Choose tiles");

        Label metaDataLabel = new Label(getMetaDataPath());

        fileChooserButton.setOnAction(e -> {
            List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
            if (list != null && !list.isEmpty()) {
                lastLoadedTiles.clear();

                for (File file : list) {
                    Path path = file.toPath();
                    lastLoadedTiles.add(path);
                }

                redrawTiles(borderPane, inputColumnsField, inputRowsField, metaDataLabel);
            }
        });

        Button shuffle = new Button("Shuffle");
        shuffle.setOnAction(e -> redrawTiles(borderPane, inputColumnsField, inputRowsField, metaDataLabel));

        VBox vBox = new VBox();

        List<Node> children = vBox.getChildren();
        children.add(fileChooserButton);
        children.add(metaDataLabel);
        children.add(new Label("Arrange tiles in:"));
        children.add(createBoxWith(rowsLabel, inputRowsField));
        children.add(createBoxWith(columnsLabel, inputColumnsField));
        children.add(shuffle);

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

    private void redrawTiles(BorderPane borderPane, TextField columnsInput, TextField rowsInput, Label metaDataLabel) {
        tiles.clear();
        tileMetaDatas.clear();
        for (Path path : lastLoadedTiles) {
            try {
                InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
                Image image = new Image(is);
                tiles.add(image);
                tileMetaDatas.add(new TileMetadata(path, image));
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        metaDataLabel.setText(getMetaDataPath());
        Group tileGroup = TilesGroup.resizeAndExport(columnsInput.getText(), rowsInput.getText(), tiles).recreateTilesGroup();
        borderPane.setCenter(tileGroup);
    }

    private String getMetaDataPath() {
        return tileMetaDatas.isEmpty() ? "" : tileMetaDatas.get(0).getPath();
    }
}
