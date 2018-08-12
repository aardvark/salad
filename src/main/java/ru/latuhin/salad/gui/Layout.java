package ru.latuhin.salad.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.TransferMode;
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
  public void start(Stage primaryStage) {
    BorderPane borderPane = new BorderPane();
    borderPane.setPrefWidth(500);
    borderPane.setPrefHeight(500);

    int width = 4;
    int height = 4;
    TilesGroup group = new TilesGroup(width, height, tiles);

    List<Node> tiles = group.recreateTilesGroup(false);

    Group tileGroup = new Group(tiles);

    VBox centerBox = new VBox();
    centerBox.getChildren().add(tileGroup);
    centerBox.setStyle("-fx-border-color: black");
    borderPane.setCenter(centerBox);


    Label columnsLabel = new Label("Columns");
    TextField inputColumnsField = new TextField(String.valueOf(width));
    inputColumnsField.setPrefWidth(50);

    Label rowsLabel = new Label("Rows");
    TextField inputRowsField = new TextField(String.valueOf(height));
    inputRowsField.setPrefWidth(50);

    final FileChooser fileChooser = new FileChooser();
    Button fileChooserButton = new Button("Choose tiles");

    CheckBox byTransparencySwitch = new CheckBox("Merge tiles by transparency");

    //drag and drop
    centerBox.setOnDragOver(event -> {
      event.acceptTransferModes(TransferMode.ANY);
      List<File> files = event.getDragboard().getFiles();
      updateOnFileChanged(tileGroup, files, inputColumnsField.getText(), inputRowsField.getText(),
        byTransparencySwitch.isSelected());
      event.consume();
    });

    /* logic */
    fileChooserButton.setOnAction(e -> {
      List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
      if (list != null && !list.isEmpty()) {
        fileChooser.setInitialDirectory(list.get(0).getParentFile());
      }
      updateOnFileChanged(tileGroup, list, inputColumnsField.getText(), inputRowsField.getText(),
        byTransparencySwitch.isSelected());
    });

    Button shuffle = new Button("Shuffle");
    shuffle.setOnAction(e -> {
      updateChildren(tileGroup, inputColumnsField.getText(), inputRowsField.getText(),
        byTransparencySwitch.isSelected());
    });


    VBox vBox = new VBox();
    vBox.getChildren().add(fileChooserButton);
    vBox.getChildren().add(new Label("Arrange tiles in:"));
    vBox.getChildren().add(createBoxWith(rowsLabel, inputRowsField));
    vBox.getChildren().add(createBoxWith(columnsLabel, inputColumnsField));
    vBox.getChildren().add(shuffle);
    vBox.getChildren().add(byTransparencySwitch);
    vBox.setSpacing(5.0);
    vBox.setPadding(new Insets(5.0));

    borderPane.setLeft(vBox);

    Scene scene = new Scene(borderPane);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Salad");
    primaryStage.show();
  }

  private void updateOnFileChanged(Group centerGroup, List<File> list, String columnsFieldText, String rowsFieldText,
                                   boolean transparencySwitchSelected) {
    if (list != null && !list.isEmpty()) {
      lastLoadedTiles.clear();

      for (File file : list) {
        Path path = file.toPath();
        lastLoadedTiles.add(path);
      }

      updateChildren(centerGroup, columnsFieldText, rowsFieldText, transparencySwitchSelected);
    }
  }

  private void updateChildren(Node borderPaneCenter, String columnsFieldText, String rowsFieldText,
                              boolean transparencySwitchSelected) {
    List<Node> tileNodes = redrawTiles(transparencySwitchSelected, columnsFieldText, rowsFieldText);
    Group center = (Group) borderPaneCenter;
    center.getChildren().clear();
    center.getChildren().addAll(tileNodes);
  }

  private HBox createBoxWith(Label label, TextField inputField) {
    HBox widthLine = new HBox(5.0, label, inputField);
    widthLine.setAlignment(Pos.CENTER);
    return widthLine;
  }

  private List<Node> redrawTiles(boolean selected, String width, String height) {
    tiles.clear();
    for (Path path : lastLoadedTiles) {
      try {
        InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
        tiles.add(new Image(is));
        is.close();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    return TilesGroup.resizeAndExport(width, height, tiles).recreateTilesGroup(selected);
  }
}
