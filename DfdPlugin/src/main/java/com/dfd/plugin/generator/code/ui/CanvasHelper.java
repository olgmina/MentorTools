package com.dfd.plugin.generator.code.ui;

public class CanvasHelper {

    private static Canvas instance = null;

    public static Canvas getInstance(){
        if(instance == null) {
            instance = new Canvas();
        }
        return instance;
    }



}
