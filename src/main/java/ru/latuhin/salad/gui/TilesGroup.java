package ru.latuhin.salad.gui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TilesGroup {

    private double width;
    private double height;
    private double tileSize;

    public TilesGroup(double width, double height, double tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;
    }

    public Group getTilesGroup(List<Image> tiles) {
        Group root = new Group();
        if (tiles.isEmpty()) return root;

        tileSize = tiles.get(0).getWidth();
        final Random[] random = {new Random()};
        Deque<Image> imagesRepo = randomizeTiles(
                () -> tiles.get(random[0].nextInt(tiles.size()))
        );
        root.getChildren().add(arrangeTiles(imagesRepo));
        return root;
    }

    public void resizeAndExport(String width, String height) {
        this.setWidth(Double.valueOf(width));
        this.setHeight(Double.valueOf(height));
    }

    private Deque<Image> randomizeTiles(Supplier<Image> supplier) {
        double limit = Math.pow(getTotalWidth() + getTotalHeight(), 2) / Math.pow(tileSize, 2);
        Stream<Image> imageStream = Stream.generate(supplier).limit((long) limit);
        return imageStream.collect(Collectors.toCollection(ArrayDeque<Image>::new));
    }


    private Group arrangeTiles(Deque<Image> imagesRepo) {
        Group tilesGroup = new Group();
        for (double y = 0.0; y < getTotalHeight(); y = y + tileSize) {
            for (double x = 0.0; x < getTotalWidth(); x = x + tileSize) {
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

    private double getTotalHeight() {
        return (height * tileSize);
    }

    private double getTotalWidth() {
        return (width * tileSize);
    }


    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
