package ru.latuhin.salad.gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Deque;
import java.util.List;

@FunctionalInterface
public interface TileArranger {
  List<Node> arrangeTiles(Deque<Image> imagesRepo);
}
