package com.dfd.plugin.generator.code.model.collections;

public class SpecialList {
    private ListElem head; // начало списка

    public SpecialList(ListElem head) {
        this.head = head;
    }

    public SpecialList() {
        this.head = new ListElem();
    }

    public ListElem getHead() {
        return head;
    }

    public void setHead(ListElem head) {
        this.head = head;
    }

}
