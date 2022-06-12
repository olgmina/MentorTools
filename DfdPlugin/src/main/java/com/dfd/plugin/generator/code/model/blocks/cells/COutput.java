package com.dfd.plugin.generator.code.model.blocks.cells;

public class COutput extends Cell {

    private static String name = "outputData";

    public COutput() {
        super();
    }

    public COutput(String v) {
        setData(v);
    }

    @Override
    public void setData(String v){
        String newValue = replaceValue(v);
        if(newValue == null){
            newValue = name;
        }
        setValue(newValue);
    }
}
