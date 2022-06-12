package com.dfd.plugin.generator.code.model.blocks.parsed;

public class CellConstantsCreator {

    public static CellConstants getStyleConstant(String style){
        if(style.indexOf("input;") != -1){
            return CellConstants.CELL_INPUT;
        }else if(style.indexOf("output;") != -1){
            return CellConstants.CELL_OUTPUT;
        }else if(style.indexOf("shape=ellipse;") != -1){
            return CellConstants.CELL_PROCESS;
        }else if(style.indexOf("shape=saveDataBox;") != -1){
            return CellConstants.CELL_TMP_DATABASE;
        }
        return null;
    }
}
