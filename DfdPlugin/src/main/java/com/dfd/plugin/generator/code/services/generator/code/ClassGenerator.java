package com.dfd.plugin.generator.code.services.generator.code;


import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import java.util.ArrayList;

public class ClassGenerator extends GeneralGenerator {

    private ArrayList<Field> fields;
    private Boolean abstr;

    public ClassGenerator(String modifycator, String name, String implementation, String extendion, ArrayList<Method> methods, ArrayList<Field> fields) {
        super(modifycator, name, implementation, extendion, methods);
        this.fields = new ArrayList<>(fields);
        abstr = false;
    }

    public ClassGenerator(String modifycator, String name, String implementation, String extendion, ArrayList<Method> methods, Field field) {
        super(modifycator, name, implementation, extendion, methods);
        this.fields = new ArrayList<Field>();
        this.fields.add(field);
        abstr = false;
    }

    public ClassGenerator(String modifycator, String name, String implementation, String extendion, Method method, ArrayList<Field> fields) {
        super(modifycator, name, implementation, extendion, method);
        this.fields = new ArrayList<>(fields);
        abstr = false;
    }

    public ClassGenerator(String modifycator, String name, String implementation, String extendion, ArrayList<Field> fields) {
        super(modifycator, name, implementation, extendion);
        this.fields = new ArrayList<>(fields);
        abstr = false;
    }

    public ClassGenerator(String modifycator, String name, String implementation, String extendion, Method method, Field field) {
        super(modifycator, name, implementation, extendion, method);
        this.fields = new ArrayList<Field>();
        this.fields.add(field);
        abstr = false;
    }

    public ClassGenerator(String modifycator, String name, String implementation, String extendion, Method method) {
        super(modifycator, name, implementation, extendion, method);
        this.fields = new ArrayList<Field>();
        abstr = false;
    }

    public ClassGenerator(String modifycator, String name, String implementation, String extendion, Field field) {
        super(modifycator, name, implementation, extendion);
        this.fields = new ArrayList<Field>();
        this.fields.add(field);
        abstr = false;
    }

    public ClassGenerator(String modifycator, String name, String implementation, String extendion) {
        super(modifycator, name, implementation, extendion);
        this.fields = new ArrayList<>();
        abstr = false;
    }

    public ClassGenerator(ArrayList<Field> fields) {
        this.fields = new ArrayList<>(fields);
        abstr = false;
    }

    public ClassGenerator() {
        fields = new ArrayList<>();
        abstr = false;
    }

    public void setAbstract(Boolean abstr){
        this.abstr = abstr;
    }

    public void addField(Field f){
        fields.add(f);
    }

    @Override
    public String getCode() {
        StringBuilder out = new StringBuilder();
        if(!packages.toString().isEmpty()){
            out.append("package " + packages + ";\n\n");
        }
        for(var imp: imports){
            out.append("import " + imp + ";\n");
        }
        out.append("\n");
        out.append(modifycator + " ");
        if(abstr){
            out.append("abstract ");
        }
        out.append("class ");
        out.append(name + " ");
        if(extendion.length() != 0){
            out.append("extends " + extendion + " ");
        }
        if(implementation.length() != 0){
            out.append("implements " + implementation + " ");
        }
        out.append(" {\n");
        for(Field f: fields){
            out.append(f.getCode());
        }
        for(Method m: methods){
            out.append(m.getCode());
        }
        out.append("\n}\n");
        try {
            return new Formatter().formatSource(out.toString());
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        return null;
    }


}
