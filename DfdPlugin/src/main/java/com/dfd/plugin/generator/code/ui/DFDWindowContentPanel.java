package com.dfd.plugin.generator.code.ui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;


public class DFDWindowContentPanel extends SimpleToolWindowPanel {
    private final ActionManager actionManager;
    private final ActionToolbar actionToolbar;

    public DFDWindowContentPanel() {
        super(true, true);


        actionManager = ActionManager.getInstance();
        actionToolbar = actionManager.createActionToolbar(
                "toolbar",
                (ActionGroup) actionManager.getAction("DFDPlugin.UI.Toolbar"),
                true);
        setToolbar(actionToolbar.getComponent());

        JScrollPane scrollPane = new JBScrollPane(CanvasHelper.getInstance());
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(50);
        setContent(scrollPane);

        revalidate();
    }

}
