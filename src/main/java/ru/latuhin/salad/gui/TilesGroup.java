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

    Group recreateTilesGroup() {
        Group root = new Group();
        if (tiles.isEmpty()) return root;

        final Random[] random = {new Random()};
        Deque<Image> imagesRepo = randomizeTiles(
                () -> tiles.get(random[0].nextInt(tiles.size()))
        );
        root.getChildren().add(arrangeTiles(imagesRepo));
        return root;
    }

    private Deque<Image> randomizeTiles(Supplier<Image> supplier) {
        long limit = (long) (Math.pow(getTotalWidth() + getTotalHeight(), 2) / Math.pow(tileSize, 2));
        Stream<Image> imageStream = Stream.generate(supplier).limit(limit);
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


    TilesGroup resizeAndExport(String width, String height, List<Image> tiles) {
        int newWidth = Integer.valueOf(width);
        int newHeight = Integer.valueOf(height);
        return new TilesGroup(newWidth, newHeight, tiles);
    }

    private double getTotalHeight() {
        return (height * tileSize);
    }

    private double getTotalWidth() {
        return (width * tileSize);
    }
}
