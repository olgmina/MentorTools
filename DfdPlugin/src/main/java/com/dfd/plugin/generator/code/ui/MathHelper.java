package com.dfd.plugin.generator.code.ui;

import java.awt.*;
import java.util.ArrayList;

public class MathHelper {

    public static boolean isPointOnSection(Point p, Point p1, Point p2){ // точка на отрезке
        if(((Math.min(p1.x, p2.x) <= p.x) && (p.x <= Math.max(p1.x, p2.x)))
            && (Math.min(p1.y, p2.y) <= p.y) && (p.y <= Math.max(p1.y, p2.y))){
            return true;
        }
        return false;
    }

//    public static boolean isPointOnSection(Point p, Point p1, Point p2){ // точка на отрезке
//        double k, c;
//
//        if (p2.x == p1.x) {
//            return (p.x == p1.x && p.y >= Math.min(p1.y, p2.y) && p1.x <= Math.max(p1.y, p2.y));
//        }
//
//        k = (p2.y - p1.y) / (p2.x - p1.x);
//        c = p1.y - k * p1.x;
//
//        return p.y == p.x * k + c;
//    }

//    public static boolean isIntersect(Point p, Rectangle rectangle){
//        if((p.x >= rectangle.x && p.x <= rectangle.x + rectangle.width)
//                && (p.y >= rectangle.y && p.y <= rectangle.y + rectangle.height)){
//            return true;
//        }
//        return false;
//    }

    public static Point getPointIntersectLines(MathLine f, MathLine s){ // точка пересечения двух прямых
        double d = f.getA() * s.getB() - s.getA() * f.getB();
        if(d == 0){
            return null;
        }
        Point p = new Point();
        p.x = (int)((f.getB() * s.getC() - s.getB() * f.getC()) / d);
        p.y = (int)((f.getC() * s.getA() - s.getC() * f.getA()) / d);
        return p;
    }

    public static MathLine createLine(Point p1, Point p2){ // создать прямую
        int a = p1.y - p2.y;
        int b = p2.x - p1.x;
        int c = p1.x * p2.y - p2.x * p1.y;
        return new MathLine(a, b, c);
    }

    public static Point getVectorFromTwoPoint(Point p1, Point p2){ // создать вектор по двум точкам
        return new Point(p2.x - p1.x, p2.y - p1.y);
    }

    public static double lengthVector(Point v){ // длина вектора
        return Math.sqrt(v.x * v.x + v.y*v.y);
    }

    public static Point sumVector(Point p1, Point p2){ // сумма векторов
        return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    public static Point subVector(Point p1, Point p2){ // разность векторов
        return new Point(p1.x - p2.x, p1.y - p2.y);
    }

    public static Point mulVector(Point p, double c){ // умножение вектора на число
        return new Point((int)(p.x * c), (int)(p.y * c));
    }

    public static boolean isPointOnLine(Point p, Point p1, Point p2){ // лежит ли точка на отрезке
        double xDiff = p2.x - p1.x;
        double yDiff = p2.y - p1.y;
        double distance = Math.abs(xDiff*(p2.y - p.y) -
                (p2.x - p.x)*yDiff) /
                Math.sqrt(xDiff*xDiff + yDiff*yDiff);
        if (distance <= 5) {
            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                if (((p.x < p1.x) && (p.x > p2.x)) || ((p.x > p1.x) && (p.x < p2.x))) {
                    return true;
                }
            }
            else if (((p.y < p1.y) && (p.y > p2.y)) || ((p.y > p1.y) && (p.y < p2.y))) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Double> solveQuadraticEquation(double A, double B, double C){
        double D = (B * B) - 4*A*C;
        if (D < 0) {
                return new ArrayList<>();
            } else if (D == 0) {
                // одна точка
                double x1 = (-B * 2) / (2 * A);
                return new ArrayList<>(){{add(x1);}};
            } else {
                // две точки
                double x1 = (-B - Math.sqrt(D)) / (2 * A);
                double x2 = (-B + Math.sqrt(D)) / (2 * A);
                return new ArrayList<>(){{add(x1);add(x2);}};
        }
    }
}
