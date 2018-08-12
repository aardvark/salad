package ru.latuhin.salad.gui.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class DragAndDropDemo extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    BorderPane borderPane = new BorderPane();
    borderPane.setPrefWidth(500);
    borderPane.setPrefHeight(500);

    TextField centerNode = new TextField("This is a test");

    centerNode.setOnDragOver(event -> {
      event.acceptTransferModes(TransferMode.ANY);
      List<File> files = event.getDragboard().getFiles();
      String filePath = files.get(0).toPath().toString();
      centerNode.setText(filePath);
      event.consume();
    });

    borderPane.setCenter(centerNode);

    Scene scene = new Scene(borderPane);

    primaryStage.setScene(scene);
    primaryStage.setTitle("Drag&Drop Demo");
    primaryStage.show();
  }
}
