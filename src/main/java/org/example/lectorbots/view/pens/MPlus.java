package org.example.lectorbots.view.pens;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MPlus extends MPoint{

    private double xp, xm,yp,ym;

    public MPlus(double x, double y, Color color) {
        super(x, y, color);
        xp=2;
        xm=2;
        yp=2;
        ym=3;
        name="Плюс";
    }

    public MPlus(double x, double y, Color color, double xp, double xm, double yp, double ym) {
        super(x, y, color);
        this.xp = xp;
        this.xm = xm;
        this.yp = yp;
        this.ym = ym;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.strokeLine(x,y-ym, x, y+yp);
        gc.strokeLine(x-xm,y, x+xp, y);
    }

    @Override
    public String toString() {
        return "+";
    }
}
