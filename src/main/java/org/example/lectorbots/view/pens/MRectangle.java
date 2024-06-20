package org.example.lectorbots.view.pens;

import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;


public class MRectangle extends MPoint {
    double height, weight;

    public MRectangle(double x, double y, Color color, double h, double w) {
        super( x, y, color);
        this.height=h;
        this.weight=w;
        name="Пиксель";
    }

    public MRectangle(Color color, double x, double y) {
        super( x, y, color);
        this.height=2;
        this.weight=4;
    }

    @Override
    public String toString() {
        return "-";
    }



    public boolean contains(double x, double y) {

        if (x >= getX() && x <= getX() + weight && y >= getY() && y <= getY() + height) {
            return true;
        } else {
            return false;
        }
    }
    public void resize(double scale) {
        weight*=scale;
        height*=scale;
    }

    public MRectangle clone() {

        MRectangle clone = new MRectangle( this.x, this.y, color, this.weight, this.height);

        return clone; }


    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, weight,height);
    }


}


