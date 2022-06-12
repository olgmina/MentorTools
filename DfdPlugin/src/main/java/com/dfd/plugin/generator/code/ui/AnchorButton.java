package com.dfd.plugin.generator.code.ui;


import com.dfd.plugin.generator.code.ui.blocks.BlockUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AnchorButton extends JLabel implements MouseListener {

    private boolean pressed;
    private IChangeSize changSizeCallback;

    public AnchorButton(int cursor, IChangeSize changSizeCallback){
        this.changSizeCallback = changSizeCallback;
        setCursor(new Cursor(cursor));
        setOpaque(true);
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        addMouseListener(this);
    }

    public boolean isPressed() {
        return pressed;
    }

    public void changeSizeParentBlock(BlockUI block){
        changSizeCallback.changeSize(block);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            pressed = true;
            System.out.println("Pressed!");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            pressed = false;
            System.out.println("Released!");
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
