package com.dfd.plugin.generator.code.services.generator.code;

public abstract class Member {

    protected StringBuilder modifycator;
    protected boolean _static;
    protected boolean _final;
    protected StringBuilder type;
    protected StringBuilder name;
    protected StringBuilder value;

    public Member(String modifycator, String type, String name) {
        this.modifycator = new StringBuilder(modifycator);
        this.type = new StringBuilder(type);
        this.name = new StringBuilder(name);
    }

    public Member(String type, String name) {
        this.modifycator = new StringBuilder();
        this.type = new StringBuilder(type);
        this.name = new StringBuilder(name);
    }

    public Member() {
    }

    public Member(String modifycator, boolean _static, boolean _final, String type, String name) {
        this._static = _static;
        this._final = _final;
        this.modifycator = new StringBuilder(modifycator);
        this.type = new StringBuilder(type);
        this.name = new StringBuilder(name);
    }

    public Member(String modifycator, boolean _static, boolean _final, String type, String name, String val) {
        this._static = _static;
        this._final = _final;
        this.modifycator = new StringBuilder(modifycator);
        this.type = new StringBuilder(type);
        this.name = new StringBuilder(name);
        this.value = new StringBuilder(val);
    }

    public abstract String getCode();




    public String getModifycator() {
        return modifycator.toString();
    }

    public String getType() {
        return type.toString();
    }

    public String getName() {
        return name.toString();
    }

    public void setType(String type){
        this.type = new StringBuilder(type);
    }

    public boolean isStatic() {
        return _static;
    }

    public boolean isFinal() {
        return _final;
    }

    public StringBuilder getValue() {
        return value;
    }
}
