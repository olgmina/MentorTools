package com.dfd.plugin.generator.code.controller.actions;

import com.dfd.plugin.generator.code.model.DFDModel;
import com.dfd.plugin.generator.code.services.CreateFiles;
import com.dfd.plugin.generator.code.services.GeneratorCode;
import com.dfd.plugin.generator.code.services.generator.code.GeneralGenerator;
import com.dfd.plugin.generator.code.services.generator.code.TypeFolders;
import com.dfd.plugin.generator.code.ui.CanvasHelper;
import com.dfd.plugin.generator.code.ui.blocks.BlockType;
import com.google.common.collect.Multimap;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.progress.impl.CoreProgressManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GenerationCodeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        // формируем DFD модель
        DFDModel dfdModel = new DFDModel();
        var connectors = CanvasHelper.getInstance().getConnectors();
        for(var conn: connectors){
            if(conn.getFirst().getBlockType() == BlockType.INPUT){
                dfdModel.setInputText(conn.getFirst().getTextBlock());
                continue;
            }
            if(conn.getSecond().getBlockType() == BlockType.OUTPUT){
                dfdModel.setOutputText(conn.getSecond().getTextBlock());
                continue;
            }
            if(conn.getFirst().getBlockType() == BlockType.PROCESS){
                dfdModel.getProcesses().put(conn.getFirst().getCurrentID(), conn.getFirst().getTextBlock());
                var _connects = dfdModel.getExternalStoragesForProcess();
                var elem = _connects.keySet().stream().filter(ev -> ev == conn.getFirst().getCurrentID()).findFirst();
                if(elem.isEmpty()){
                    dfdModel.getExternalStoragesForProcess().put(conn.getFirst().getCurrentID(), new ArrayList<>());
                }
                dfdModel.getExternalStoragesForProcess().get(conn.getFirst().getCurrentID()).add(conn.getSecond().getCurrentID());
            }
            if(conn.getSecond().getBlockType()== BlockType.EXTERNAL_STORAGE){
                dfdModel.getExternalStorages().put(conn.getSecond().getCurrentID(), conn.getSecond().getTextBlock());
            }
        }
        // показать msg box с пакетом кода
        String s = (String) JOptionPane.showInputDialog(
                null,
                "Please, set package for code:\n",
                "Package tool",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                dfdModel.getPackageText());
        if ((s != null) && (s.length() > 0)) {
            dfdModel.setPackageText(s);
        }
        // если ок - то генериуем код
        AtomicReference<Multimap<TypeFolders, GeneralGenerator>> code = new AtomicReference<>();
        CoreProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
            CoreProgressManager.getInstance().getProgressIndicator().setText("Please, wait...");
            code.set(new GeneratorCode(dfdModel).startGenerateCode());
        }, "Generating code", false /* canBeCanceled */, e.getProject());
        CommandProcessor.getInstance().executeCommand(e.getProject(), () -> {
            ApplicationManager.getApplication().runWriteAction(() -> {
                new CreateFiles().generateSourceCode(e.getProject(), code.get(), dfdModel.getPackageText());
            });
        }, "Null1", "Null2"); // что за параметры...хз, гуглить лень, работает без них отлично
    }
}
