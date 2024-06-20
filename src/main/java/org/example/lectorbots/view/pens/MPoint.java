package org.example.lectorbots.view.pens;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MPoint {
    protected double x,y;
    protected Color color;
    protected String name;

    public MPoint(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public MPoint(double x, double y) {
        this.x = x;
        this.y = y;
        this.color = Color.BLACK;
    }

    @Override
    public MPoint clone() {
        try {
            return (MPoint) super.clone();
        } catch (CloneNotSupportedException e) {
            // Обработка исключения
            return null;
        }
    }


    public void draw(GraphicsContext gc) {
        // без абстрактного класса
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
