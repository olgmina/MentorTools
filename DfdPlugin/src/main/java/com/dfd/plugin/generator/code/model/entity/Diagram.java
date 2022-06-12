package com.dfd.plugin.generator.code.model.entity;

public class Diagram {
    private String xmlData;
    private String javaCode;
    private String language;
    private String systemBuilds;

    public Diagram() {
        xmlData = "";
        javaCode = "";
        language = "";
        systemBuilds = "";
    }

    public Diagram(String x) {
        xmlData = x;
        javaCode = "";
        language = "";
        systemBuilds = "";
    }

    public Diagram(String x, String jc) {
        xmlData = x;
        javaCode = jc;
        language = "";
        systemBuilds = "";
    }

    public Diagram(String x, String jc, String l, String sb) {
        xmlData = x;
        javaCode = jc;
        language = l;
        systemBuilds = sb;
    }

    public String getXmlData() {
        return xmlData;
    }

    public void setXmlData(String data) {
        xmlData = data;
    }

    public String getJavaCode() {
        return javaCode;
    }

    public void setJavaCode(String data) {
        javaCode = data;
    }

    public String getLanguage() {
        return language;
    }

    public String getSystemBuilds() {
        return systemBuilds;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSystemBuilds(String systemBuilds) {
        this.systemBuilds = systemBuilds;
    }
}
