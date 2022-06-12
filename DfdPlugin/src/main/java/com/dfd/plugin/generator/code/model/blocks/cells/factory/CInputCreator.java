package com.dfd.plugin.generator.code.model.blocks.cells.factory;


import com.dfd.plugin.generator.code.model.blocks.cells.*;

public class CInputCreator extends CCreator{
    @Override
    public Cell create() {
        return new CInput();
    }
}
