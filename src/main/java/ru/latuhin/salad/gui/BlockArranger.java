package ru.latuhin.salad.gui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Deque;

class BlockArranger implements TileArranger {
  private final double tileSize;
  private final double totalHeight;
  private final double totalWidth;


  public BlockArranger(double tileSize, double height, double width) {
    this.tileSize = tileSize;
    this.totalHeight = height * tileSize;
    this.totalWidth = width * tileSize;
  }

  @Override
  public Group arrangeTiles(Deque<Image> imagesRepo) {
    Group tilesGroup = new Group();
    for (double y = 0.0; y < totalHeight; y = y + tileSize) {
      for (double x = 0.0; x < totalWidth; x = x + tileSize) {
        ImageView imageView = new ImageView();
        imageView.setImage(imagesRepo.pop());
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitHeight(tileSize);
        imageView.setFitWidth(tileSize);
        tilesGroup.getChildren().add(imageView);
      }
    }
    return tilesGroup;
  }
}
