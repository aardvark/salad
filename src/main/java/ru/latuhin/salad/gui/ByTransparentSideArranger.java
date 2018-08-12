package ru.latuhin.salad.gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class ByTransparentSideArranger implements TileArranger {
  private final double tileSize;
  private final double totalHeight;
  private final double totalWidth;
  private final double height;
  private final double width;

  public ByTransparentSideArranger(double tileSize, double height, double width) {
    this.tileSize = tileSize;
    this.totalHeight = height * tileSize;
    this.height = height;
    this.totalWidth = width * tileSize;
    this.width = width;
  }

  @Override
  public List<Node> arrangeTiles(Deque<Image> imagesRepo) {
    List<Node> images = new ArrayList<>();
    double xStart = 0.0;
    int[] shifts = findShifts(imagesRepo.peekFirst());
    Number xShift = shifts[0];
    Number yShift = shifts[1];
    for (double y = 0.0; y < totalHeight; y = y + yShift.doubleValue()) {
      for (double x = xStart; x < totalWidth; x = x + tileSize) {
        ImageView imageView = new ImageView();
        Image image = imagesRepo.pop();
        imageView.setImage(image);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setFitHeight(tileSize);
        imageView.setFitWidth(tileSize);
        images.add(imageView);
      }
      xStart = xStart == 0.0 ? xShift.doubleValue() : 0.0;
    }
    return images;
  }

  /**
   * Assume that all images have same form find an effective transparency triangle
   * <p>
   * XXXB
   * XX
   * X
   * A  C
   *
   * @param image
   * @return
   */
  private int[] findShifts(Image image) {
    PixelReader pixelReader = image.getPixelReader();
    Number height = image.getHeight() - 1;
    Number width = image.getWidth() - 1;
    // proceed to scanline pixels to the left until we find non transparent pixel
    int[] a = new int[2];
    for (int y = height.intValue(); y > 0; y--) {
      for (int x = width.intValue(); x > 0; x--) {
        if (!isTransparent(pixelReader.getColor(x, y))) {
          a[0] = x;
          a[1] = y;
          break;
        }
      }
      if (a[0] != 0) {
        break;
      }
    }
    if (a[0] == 0) {
      //      "Image is fully transparent nothing to patch"
      return new int[]{height.intValue()/2, height.intValue()/2};
    }
    System.out.printf("A[%s,%s]", a[0], a[1]);
    //assume here we found our first non transparent pixel
    //now we will scanline up from or point to find another transparent pixel

    int[] b = new int[2];
    for (int x = width.intValue(); x > 0; x--) {
      for (int y = a[1]; y > 0; y--) {
        if (!isTransparent(pixelReader.getColor(x, y))) {
          b[0] = x;
          b[1] = y;
          break;
        }
      }
      if (b[0] != 0) {
        break;
      }
    }
    System.out.printf("B[%s,%s]", b[0], b[1]);

    int[] c = new int[2];
    c[0] = a[1];
    c[1] = b[0];
    System.out.printf("C[%s,%s]", c[0], c[1]);

    int ac = c[0] - a[0];
    int cb = c[1] - b[1];

    int[] shifts = new int[2];
    shifts[0] = cb + 1;
    shifts[1] = ac + 1;

    return shifts;
  }

  private boolean isTransparent(Color color) {
    return color.getOpacity() == 0.0;
  }

}
