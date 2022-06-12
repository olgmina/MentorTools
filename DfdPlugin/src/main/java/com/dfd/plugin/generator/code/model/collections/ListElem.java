package com.dfd.plugin.generator.code.model.collections;


import com.dfd.plugin.generator.code.model.blocks.cells.Cell;

import java.util.ArrayList;

public class ListElem {
    private Cell data; // данные, которые содержит узел
    private ListElem next; // следующий
    private ArrayList<Cell> tmpDataBlocks; // множество ссылок на доп данные

    public ListElem(Cell data, ListElem next) {
        this.data = data;
        this.next = next;
        this.tmpDataBlocks = new ArrayList<>();
    }

    public ListElem() {
        this.next = null;
        this.data = null;
        this.tmpDataBlocks = new ArrayList<>();
    }

    public ListElem getNext() {
        return next;
    }

    public void setNext(ListElem next) {
        this.next = next;
    }

    public Cell getData() {
        return data;
    }

    public void setData(Cell data) {
        this.data = data;
    }

    public void addTmpDataBlock(Cell data){
        tmpDataBlocks.add(data);
    }

    public ArrayList<Cell> getTmpDataBlocks() {
        return tmpDataBlocks;
    }

    public void setTmpDataBlocks(ArrayList<Cell> tmpDataBlocks) {
        this.tmpDataBlocks = tmpDataBlocks;
    }

    @Override
    public String toString(){
        String text = "Elem: " + data.getValue() + "\n";
        for(int i = 0, j = 0; i < tmpDataBlocks.size();i++){
            if(i == 0){
                text += "Tmp database:\n";
            }
            text += (j+1) + ") " + tmpDataBlocks.get(i).getValue() + ";\n";
        }
        return text;
    }
}
