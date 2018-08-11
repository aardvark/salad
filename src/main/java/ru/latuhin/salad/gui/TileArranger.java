package ru.latuhin.salad.gui;

import javafx.scene.Group;
import javafx.scene.image.Image;

import java.util.Deque;

@FunctionalInterface
public interface TileArranger {
  Group arrangeTiles(Deque<Image> imagesRepo);
}
