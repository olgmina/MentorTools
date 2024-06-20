package org.example.lectorbots.view.pens;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MRing extends MPoint  {
    double radius;

    public MRing(double x, double y, Color color, double radius) {
        super( x, y, color);
        this.radius = radius;
        name="Клякса";
    }

    public boolean contains(double x, double y) {

        if (x >= getX() && x <= getX() + radius && y >= getY() && y <= getY() + radius) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "0";
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(color);
        context.fillOval(x, y, radius, radius);


    }
}
