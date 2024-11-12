package flame;

import java.util.List;
import java.util.Random;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Image.PixelFormat;
import transform.Squinch;
import transform.PointTransform;
import transform.Warble;

public class Flame {
    private static final int ITER_BATCH_SIZE = 20000;

    private final int size;
    private final double scale;
    private final int[] histogram;
    private final Random rand;
    
    private List<PointTransform> transforms;

    public static void main(String[] args) {
        int size = 640;
        CanvasWindow canvas = new CanvasWindow("hi", size, size);
        Flame flame = new Flame(size, 1);

        while(true) {
            Image image = flame.render();
            canvas.removeAll();
            canvas.add(image);
            canvas.draw();
        }
    }

    public Flame(int size, double scale) {
        this.size = size;
        this.scale = scale;
        
        histogram = new int[size * size];
        rand = new Random();
        transforms = List.of(
            // THINGS TO TRY:
            // - Change parameters
            // - Add a Rotate transform
            // - Try different subsets of transforms
            // - Try two of same transform w/different params
            new Warble(200000, 3000000),
            new Squinch(0.01, 0.3)
        );
    }

    public Image render() {
        // Apply randomly selected transforms to point, gather 2D histogram

        Point p = Point.ORIGIN;
        for (int i = 0; i < ITER_BATCH_SIZE; i++) {
            PointTransform transform = transforms.get(rand.nextInt(transforms.size()));
            p = transform.apply(p);

            incrementPixel(p.getX(), p.getY());
        }

        // Convert histogram to colors

        float[] pixels = new float[histogram.length * 3];
        for (int i = 0; i < histogram.length; i++) {
            double z = Math.log(histogram[i] + 1) / 10;
            pixels[i * 3 + 0] = colorCurve(z, 10, 40, 0.9);
            pixels[i * 3 + 1] = colorCurve(z, 20, 300, 1.5);
            pixels[i * 3 + 2] = colorCurve(z, 60, 4, 2);
        }
        return new Image(size, size, pixels, PixelFormat.RGB);
    }

    // Maps a scaled histogram value z to a color, using color curve parameters a, b, c
    private float colorCurve(double z, double a, double b, double c) {
        return (float)
            Math.pow(
                square(
                    Math.sin(
                        b * Math.pow(z, c))),
                a);
    }

    private double square(double x) {
        return x * x;
    }

    private void incrementPixel(double x, double y) {
        int xi = (int) ((x * scale + 1) / 2 * size);
        int yi = (int) ((y * scale + 1) / 2 * size);
        if (xi < 0 || xi >= size || yi < 0 || yi >= size) {
            return;
        }
        histogram[xi + yi * size] += 20;
    }
}
