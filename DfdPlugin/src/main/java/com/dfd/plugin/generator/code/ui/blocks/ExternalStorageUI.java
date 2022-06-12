package com.dfd.plugin.generator.code.ui.blocks;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ExternalStorageUI extends JPanel implements IAdapterUI{

    private int strokeBorder = 4;
    private JBLabel textShowing;
    private String text;
    private Point minSizes = new Point(200, 100);

    public ExternalStorageUI(){
        super();
        setBorder(new EmptyBorder(strokeBorder * 2,strokeBorder * 2,strokeBorder * 2,strokeBorder * 2));
        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        textShowing = new JBLabel("", SwingConstants.CENTER);
        setText("");
        textShowing.setOpaque(false);
        add(textShowing, BorderLayout.CENTER);
        textShowing.setForeground(Color.BLACK);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(strokeBorder));
        g2d.setColor(new Color(169,169,169,255));
        g2d.fillRect(strokeBorder,strokeBorder, getWidth() - strokeBorder, getHeight() - strokeBorder * 2);
        g2d.setColor(Color.BLACK);
        g2d.drawLine(strokeBorder,strokeBorder, getWidth(), strokeBorder);
        g2d.drawLine(strokeBorder,strokeBorder, strokeBorder, getHeight() - strokeBorder * 2);
        g2d.drawLine(strokeBorder,getHeight() - strokeBorder, getWidth(), getHeight() - strokeBorder);

    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String str){
        text = str;
//        textShowing.setText("<html><p>" + str + "</p></html>");
        textShowing.setText(str);
    }

    @Override
    public Point getMinSizes() {
        return minSizes;
    }

}
