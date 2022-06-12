package com.dfd.plugin.generator.code.controller.actions;

import com.dfd.plugin.generator.code.ui.CanvasHelper;
import com.dfd.plugin.generator.code.ui.blocks.BlockType;
import com.dfd.plugin.generator.code.ui.blocks.BlockUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class ExternalStorageBlockCreatorAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        BlockUI block = new BlockUI();
        block.init(BlockType.EXTERNAL_STORAGE);
        CanvasHelper.getInstance().add(block);
        CanvasHelper.getInstance().addBlock(block);
        block.setBounds(15, 15, block.getWidth(), block.getHeight());
        CanvasHelper.getInstance().revalidate();
        CanvasHelper.getInstance().repaint();
    }
}
