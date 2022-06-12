package com.dfd.plugin.generator.code.services.generator.code;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import java.util.ArrayList;

public class InterfaceGenerator extends GeneralGenerator {

    public InterfaceGenerator(String modifycator, String name, String implementation, String extendion, ArrayList<Method> methods) {
        super(modifycator, name, implementation, extendion, methods);
    }

    public InterfaceGenerator(String modifycator, String name, String implementation, String extendion) {
        super(modifycator, name, implementation, extendion);
    }

    public InterfaceGenerator() {
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
        out.append("interface ");
        out.append(name + " ");
        if(extendion.length() != 0){
            out.append(extendion + " ");
        }
        if(implementation.length() != 0){
            out.append(implementation + " ");
        }
        out.append(" {\n");
        for(Method m: methods){
            out.append(m.getCodeInterface());
        }
        out.append("\n}\n");

        try {
            return new Formatter().formatSource(out.toString());
        } catch (FormatterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Method> getMethods(){
        ArrayList<Method> m = new ArrayList<>();
        for(Method _m: methods){
            Method __m = new Method(_m.getModifycator(), _m.getType(), _m.getName(), _m.getOverrides());
            for(MethodArg md: _m.getMethodArgs()){
                __m.addArg((MethodArg)md.clone());
            }
            m.add(__m);
        }
        return m;
    }

}
