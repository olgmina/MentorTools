package com.dfd.plugin.generator.code.services;

import com.dfd.plugin.generator.code.services.generator.code.GeneralGenerator;
import com.dfd.plugin.generator.code.services.generator.code.TypeFolders;
import com.google.common.collect.Multimap;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CreateFiles {

    private boolean complete;

    public CreateFiles(){
        complete = false;
    }
    
    public void generateSourceCode(Project project, Multimap<TypeFolders, GeneralGenerator> code, String rootPackageStr) {

        try {
            ModuleManager manager = ModuleManager.getInstance(project);
            Module currentModule = Arrays.stream(manager.getModules()).findFirst().get();
            VirtualFile srcFolder = ModuleRootManager.getInstance(currentModule).getSourceRoots()[0];
            VirtualFile rootPackage = createPackage(srcFolder, rootPackageStr, project);
            HashMap<TypeFolders, String> folders  = new HashMap<>(){{
                put(TypeFolders.MODELS, "models");
                put(TypeFolders.SERVICES, "services");
                put(TypeFolders.EXCEPTIONS, "exceptions");
            }};
            for(var f: code.asMap().entrySet()){
                VirtualFile newPackage = createPackage(rootPackage, folders.get(f.getKey()), project);
                for(var cl: f.getValue()){
                    VirtualFile currentClass = newPackage.createChildData(project, cl.getName() + ".java");
                    currentClass.setBinaryContent(cl.getCode().getBytes(StandardCharsets.UTF_8));
                    // write code
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        complete = true;

        // несколько модулей в проекте
//        try {
//            ModuleManager manager = ModuleManager.getInstance(project);
//            Module[] modules = manager.getModules();
//            for (Module module : modules) {
//                ModuleRootManager root = ModuleRootManager.getInstance(module);
//                for (VirtualFile srcFolder : root.getSourceRoots()) {
//                    VirtualFile testPackage = srcFolder.createChildDirectory(project, "test.cock.sucker");
//                    VirtualFile testClass = testPackage.createChildData(project, "TestClass.java");
//                }
//            }
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }

    }

    private VirtualFile createPackage(VirtualFile srcFolder, String pack, Project project) throws IOException {
        VirtualFile currentPackage = srcFolder;
        String [] folders = pack.split(Pattern.quote("."));
        for(String f: folders) {
            currentPackage = currentPackage.createChildDirectory(project, f);
        }
        return currentPackage;
    }

    public boolean isComplete(){
        return complete;
    }

}
