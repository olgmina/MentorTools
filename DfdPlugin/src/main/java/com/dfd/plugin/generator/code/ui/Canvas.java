package com.dfd.plugin.generator.code.ui;

import com.dfd.plugin.generator.code.ui.blocks.BlockType;
import com.dfd.plugin.generator.code.ui.blocks.BlockUI;
import com.dfd.plugin.generator.code.ui.connectors.Connector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Canvas extends JPanel implements MouseListener{

    // меню
    private JMenuBar menu;
    private JPopupMenu contextMenu;
    // блоки
    private ArrayList<BlockUI> blocks;
    // глобальные координаты мышки
    private int mouseX = -1;
    private int mouseY = -1;
    // запоминаем координаты мыши по нажатию на полотно
    private int mouseStartX = -1;
    private int mouseStartY = -1;
    // старая позиция блока для перемещения
    private int oldPositionSelectedBlockX = -1;
    private int oldPositionSelectedBlockY = -1;
    // соединительные линии
    private boolean drawNewConnector = false;
    private ArrayList<Connector> connectors;
    private BlockUI firstConnectionBlockTmp;

    public Canvas(){
        super();
        setPreferredSize(new Dimension(3000, 3000));
        setLayout(null);
        addMouseListener(this);
        blocks = new ArrayList<>();
        connectors = new ArrayList<>();

        long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
            //System.out.println(e.getID());
            if(e.getID() == 501 && getMousePosition() != null){ // mouse pressed
                mouseStartX = getMousePosition().x;
                mouseStartY = getMousePosition().y;
                var block = blocks.stream().filter(ev -> ev.isIntersect(mouseStartX, mouseStartY)).findFirst();
                if(isDrawNewConnector()){
                    disableDrawNewConnector();
                    if(block.isEmpty()){
                        revalidate();
                        repaint();
                        return;
                    }
                    addConnector(block.get());
                }
                for(var conn: connectors){
                    conn.clearSelect();
                }
                revalidate();
                repaint();
                var connect = connectors.stream().filter(ev -> ev.isIntersect(mouseStartX, mouseStartY)).findFirst();
                if(connect.isEmpty()){
                    return;
                }
                connect.get().select();
                revalidate();
                repaint();
            }
            else if(e.getID() == 506 && getMousePosition() != null){ // mouse move & pressed
                mouseX = getMousePosition().x;
                mouseY = getMousePosition().y;
                var block = blocks.stream().filter(BlockUI::isSelected).findFirst();
                if(block.isEmpty()){
                    return;
                }
                if(block.get().changeSizeFromAnchor()){
                    revalidate();
                    repaint();
                    return;
                }
                block.get().setLocation(oldPositionSelectedBlockX + (mouseX - mouseStartX),oldPositionSelectedBlockY + (mouseY - mouseStartY));
                revalidate();
                repaint();
            }else if(e.getID() == 503 && getMousePosition() != null){
                mouseX = getMousePosition().x;
                mouseY = getMousePosition().y;
//                System.out.println("start: " + mouseStartX + ", " + mouseStartY);
//                System.out.println("end: " + mouseX + ", " + mouseY);
                if(isDrawNewConnector()){
                    revalidate();
                    repaint();
                }
            }
        }, eventMask);

        contextMenu = new JPopupMenu();
        JMenuItem remove = new JMenuItem("Remove");
        remove.addActionListener(e -> {
            var connect = connectors.stream().filter(Connector::isSelected).findFirst();
            if(connect.isEmpty()){
                return;
            }
            connectors.remove(connect.get());
        });
        contextMenu.add(remove);
    }

    public void addBlock(BlockUI block){
        blocks.add(block);
    }

    public boolean isDrawNewConnector() {
        return drawNewConnector;
    }

    public void enableDrawNewConnector() {
        this.drawNewConnector = true;
    }

    public void disableDrawNewConnector() {
        this.drawNewConnector = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));

        // connectors
        for(var connector: connectors){
            connector.recalculateShape();
            connector.drawConnector(g2d);
        }
        if(isDrawNewConnector()){
            g2d.drawLine(mouseStartX, mouseStartY, mouseX, mouseY);
        }
    }

    public void clearSelectedAll(){
        for(var block: blocks){
            block.clearSelected();
        }
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public ArrayList<BlockUI> getBlocks() {
        return blocks;
    }

    public void setFirstConnectionBlockTmp(BlockUI block){
        firstConnectionBlockTmp = block;
    }

    public void setOldPositionSelectedBlock(int x, int y){
        oldPositionSelectedBlockX = x;
        oldPositionSelectedBlockY = y;
    }

    public void addConnector(BlockUI second){
        if(firstConnectionBlockTmp != second) {
            connectors.add(new Connector(firstConnectionBlockTmp, second));
        }
    }

    public JMenuBar getMenu() {
        return menu;
    }

    public ArrayList<Connector> getConnectors() {
        return connectors;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clearSelectedAll();
        if (e.getButton() == MouseEvent.BUTTON3) {
            contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
//            BlockUI block = new BlockUI();
//            //block.init(new ProcessUI());
//            block.init(new InputOutputUI("src/images/input_icon.png"));
//            //block.init(new InputOutputUI("src/images/output_icon.png"));
//            //block.init(new ExternalStorageUI());
//            add(block);
//            blocks.add(block);
//            block.setBounds(e.getX(), e.getY(), block.getWidth(), block.getHeight());
//            revalidate();
//            repaint();
//        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
