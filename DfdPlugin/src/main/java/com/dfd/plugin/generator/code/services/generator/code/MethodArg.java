package com.dfd.plugin.generator.code.services.generator.code;

public class MethodArg implements Comparable<MethodArg> {

    private StringBuilder type;
    private StringBuilder name;

    public MethodArg(String type, String name) {
        this.type = new StringBuilder(type);
        this.name = new StringBuilder(name);
    }

    public String getCode(){
        return type + " " + name;
    }

    @Override
    public Object clone(){
        return new MethodArg(type.toString(), name.toString());
    }

    @Override
    public int compareTo(MethodArg o) {
        if (o == null)
            throw new NullPointerException();
        if(type.toString().compareTo(o.type.toString()) == 0){
            if(name.toString().compareTo(o.name.toString()) == 0){
                return 0;
            }else{
                return name.toString().compareTo(o.name.toString());
            }
        }else{
            return type.toString().compareTo(o.type.toString());
        }
    }

}
