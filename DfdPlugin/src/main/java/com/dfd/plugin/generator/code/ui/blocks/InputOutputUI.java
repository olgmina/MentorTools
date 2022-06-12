package com.dfd.plugin.generator.code.ui.blocks;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class InputOutputUI extends JPanel implements IAdapterUI{

    private int strokeBorder = 3;
    private JBLabel textShowing;
    private String text;
    private Point minSizes = new Point(200, 100);

    public InputOutputUI(String pathToIcon){
        super();
        setBorder(new EmptyBorder(strokeBorder * 2,strokeBorder * 2,strokeBorder * 2,strokeBorder * 2));
        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        JLabel icon = new JLabel(new ImageIcon(pathToIcon), SwingConstants.LEFT);
        icon.setOpaque(false);
        if(icon.getIcon() == null){
            System.out.println("NULL!");
        }
        add(icon, BorderLayout.LINE_END);

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
        g2d.fillRect(strokeBorder,strokeBorder, getWidth() - strokeBorder * 2, getHeight() - strokeBorder * 2);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(strokeBorder,strokeBorder, getWidth() - strokeBorder * 2, getHeight() - strokeBorder * 2);
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
