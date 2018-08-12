package ru.latuhin.salad.gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TilesGroup {

  private final int width;
  private final int height;
  private final double tileSize;
  private final List<Image> tiles;

  TilesGroup(int width, int height, List<Image> tiles) {
    this.width = width;
    this.height = height;
    this.tiles = tiles;
    if (!tiles.isEmpty()) {
      this.tileSize = tiles.get(0).getHeight();
    } else {
      this.tileSize = 0.0;
    }
  }

  static TilesGroup resizeAndExport(String width, String height, List<Image> tiles) {
    int newWidth = Integer.valueOf(width);
    int newHeight = Integer.valueOf(height);
    return new TilesGroup(newWidth, newHeight, tiles);
  }

  List<Node> recreateTilesGroup(boolean useTransparency) {
    final Random[] random = {new Random()};
    Deque<Image> imagesRepo = randomizeTiles(() -> tiles.get(random[0].nextInt(tiles.size())));
    return arrangeTilesGroup(imagesRepo, useTransparency);
  }

  private List<Node> arrangeTilesGroup(Deque<Image> imagesRepo, boolean useTransparency) {
    if (useTransparency) {
      return new ByTransparentSideArranger(tileSize, height, width).arrangeTiles(imagesRepo);
    } else {
      return new BlockArranger(tileSize, height, width).arrangeTiles(imagesRepo);
    }
  }

  private Deque<Image> randomizeTiles(Supplier<Image> supplier) {
    long limit = (long) (Math.pow(getTotalWidth() + getTotalHeight(), 2) / Math.pow(tileSize, 2));
    Stream<Image> imageStream = Stream.generate(supplier).limit(limit);
    return imageStream.collect(Collectors.toCollection(ArrayDeque::new));
  }

  private double getTotalHeight() {
    return (height * tileSize);
  }

  private double getTotalWidth() {
    return (width * tileSize);
  }

}
