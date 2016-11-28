package ru.latuhin.salad.tiles;

import javafx.scene.image.Image;

import java.nio.file.Path;

public class TileMetadata {
    private final Path path;
    private final Image image;

    public TileMetadata(Path path, Image image) {
        this.path = path;
        this.image = image;
    }

    public String getPath() {
        return path.toUri().toASCIIString();
    }

    public Image getImage() {
        return image;
    }
}
