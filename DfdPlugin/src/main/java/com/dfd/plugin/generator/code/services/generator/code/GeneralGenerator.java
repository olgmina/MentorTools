package com.dfd.plugin.generator.code.services.generator.code;

import java.util.ArrayList;

public abstract class GeneralGenerator {

    protected StringBuilder modifycator;
    protected StringBuilder name;
    protected StringBuilder implementation;
    protected StringBuilder extendion;
    protected ArrayList<Method> methods;
    protected ArrayList<StringBuilder> imports;
    protected StringBuilder packages;

    public GeneralGenerator(String modifycator, String name, String implementation, String extendion, ArrayList<Method> methods) {
        this.modifycator = new StringBuilder(modifycator);
        this.name = new StringBuilder(name);
        this.implementation = new StringBuilder(implementation);
        this.extendion = new StringBuilder(extendion);
        this.methods = new ArrayList<>(methods);
        imports = new ArrayList<>();
        packages = new StringBuilder();
    }

    public GeneralGenerator(String modifycator, String name, String implementation, String extendion, Method method) {
        this.modifycator = new StringBuilder(modifycator);
        this.name = new StringBuilder(name);
        this.implementation = new StringBuilder(implementation);
        this.extendion = new StringBuilder(extendion);
        this.methods = new ArrayList<>();
        this.methods.add(method);
        imports = new ArrayList<>();
        packages = new StringBuilder();
    }

    public GeneralGenerator(String modifycator, String name, String implementation, String extendion) {
        this.modifycator = new StringBuilder(modifycator);
        this.name = new StringBuilder(name);
        this.implementation = new StringBuilder(implementation);
        this.extendion = new StringBuilder(extendion);
        this.methods = new ArrayList<>();
        imports = new ArrayList<>();
        packages = new StringBuilder();
    }

    public GeneralGenerator() {
        modifycator = new StringBuilder();
        name = new StringBuilder();
        implementation = new StringBuilder();
        extendion = new StringBuilder();
        methods = new ArrayList<>();
        imports = new ArrayList<>();
        packages = new StringBuilder();
    }

    public abstract String getCode();

    public void addMethod(Method m) {
        methods.add(m);
    }

    public void addCodeMethod(String name, String code) {
        for(Method met: methods){
            if(met.getName().equals(name)){
                met.addCode(code);
                return;
            }
        }
    }

    public String getName(){
        return name.toString();
    }

    public void addImport(String imp){
        imports.add(new StringBuilder(imp));
    }

    public void setPackage(String packages) {
        this.packages = new StringBuilder(packages);
    }
}
