package org.example.lectorbots.parser;


import org.apache.poi.xslf.usermodel.XSLFSlide;

public interface Iterator {
    public XSLFSlide getSlide(int index);
    public boolean hasNext();
    public boolean hasPrev();
    public XSLFSlide Next();
    public XSLFSlide Prev();
    public int getCurrent();
}
