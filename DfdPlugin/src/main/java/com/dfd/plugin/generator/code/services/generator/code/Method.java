package com.dfd.plugin.generator.code.services.generator.code;

import java.util.ArrayList;

public class Method extends Member {

    private StringBuilder code;
    private Boolean overrides;
    private ArrayList<MethodArg> methodArgs;
    private StringBuilder throwes;

    public Method(String modifycator, String type, String name, String code, ArrayList<MethodArg> args, Boolean overrides) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(overrides);
        this.methodArgs = new ArrayList<>(args);
        this.throwes = new StringBuilder();
    }

    public Method(String modifycator, String type, String name, String code, MethodArg arg, Boolean overrides) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(overrides);
        this.methodArgs = new ArrayList<>();
        this.methodArgs.add(arg);
        this.throwes = new StringBuilder();
    }

    public Method(String modifycator, String type, String name, String code, ArrayList<MethodArg> args, String t) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(false);
        this.methodArgs = new ArrayList<>(args);
        this.throwes = new StringBuilder(t);
    }

    public Method(String modifycator, String type, String name, String code, MethodArg arg, String t) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(false);
        this.methodArgs = new ArrayList<>();
        this.methodArgs.add(arg);
        this.throwes = new StringBuilder(t);
    }

    public Method(String modifycator, String type, String name, String code, ArrayList<MethodArg> args) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(false);
        this.methodArgs = new ArrayList<>(args);
        this.throwes = new StringBuilder();
    }

    public Method(String modifycator, String type, String name, String code, MethodArg arg) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(false);
        this.methodArgs = new ArrayList<>();
        this.methodArgs.add(arg);
        this.throwes = new StringBuilder();
    }

    public Method(String modifycator, String type, String name, String code, Boolean override) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(override);
        this.methodArgs = new ArrayList<>();
        this.throwes = new StringBuilder();
    }

    public Method(String modifycator, String type, String name, String code, String th, Boolean override) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(override);
        this.methodArgs = new ArrayList<>();
        this.throwes = new StringBuilder(th);
    }

    public Method(String modifycator, String type, String name, Boolean override) {
        super(modifycator, type, name);
        this.code = new StringBuilder();
        this.overrides = new Boolean(override);
        this.methodArgs = new ArrayList<>();
        this.throwes = new StringBuilder();
    }

    public Method(String modifycator, String type, String name, String code) {
        super(modifycator, type, name);
        this.code = new StringBuilder(code);
        this.overrides = new Boolean(false);
        this.methodArgs = new ArrayList<>();
        this.throwes = new StringBuilder();
    }

    public Method(String type, String name) {
        super(type, name);
        this.code = new StringBuilder();
        this.overrides = new Boolean(false);
        this.methodArgs = new ArrayList<>();
        this.throwes = new StringBuilder();
    }

    public Method(String type, String name, String t) {
        super(type, name);
        this.code = new StringBuilder();
        this.overrides = new Boolean(false);
        this.methodArgs = new ArrayList<>();
        this.throwes = new StringBuilder(t);
    }

    public Method(String type, String name, MethodArg arg) {
        super(type, name);
        this.code = new StringBuilder();
        this.overrides = new Boolean(false);
        this.methodArgs = new ArrayList<>();
        this.methodArgs.add(arg);
        this.throwes = new StringBuilder();
    }

    public Method() {
    }

    @Override
    public String getCode() {
        StringBuilder out = new StringBuilder();
        if(overrides) {
            out.append("@Override\n");
        }
        out.append(modifycator + " ");
        out.append(type + " ");
        out.append(name + " (");
        for(int i = 0; i < methodArgs.size(); i++){
            out.append(methodArgs.get(i).getCode());
            if (i != methodArgs.size() - 1) {
                out.append(", ");
            }
        }
        out.append(") ");
        if(throwes.length() != 0) {
            out.append("throws " + throwes + " ");
        }
        out.append("{\n" + code + "\n}\n");
        return out.toString();
    }

    public String getCodeInterface() {
        StringBuilder out = new StringBuilder();
        out.append(type + " ");
        out.append(name + " (");
        for(int i = 0; i < methodArgs.size(); i++){
            out.append(methodArgs.get(i).getCode());
            if (i != methodArgs.size() - 1) {
                out.append(", ");
            }
        }
        out.append(") ");
        if(throwes.length() != 0) {
            out.append("throws " + throwes + " ");
        }
        out.append(";\n");
        return out.toString();
    }

    @Override
    public Object clone(){
        Method m = new Method(modifycator.toString(), type.toString(), name.toString(), code.toString(), throwes.toString(), overrides);
        for(MethodArg _m: methodArgs){
            m.methodArgs.add((MethodArg)_m.clone());
        }
        return m;
    }

    public Object cloneOverride(){
        Method m = (Method)this.clone();
        m.overrides = true;
        m.modifycator = new StringBuilder("public");
        m.methodArgs = new ArrayList<>();
        for(MethodArg _m: methodArgs){
            m.methodArgs.add((MethodArg)_m.clone());
        }
        return m;
    }

    public void addArg(MethodArg arg){
        methodArgs.add(arg);
    }

    public void setMethodCode(String c){
        code = new StringBuilder(c);
    }

    public String getMethodCode(){
        return code.toString();
    }

    public Boolean getOverrides() {
        return overrides;
    }

    public void addCode(String code){
        this.code.append(code);
    }

    public ArrayList<MethodArg> getMethodArgs() {
        return methodArgs;
    }
}
