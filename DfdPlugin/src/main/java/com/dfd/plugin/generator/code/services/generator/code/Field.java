package com.dfd.plugin.generator.code.services.generator.code;

public class Field extends Member {

    public Field(String modifycator, String type, String name) {
        super(modifycator, type, name);
    }

    public Field(boolean st, boolean fn, String modifycator, String type, String name) {
        super(modifycator, st, fn, type, name);
    }

    public Field(boolean st, boolean fn, String modifycator, String type, String name, String val) {
        super(modifycator, st, fn, type, name, val);
    }

    public Field() {
    }

    @Override
    public String getCode(){
        StringBuilder out = new StringBuilder();
        out.append(modifycator + " ");
        if(isStatic()){
            out.append("static ");
        }
        if(isFinal()){
            out.append("final ");
        }
        out.append(type + " " + name);
        if(getValue() != null){
            out.append(" = " + getValue().toString() + ";");
        }else{
            out.append(";");
        }
        return out.toString();
    }

    @Override
    public Object clone(){
        Field m = new Field(_static, _final, modifycator.toString(), type.toString(), name.toString(), value.toString());
        return m;
    }


}
