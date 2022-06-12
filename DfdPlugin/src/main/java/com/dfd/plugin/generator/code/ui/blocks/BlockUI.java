package com.dfd.plugin.generator.code.ui.blocks;

import com.dfd.plugin.generator.code.ui.AnchorButton;
import com.dfd.plugin.generator.code.ui.CanvasHelper;
import com.dfd.plugin.generator.code.ui.IChangeSize;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BlockUI extends JPanel implements MouseListener {

    private ArrayList<JComponent> allComponents;
    private ArrayList<AnchorButton> anchors;
    public static final int SIZE_BTN_ANCHOR = 8;
    private boolean selected = false;
    private ArrayList<Integer> cursors;
    private int minSizeHeight = -1;
    private int minSizeWidth = -1;
    private JPopupMenu contextMenu;
    private IAdapterUI centerPanel;
    private BlockType blockType;
    private static long ID = 0;
    private long currentID = 0;
    //private boolean isMouseOver = false;

    public void init(BlockType blockType){
        currentID = ID++;
        this.blockType = blockType;
        centerPanel = createBlock(blockType);
        setLayout(null);
        var minSizes = centerPanel.getMinSizes();
        minSizeWidth = minSizes.x;
        minSizeHeight = minSizes.y;
        setSize(new Dimension(minSizeWidth, minSizeHeight));
        allComponents = new ArrayList<>();
        anchors = new ArrayList<>();
        cursors = new ArrayList<>(){{
            add(Cursor.NW_RESIZE_CURSOR);
            add(Cursor.N_RESIZE_CURSOR);
            add(Cursor.NE_RESIZE_CURSOR);
            add(Cursor.E_RESIZE_CURSOR);
            add(-1);
            add(Cursor.E_RESIZE_CURSOR);
            add(Cursor.NE_RESIZE_CURSOR);
            add(Cursor.N_RESIZE_CURSOR);
            add(Cursor.NW_RESIZE_CURSOR);
        }};
        var sizes = getChangersSize();
        for(int i = 0;i<9;++i){
            JComponent component;
            if(i == 4){
                component = (JComponent) centerPanel;
            }else{
                AnchorButton anchorButton = new AnchorButton(cursors.get(i), sizes.get(i));
                component = anchorButton;
                anchors.add(anchorButton);
            }
            allComponents.add(component);
            add(component);
        }
        addMouseListener(this);
        contextMenu = new JPopupMenu();
        JMenuItem rename = new JMenuItem("Rename");
        rename.addActionListener(e -> {
            String s = (String)JOptionPane.showInputDialog(
                    null,
                    "Please, edit name class:\n",
                    "Renamer tool",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    centerPanel.getText());
            if ((s != null) && (s.length() > 0)) {
                centerPanel.setText(s);
            }
        });
        contextMenu.add(rename);
        JMenuItem remove = new JMenuItem("Remove");
        remove.addActionListener(e -> {
            var results = CanvasHelper.getInstance().getConnectors().stream().filter(ev -> ev.getFirst() == this).toArray();
            for(var conn: results){
                CanvasHelper.getInstance().getConnectors().remove(conn);
            }
            results = CanvasHelper.getInstance().getConnectors().stream().filter(ev -> ev.getSecond() == this).toArray();
            for(var conn: results){
                CanvasHelper.getInstance().getConnectors().remove(conn);
            }
            CanvasHelper.getInstance().remove(this);
            CanvasHelper.getInstance().getBlocks().remove(this);
            CanvasHelper.getInstance().revalidate();
            CanvasHelper.getInstance().repaint();
        });
        contextMenu.add(remove);
        JMenuItem connectTo = new JMenuItem("Connect to...");
        connectTo.addActionListener(e -> {
            CanvasHelper.getInstance().setFirstConnectionBlockTmp(this);
            CanvasHelper.getInstance().enableDrawNewConnector();
        });
        contextMenu.add(connectTo);
        resizeChildrens();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        resizeChildrens();
    }

    public String getTextBlock(){
        return centerPanel.getText();
    }

    ArrayList<Rectangle> getSizesWithAnchors(){
        return new ArrayList<>(){{
            add(new Rectangle(0, 0, SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR));
            add(new Rectangle(getWidth() / 2 - SIZE_BTN_ANCHOR /2, 0, SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR));
            add(new Rectangle(getWidth() - SIZE_BTN_ANCHOR, 0, SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR));
            add(new Rectangle(0, getHeight() / 2 - SIZE_BTN_ANCHOR /2, SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR));
            add(new Rectangle(SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR, getWidth() - SIZE_BTN_ANCHOR * 2, getHeight() - SIZE_BTN_ANCHOR * 2));
            add(new Rectangle(getWidth() - SIZE_BTN_ANCHOR, getHeight() / 2- SIZE_BTN_ANCHOR /2, SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR));
            add(new Rectangle(0, getHeight() - SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR));
            add(new Rectangle(getWidth() / 2- SIZE_BTN_ANCHOR /2, getHeight() - SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR));
            add(new Rectangle(getWidth() - SIZE_BTN_ANCHOR, getHeight() - SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR, SIZE_BTN_ANCHOR));
        }};
    }

    ArrayList<Rectangle> getSizesWithoutAnchors(){
        return new ArrayList<>(){{
            add(new Rectangle(0, 0, 0, 0));
            add(new Rectangle(0, 0, 0, 0));
            add(new Rectangle(0, 0, 0, 0));
            add(new Rectangle(0, 0, 0, 0));
            add(new Rectangle(0, 0, getWidth() - SIZE_BTN_ANCHOR, getHeight() - SIZE_BTN_ANCHOR));
            add(new Rectangle(0, 0, 0, 0));
            add(new Rectangle(0, 0, 0, 0));
            add(new Rectangle(0, 0, 0, 0));
            add(new Rectangle(0, 0, 0, 0));
        }};
    }

    void resizeChildrens(){
        ArrayList<Rectangle> sizeAnchors;
        if(selected) {
            sizeAnchors = getSizesWithAnchors();
        }else{
            sizeAnchors = getSizesWithoutAnchors();
        }
        for(int i = 0;i<sizeAnchors.size();++i){
            allComponents.get(i).setBounds(sizeAnchors.get(i));
        }
        revalidate();
        repaint();
    }

    public void clearSelected(){
        if(selected) {
            selected = false;
            setBounds(new Rectangle(getX() + SIZE_BTN_ANCHOR, getY() + SIZE_BTN_ANCHOR, getWidth() - SIZE_BTN_ANCHOR, getHeight() - SIZE_BTN_ANCHOR));
            resizeChildrens();
        }
    }

    public boolean isSelected(){
        return selected;
    }

    public ArrayList<AnchorButton> getAnchors() {
        return anchors;
    }

    public boolean changeSizeFromAnchor(){
        var btn = anchors.stream().filter(AnchorButton::isPressed).findFirst();
        if(btn.isEmpty()){
           return false;
        }
        btn.get().changeSizeParentBlock(this);
        revalidate();
        repaint();
        return true;
    }

    ArrayList<IChangeSize> getChangersSize(){
        return new ArrayList<>(){{
           add(block -> {
               int xShift = CanvasHelper.getInstance().getMouseX() - block.getLocation().x;
               int yShift = CanvasHelper.getInstance().getMouseY() - block.getLocation().y;
               int x = CanvasHelper.getInstance().getMouseX();
               int y = CanvasHelper.getInstance().getMouseY();
               int width = Math.max(block.getWidth() - xShift, minSizeWidth);
               int height = Math.max(block.getHeight() - yShift, minSizeHeight);
               if(block.getWidth() - xShift < minSizeWidth){
                   x = block.getLocation().x;
                   width = block.getWidth();
               }
               if(block.getHeight() - yShift < minSizeHeight){
                   y = block.getLocation().y;
                   height = block.getHeight();
               }
               block.setBounds(x, y, width, height);
           });
           add(block -> {
               int yShift = CanvasHelper.getInstance().getMouseY() - block.getLocation().y;
               int y = CanvasHelper.getInstance().getMouseY();
               int height = Math.max(block.getHeight() - yShift, minSizeHeight);
               if(block.getHeight() - yShift < minSizeHeight){
                   y = block.getLocation().y;
                   height = block.getHeight();
               }
               block.setBounds(block.getLocation().x, y, block.getWidth(), height);
           });
            add(block -> {
                int yShift = CanvasHelper.getInstance().getMouseY() - block.getLocation().y;
                int y = CanvasHelper.getInstance().getMouseY();
                int width = Math.max(CanvasHelper.getInstance().getMouseX() - block.getLocation().x, minSizeWidth);
                int height = Math.max(block.getHeight() - yShift, minSizeHeight);
                if(CanvasHelper.getInstance().getMouseX() - block.getLocation().x < minSizeWidth){
                    width = block.getWidth();
                }
                if(block.getHeight() - yShift < minSizeHeight){
                    y = block.getLocation().y;
                    height = block.getHeight();
                }
                block.setBounds(block.getLocation().x, y, width, height);
            });
            add(block -> {
                int xShift = CanvasHelper.getInstance().getMouseX() - block.getLocation().x;
                int x = CanvasHelper.getInstance().getMouseX();
                int width = Math.max(block.getWidth() - xShift, minSizeWidth);
                if(block.getWidth() - xShift < minSizeWidth){
                    x = block.getLocation().x;
                    width = block.getWidth();
                }
                block.setBounds(x, block.getLocation().y, width, block.getHeight());
            });
            add(block -> {
                return; // центральная панель, здесь не нужно
            });
            add(block -> {
                int width = Math.max(CanvasHelper.getInstance().getMouseX() - block.getLocation().x, minSizeWidth);
                if(CanvasHelper.getInstance().getMouseX() - block.getLocation().x < minSizeWidth){
                    width = block.getWidth();
                }
                block.setBounds(block.getLocation().x, block.getLocation().y, width, block.getHeight());
            });
            add(block -> {
                int xShift = CanvasHelper.getInstance().getMouseX() - block.getLocation().x;
                int x = CanvasHelper.getInstance().getMouseX();
                int width = Math.max(block.getWidth() - xShift, minSizeWidth);
                int height = Math.max(CanvasHelper.getInstance().getMouseY() - block.getLocation().y, minSizeHeight);
                if(block.getWidth() - xShift < minSizeWidth){
                    x = block.getLocation().x;
                    width = block.getWidth();
                }
                if(CanvasHelper.getInstance().getMouseY() - block.getLocation().y < minSizeHeight){
                    height = block.getHeight();
                }
                block.setBounds(x, block.getLocation().y, width, height);
            });
            add(block -> {
                int height = Math.max(CanvasHelper.getInstance().getMouseY() - block.getLocation().y, minSizeHeight);
                if(CanvasHelper.getInstance().getMouseY() - block.getLocation().y < minSizeHeight){
                    height = block.getHeight();
                }
                block.setBounds(block.getLocation().x, block.getLocation().y, block.getWidth(), height);
            });
            add(block -> {
                int height = Math.max(CanvasHelper.getInstance().getMouseY() - block.getLocation().y, minSizeHeight);
                int width = Math.max(CanvasHelper.getInstance().getMouseX() - block.getLocation().x, minSizeWidth);
                if(CanvasHelper.getInstance().getMouseY() - block.getLocation().y < minSizeHeight){
                    height = block.getHeight();
                }
                if(CanvasHelper.getInstance().getMouseX() - block.getLocation().x < minSizeWidth){
                    width = block.getWidth();
                }
                block.setBounds(block.getLocation().x, block.getLocation().y, width, height);
            });
        }};
    }

    public boolean isIntersect(int x, int y){
        if((x >= getLocation().x && x <= getLocation().x + getWidth())
                && (y >= getLocation().y && y <= getLocation().y + getHeight())){
            return true;
        }
        return false;
    }

    public void selected(){
        CanvasHelper.getInstance().clearSelectedAll();
        selected = true;
        setBounds(new Rectangle(getX() - SIZE_BTN_ANCHOR, getY() - SIZE_BTN_ANCHOR, getWidth() + SIZE_BTN_ANCHOR, getHeight() + SIZE_BTN_ANCHOR));
        CanvasHelper.getInstance().setOldPositionSelectedBlock(getLocation().x, getLocation().y);
        resizeChildrens();
    }

//    public Point getCenter(){
//        if(selected){
//            return new Point((getLocation().x + getLocation().x + getWidth() + SIZE_BTN_ANCHOR) / 2, (getLocation().y+ getLocation().y + getHeight() + SIZE_BTN_ANCHOR) / 2);
//        }
//        return new Point((getLocation().x + getLocation().x + getWidth()) / 2, (getLocation().y+ getLocation().y + getHeight()) / 2);
//    }

    public Point getCenter(){
        if(selected){
            return new Point(getLocation().x + (getWidth() + SIZE_BTN_ANCHOR) / 2, getLocation().y + (getHeight() + SIZE_BTN_ANCHOR) / 2);
        }
        return new Point(getLocation().x + getWidth() / 2, getLocation().y + getHeight() / 2);
    }

    public Rectangle getRectangle(){
        if(selected){
            return new Rectangle(getLocation().x + SIZE_BTN_ANCHOR, getLocation().y + SIZE_BTN_ANCHOR,
                    getWidth() - SIZE_BTN_ANCHOR * 2, getHeight() - SIZE_BTN_ANCHOR * 2);
        }
        return new Rectangle(getLocation().x, getLocation().y,
                getWidth() - SIZE_BTN_ANCHOR, getHeight() - SIZE_BTN_ANCHOR);
    }

    public IAdapterUI createBlock(BlockType blockType){
        if(blockType == BlockType.INPUT) {
            return new InputOutputUI(getClass().getClassLoader().getResource("icons/input_icon.png").getPath());
        }
        else if(blockType == BlockType.OUTPUT) {
            return new InputOutputUI(getClass().getClassLoader().getResource("icons/output_icon.png").getPath());
        }
        else if(blockType == BlockType.PROCESS) {
            return new ProcessUI();
        }
        else if(blockType == BlockType.EXTERNAL_STORAGE) {
            return new ExternalStorageUI();
        }
        return null;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public long getCurrentID() {
        return currentID;
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        if(CanvasHelper.getInstance().isDrawNewConnector()){
//            CanvasHelper.getInstance().addConnector(this);
//            CanvasHelper.getInstance().disableDrawNewConnector();
//        }
        selected();
        if(e.getButton() == MouseEvent.BUTTON3){
            contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //selected();
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