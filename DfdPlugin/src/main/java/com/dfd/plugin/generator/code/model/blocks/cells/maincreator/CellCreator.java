package com.dfd.plugin.generator.code.model.blocks.cells.maincreator;


import com.dfd.plugin.generator.code.model.blocks.cells.Cell;
import com.dfd.plugin.generator.code.model.blocks.cells.factory.*;
import com.dfd.plugin.generator.code.model.blocks.parsed.*;

import java.util.HashMap;

import static com.dfd.plugin.generator.code.model.blocks.parsed.CellConstants.*;

public class CellCreator { // фабричный метод

    private static HashMap<CellConstants, CCreator> factoryData = initFactoryData();

    private static HashMap<CellConstants, CCreator> initFactoryData(){
        return new HashMap<CellConstants, CCreator>() {{
            put(CELL_INPUT, new CInputCreator());
            put(CELL_OUTPUT, new COutputCreator());
            put(CELL_PROCESS, new CProcessCreator());
            put(CELL_TMP_DATABASE, new CTempDatabaseCreator());
        }};
    }

    public static Cell CreateBlock(CellConstants style){
        return factoryData.get(style).create();
    }

}
