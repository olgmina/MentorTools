package com.dfd.plugin.generator.code.controller;

import com.dfd.plugin.generator.code.ui.DFDWindowContentPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;


public class DFDPluginToolWindowFactory implements ToolWindowFactory {
    private DFDWindowContentPanel panel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        panel = new DFDWindowContentPanel();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel, "", true);
        toolWindow.getContentManager().addContent(content);
    }
}

