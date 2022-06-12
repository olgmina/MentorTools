package com.dfd.plugin.generator.code.model.blocks.cells;

public class CInput extends Cell {

    private static String name = "inputData";

    public CInput() {
        super();
    }

    public CInput(String v) {
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
