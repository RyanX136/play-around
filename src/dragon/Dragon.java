package dragon;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;

public class Dragon {
    private CanvasWindow canvas = new CanvasWindow("Dragon", 1000, 1000);
    private float hue = 0.75f;

    Dragon() {
        canvas.setBackground(Color.BLACK);
        // Draw dragon at increasing levels of recursion
        for (int levels = 0; levels < 20; levels++) {
            canvas.removeAll();
            draw(new Point(300, 500), 0, 500, levels, 1);
            canvas.draw();
            System.out.println(levels);
            canvas.pause(1000);
        }
    }

    private void draw(Point p, double angle, double step, int levels, float hueStep) {
        if (levels > 0) {
            // Each recursive level is made out of two smaller copies of the dragon
            double smallerStep = step * Math.sqrt(.3);
            draw(p, angle + 75, smallerStep, levels-1, hueStep / 2);
            draw(endpoint(p, angle, step), angle + 135, smallerStep, levels-1, hueStep / 2);
        } else {
            // At the lowest recursive level, we build our dragon out of straight lines
            Point q = endpoint(p, angle, step);
            Line line = new Line(p.getX(), p.getY(), q.getX(), q.getY());
            line.setStrokeColor(Color.getHSBColor(hue, 1, 1));
            line.setStrokeWidth(Math.max(0.5, step / 5));
            hue += hueStep;
            canvas.add(line);
        }
    }

    private Point endpoint(Point p, double angle, double step) {
        return p.add(Point.atAngle(Math.toRadians(angle)).scale(step));
    }

    public static void main(String[] args) {
        new Dragon();
    }
}
