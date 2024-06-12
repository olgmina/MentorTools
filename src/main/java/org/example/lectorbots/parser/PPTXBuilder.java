package org.example.lectorbots.parser;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.poi.sl.usermodel.Notes;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.example.lectorbots.HelloApplication;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;



public class PPTXBuilder implements Aggregate {
    private List<XSLFSlide> slides;

    public PPTXBuilder(String path) throws FileNotFoundException {
        try {
            FileInputStream fis = new FileInputStream(path);
            XMLSlideShow ppt = new XMLSlideShow(fis);
            fis.close();
            slides = ppt.getSlides();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator getIterator() {
        return new PPTXIterator();
    }

    private class PPTXIterator implements Iterator {

        int count = 0;

        @Override
        public XSLFSlide getSlide(int index){
            return slides.get(index);
        }

        @Override
        public boolean hasNext() {
            if (count + 1 < slides.size())  return true;
            return false;
        }

        @Override
        public int getCurrent(){
            return count;
        }

        @Override
        public boolean hasPrev() {
            if (count - 1 >= 0)  return true;
            return false;
        }

        @Override
        public XSLFSlide Next() {
            if (hasNext()) return slides.get(++count);
            else{
                count = 0;
                return slides.get(count);
            }
        }

        @Override
        public XSLFSlide Prev() {
            if (hasPrev()) return slides.get(--count);
            else{
                count = slides.size();
                return slides.get(count - 1);
            }
        }
    }
    public Image toImage(XSLFSlide slide){
        BufferedImage fxImage = new BufferedImage(HelloApplication.SLIDE_WIDTH, HelloApplication.SLIDE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = fxImage.createGraphics();
        slide.draw(g2d);
        Image image = SwingFXUtils.toFXImage(fxImage, null);
        return image;
    }

    public String toText(XSLFSlide slide){
        Notes notes = slide.getNotes();
        List<TextParagraph> textParagraphs = notes.getTextParagraphs();
        StringBuilder texts = new StringBuilder();
        for (TextParagraph paragraph : textParagraphs) {
            texts.append(paragraph.toString());
            texts.append("/n");
        }
        return texts.toString();
    }

}
