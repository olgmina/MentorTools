package com.dfd.plugin.generator.code.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class DFDModel {

    private String inputText;
    private String outputText;
    private String packageText;
    private HashMap<Long, String> processes; // all process, key - index, value - text
    private HashMap<Long, String> externalStorages; // all external storages, key - index, value - text
    private HashMap<Long, ArrayList<Long>> externalStoragesForProcess; // key - key process, value - array keys external storages

    public DFDModel(){
        packageText = "dfd.framework.generated.classes";
        processes = new HashMap<>();
        externalStorages = new HashMap<>();
        externalStoragesForProcess = new HashMap<>();
    }

    public String getInputText() {
        if(inputText == null){
            return "InputDefaultClass";
        }
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getOutputText() {
        if(outputText == null){
            return "OutputDefaultClass";
        }
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public HashMap<Long, String> getProcesses() {
        return processes;
    }

    public void setProcesses(HashMap<Long, String> processes) {
        this.processes = processes;
    }

    public HashMap<Long, String> getExternalStorages() {
        return externalStorages;
    }

    public void setExternalStorages(HashMap<Long, String> externalStorages) {
        this.externalStorages = externalStorages;
    }

    public HashMap<Long, ArrayList<Long>> getExternalStoragesForProcess() {
        return externalStoragesForProcess;
    }

    public void setExternalStoragesForProcess(HashMap<Long, ArrayList<Long>> externalStoragesForProcess) {
        this.externalStoragesForProcess = externalStoragesForProcess;
    }

    public String getPackageText() {
        return packageText;
    }

    public void setPackageText(String packageText) {
        this.packageText = packageText;
    }
}
