package com.dfd.plugin.generator.code.model.blocks.cells;

public class CProcess extends Cell {

    private static String name = "process";
    private static int number = 0;

    public CProcess() {
        super();
    }

    public CProcess(String v) {
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
