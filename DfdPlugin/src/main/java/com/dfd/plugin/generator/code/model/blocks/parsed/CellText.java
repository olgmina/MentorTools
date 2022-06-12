package com.dfd.plugin.generator.code.model.blocks.parsed;

public class CellText {
    private String value;
    private CellConstants type;

    public CellText(String value, CellConstants type) {
        this.value = value;
        this.type = type;
    }

    public CellText() {
        this.value = "";
        this.type = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CellConstants getType() {
        return type;
    }

    public void setType(CellConstants type) {
        this.type = type;
    }
}
