package com.dfd.plugin.generator.code.ui.connectors;

import com.dfd.plugin.generator.code.ui.MathHelper;
import com.dfd.plugin.generator.code.ui.blocks.BlockType;
import com.dfd.plugin.generator.code.ui.blocks.BlockUI;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Connector {

    private BlockUI first;
    private BlockUI second;
    private boolean select;
    private Point leftArrow;
    private Point rightArrow;
    private Point searchPointsFirst;
    private Point searchPointsSecond;

    public Connector(BlockUI first, BlockUI second) {
        this.first = first;
        this.second = second;
        recalculateShape();
    }

    public BlockUI getFirst() {
        return first;
    }

    public BlockUI getSecond() {
        return second;
    }

    private Point calculatePointsIntersectBlockForRect(Rectangle rect, Point blockForLength){
        //var rect = second.getRectangle();
        var coords = new ArrayList<Point>(){{
            add(new Point(rect.x, rect.y)); // lt
            add(new Point(rect.x + rect.width, rect.y)); // rt
            add(new Point(rect.x + rect.width, rect.y + rect.height)); // rb
            add(new Point(rect.x, rect.y + rect.height)); // lb
            add(new Point(rect.x, rect.y)); // lt
        }};
        Point out = null;
        double lengthVecMin = -1;
        var lineDraw = MathHelper.createLine(getFirst().getCenter(), getSecond().getCenter());
        for(int i = 0;i<coords.size()-1;++i){
            var lineTmp = MathHelper.createLine(coords.get(i), coords.get(i + 1));
            var pointIntersectLines = MathHelper.getPointIntersectLines(lineDraw, lineTmp);
            if(pointIntersectLines ==null){
                continue;
            }
            if(MathHelper.isPointOnLine(pointIntersectLines, coords.get(i), coords.get(i + 1))){
                double lengthVec = MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(blockForLength, pointIntersectLines));
                if(lengthVecMin == -1 || lengthVecMin > lengthVec){
                    lengthVecMin = lengthVec;
                    out = pointIntersectLines;
                }
            }
        }
        return out;
    }

//    private Point calculatePointsIntersectBlock(Rectangle rect, Point pt1, Point pt2, Point centerFirst){
//        Rectangle rectCoordinate = new Rectangle(rect.x, rect.y, rect.x+ rect.width, rect.y+ rect.height);// координаты эллипса
//        double _a = rect.width / 2.0; // полуось эллипса a
//        double _b = rect.height / 2.0; // полуось эллипса b
////        _a = Math.max(_a, _b); // большая полуось эллипса
////        _b = Math.min(_a, _b); // меньшаяполуось эллипса
//        double x0 = (rectCoordinate.x + rectCoordinate.width) /2.0; // x центр эллипса
//        double y0 = (rectCoordinate.y + rectCoordinate.height) /2.0; // y центр эллипса
//        // уравнение прямой
//        double k = (pt2.y - pt1.y) / (double)(pt2.x - pt1.x);
//        double b = pt1.y - k * pt1.x;
//        // уравнение эллипса
//        BigDecimal v = BigDecimal.valueOf(_a).pow(2).multiply(BigDecimal.valueOf(_b).pow(2));
//        BigDecimal s = BigDecimal.valueOf(b).subtract(BigDecimal.valueOf(y0));
//        // собранное квадратное уравнение
//        BigDecimal A;
//        BigDecimal B;
//        BigDecimal C;
//        if(pt2.x - pt1.x != 0) {
//            A = BigDecimal.valueOf(_a).pow(2).add(BigDecimal.valueOf(_b).pow(2).multiply(BigDecimal.valueOf(k).pow(2)));
//            B = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(_a).pow(2).multiply(BigDecimal.valueOf(k)).multiply(s).subtract(BigDecimal.valueOf(_b).pow(2).multiply(BigDecimal.valueOf(x0))));
//            C = BigDecimal.valueOf(_a).pow(2).multiply(BigDecimal.valueOf(x0).pow(2)).add(BigDecimal.valueOf(_b).pow(2).multiply(s.pow(2))).subtract(v);
//        }else{
//            BigDecimal w = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(pt1.x - x0).pow(2).divide(BigDecimal.valueOf(_a).pow(2))).multiply(BigDecimal.valueOf(_b).pow(2));
//            A = BigDecimal.valueOf(1);
//            B = BigDecimal.valueOf(-2 * y0);
//            C = BigDecimal.valueOf(y0).pow(2).subtract(w);
//        }
//        // решаем и выводим два корня
//        BigDecimal D = B.pow(2).subtract(BigDecimal.valueOf(4).multiply(A).multiply(C));
//        // подставляем и находим два x и два y
//        if(pt2.x - pt1.x != 0) {
//            int compare = D.compareTo(BigDecimal.valueOf(0));
//            if (compare < 0) {
//                return null;
//            } else if (compare == 0) {
//                // одна точка
//                BigDecimal x1 = B.negate().multiply(BigDecimal.valueOf(2)).divide(BigDecimal.valueOf(2).multiply(A), 2, RoundingMode.HALF_UP);
//                return new Point(x1.intValue(), BigDecimal.valueOf(k).multiply(x1).add(BigDecimal.valueOf(b)).intValue());
//            } else {
//                // две точки
//                MathContext mc = new MathContext(10);
//                BigDecimal x1 = B.negate().subtract(D.sqrt(mc)).divide(BigDecimal.valueOf(2).multiply(A), 2, RoundingMode.HALF_UP);
//                BigDecimal x2 = B.negate().add(D.sqrt(mc)).divide(BigDecimal.valueOf(2).multiply(A), 2, RoundingMode.HALF_UP);
//                BigDecimal y1 = BigDecimal.valueOf(k).multiply(x1).add(BigDecimal.valueOf(b));
//                BigDecimal y2 = BigDecimal.valueOf(k).multiply(x2).add(BigDecimal.valueOf(b));
//                Point first = new Point(x1.intValue(), y1.intValue());
//                Point second = new Point(x2.intValue(), y2.intValue());
//                if (MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(first, centerFirst)) <
//                        MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(second, centerFirst))) {
//                    return first;
//                }
//                return second;
//            }
//        }else{
//            int compare = D.compareTo(BigDecimal.valueOf(0));
//            if (compare < 0) {
//                return null;
//            } else if (compare == 0) {
//                // одна точка
//                BigDecimal y1 = B.negate().multiply(BigDecimal.valueOf(2)).divide(BigDecimal.valueOf(2).multiply(A), 2, RoundingMode.HALF_UP);
//                return new Point(0, y1.intValue());
//            } else {
//                // две точки
//                MathContext mc = new MathContext(10);
//                BigDecimal y1 = B.negate().subtract(D.sqrt(mc)).divide(BigDecimal.valueOf(2).multiply(A), 2, RoundingMode.HALF_UP);
//                BigDecimal y2 = B.negate().add(D.sqrt(mc)).divide(BigDecimal.valueOf(2).multiply(A), 2, RoundingMode.HALF_UP);
//                double x1 = pt1.x;
//                double x2 = pt1.x;
//                Point first = new Point((int) x1, y1.intValue());
//                Point second = new Point((int) x2, y2.intValue());
//                if (MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(first, centerFirst)) <
//                        MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(second, centerFirst))) {
//                    return first;
//                }
//                return second;
//            }
//        }
//    }

    private Point calculatePointsIntersectBlock(Rectangle rect, Point pt1, Point pt2, Point centerFirst){
        Rectangle rectCoordinate = new Rectangle(rect.x, rect.y, rect.x+ rect.width, rect.y+ rect.height);// координаты эллипса
        double _a = rect.width / 2.0;
        double _b = rect.height / 2.0;
        double x0 = (rectCoordinate.x + rectCoordinate.width) /2.0; // x центр эллипса
        double y0 = (rectCoordinate.y + rectCoordinate.height) /2.0; // y центр эллипса
        // уравнение прямой
        double k = (pt2.y - pt1.y) / (double)(pt2.x - pt1.x);
        double b = pt1.y - k * pt1.x;
        // уравнение эллипса
        double v = (_a*_a)*(_b*_b);
        double s = b - y0;
        // собранное квадратное уравнение
        double A = -1;
        double B = -1;
        double C = -1;
        if(pt2.x - pt1.x != 0) {
            A = (_a * _a) + (_b * _b) * (k * k);
            B = 2 * ((_a * _a) * k * s - (_b * _b) * x0);
            C = (_a * _a) * (x0 * x0) + (_b * _b) * (s * s) - v;
        }else{
            double w = (1 - (Math.pow(pt1.x - x0, 2) / (_a*_a))) * (_b*_b);
            A = 1;
            B = -2 * y0;
            C = (y0 * y0) - w;
        }
        // решаем и выводим два корня
        double D = (B * B) - 4*A*C;
        // подставляем и находим два x и два y
        if(pt2.x - pt1.x != 0) {
            if (D < 0) {
                return null;
            } else if (D == 0) {
                // одна точка
                double x1 = (-B * 2) / (2 * A);
                return new Point((int) x1, (int) (k * x1 + b));
            } else {
                // две точки
                double x1 = (-B - Math.sqrt(D)) / (2 * A);
                double x2 = (-B + Math.sqrt(D)) / (2 * A);
                double y1 = k * x1 + b;
                double y2 = k * x2 + b;
                Point first = new Point((int) x1, (int) y1);
                Point second = new Point((int) x2, (int) y2);
                if (MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(first, centerFirst)) <
                        MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(second, centerFirst))) {
                    return first;
                }
                return second;
            }
        }else{
            if (D < 0) {
                return null;
            } else if (D == 0) {
                // одна точка
                double y1 = (-B * 2) / (2 * A);
                return new Point((int) 0, (int) y1);
            } else {
                // две точки
                double y1 = (-B - Math.sqrt(D)) / (2 * A);
                double y2 = (-B + Math.sqrt(D)) / (2 * A);
                double x1 = pt1.x;
                double x2 = pt1.x;
                Point first = new Point((int) x1, (int) y1);
                Point second = new Point((int) x2, (int) y2);
                if (MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(first, centerFirst)) <
                        MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(second, centerFirst))) {
                    return first;
                }
                return second;
            }
        }
    }

//    private Point calculatePointsIntersectBlock(Rectangle rect, Point pt1, Point pt2, Point centerFirst){
//        Rectangle rectCoordinate = new Rectangle(rect.x, rect.y, rect.x+ rect.width, rect.y+ rect.height);// координаты эллипса
//        double a = rect.width / 2.0;
//        double b = rect.height / 2.0;
//        double x0 = rectCoordinate.x + rectCoordinate.width /2.0; // x центр эллипса
//        double y0 = rectCoordinate.y + rectCoordinate.height /2.0; // y центр эллипса
//        double x1 = pt1.x;
//        double y1 = pt1.y;
//        double x2 = pt2.x;
//        double y2 = pt2.y;
//        // параметрическое (от t) уравнение прямой
//        // x = (x2 - x1)t + x1
//        // y = (y2 - y1)t + y1
//        //
//        // уравнение эллипса
//        // (x - x0)^2 / a^2 + (y - y0)^2 / b^2 = 1
//        // <->
//        // b^2 (x - x0)^2 + a^2 (y - y0)^2 = a^2 b^2
//        //
//        // подставляем прямую в эллипс
//        // b^2 ((x2 - x1)t + x1 - x0)^2 + a^2 ((y2 - y1)t + y1 - y0)^2 = a^2 b^2
//        // <-> (x21 := x2 - x1, y21 := y2 - y1, x10 := x1 - x0, y10 := y1 - y0)
//        // b^2 (x21 t + x10)^2 + a^2 (y21 t + y10)^2 = a^2 b^2
//        // <->
//        // (b^2 x21^2 + a^2 y21^2) t^2 +
//        // 2(b^2 x21 x10 + aa^2 y21 y10) t +
//        // (b^2 x10^2 + a^2 y10^2 - a^2 b^2) = 0
//
//        final double x21 = x2 - x1;
//        final double y21 = y2 - y1;
//
//        final double x10 = x1 - x0;
//        final double y10 = y1 - y0;
//
//        final double a2 = a * a;
//        final double b2 = b * b;
//
//        final ArrayList<Double> t = MathHelper.solveQuadraticEquation(
//                b2 * x21 * x21 + a2 * y21 * y21,
//                2 * (b2 * x21 * x10 + a2 * y21 * y10),
//                b2 * x10 * x10 + a2 * y10 * y10 - a2 * b2
//        );
//
//        // восстанавливаем точки пересечения
//        if(t.size() == 0){
//            return null;
//        }
//
//        final Point p[] = new Point[t.size()];
//
//        for (int i = 0; i < t.size(); ++i) {
//            p[i] = new Point((int) (t.get(i) * (x2 - x1) + x1), (int)(t.get(i) * (y2 - y1) + y1));
//        }
//
//        if(p.length == 1){
//            return p[0];
//        }
//
//        if (MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(p[0], centerFirst)) <
//                MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(p[1], centerFirst))) {
//            return p[0];
//        }
//        return p[1];
//
//    }

    public void recalculateShape(){

        if(getFirst().getBlockType() == BlockType.PROCESS){
            searchPointsFirst = calculatePointsIntersectBlock(first.getRectangle(), first.getCenter(), second.getCenter(), second.getCenter());
        }else {
            searchPointsFirst = calculatePointsIntersectBlockForRect(first.getRectangle(), getSecond().getCenter());
        }
        if(getSecond().getBlockType() == BlockType.PROCESS){
            searchPointsSecond = calculatePointsIntersectBlock(second.getRectangle(), first.getCenter(), second.getCenter(), first.getCenter());
        }else {
            searchPointsSecond = calculatePointsIntersectBlockForRect(second.getRectangle(), getFirst().getCenter());
        }

        Point rotatePoint;
        double lengthToNewPoint = MathHelper.lengthVector(MathHelper.getVectorFromTwoPoint(searchPointsSecond, getFirst().getCenter()));
        rotatePoint = MathHelper.mulVector(MathHelper.subVector(searchPointsSecond, getFirst().getCenter()), /*(lengthToNewPoint / 100 * 95)*/ (lengthToNewPoint - 20) / lengthToNewPoint);
        rotatePoint = MathHelper.sumVector(getFirst().getCenter(), rotatePoint);

        AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(30.f), searchPointsSecond.x, searchPointsSecond.y);
        leftArrow = new Point(0,0);
        transform.transform(new Point(rotatePoint.x, rotatePoint.y), leftArrow);

        transform = AffineTransform.getRotateInstance(Math.toRadians(-30.f), searchPointsSecond.x, searchPointsSecond.y);
        rightArrow = new Point(0,0);
        transform.transform(new Point(rotatePoint.x, rotatePoint.y), rightArrow);
    }

    public void drawConnector(Graphics2D g2d){
        if(select){
            g2d.setColor(Color.red);
        }else{
            g2d.setColor(Color.black);
        }
        //Set  anti-alias!
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawLine(searchPointsFirst.x, searchPointsFirst.y,
                searchPointsSecond.x, searchPointsSecond.y);

        g2d.drawLine(searchPointsSecond.x, searchPointsSecond.y, leftArrow.x, leftArrow.y);
        g2d.drawLine(searchPointsSecond.x, searchPointsSecond.y, rightArrow.x, rightArrow.y);
    }

    public boolean isIntersect(int x, int y){
//        if(getSecond().isSelected() || getFirst().isSelected()){
//            return false;
//        }
        return MathHelper.isPointOnLine(new Point(x, y), searchPointsFirst, searchPointsSecond);
        //return MathHelper.isPointOnSection(new Point(x, y), getFirst().getCenter(), getSecond().getCenter());
    }

    public void select(){
        select = true;
    }

    public void clearSelect(){
        select = false;
    }

    public boolean isSelected(){
        return select;
    }

}

