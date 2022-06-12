package com.dfd.plugin.generator.code.model.blocks.cells;

public class CTempDatabase extends Cell {

    private static String name = "tempDatabase";
    private static int number = 0;

    public CTempDatabase() {
        super();
    }

    public CTempDatabase(String v) {
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
