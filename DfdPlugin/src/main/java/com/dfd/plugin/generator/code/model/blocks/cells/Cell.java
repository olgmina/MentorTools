package com.dfd.plugin.generator.code.model.blocks.cells;

import java.util.regex.Pattern;

public abstract class Cell { // абстрактный класс фигуры

    private String value; // text cell

    public Cell(){
    }

    public Cell(String v){
        setData( v);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public abstract void setData(String v);


    protected String replaceValue(String v){ // метод конструирования правильного имени класса(в стиле Java)
        v = v.replace('-', '_');
        String words[] = v.split("[\\u00A0\\s]+");
        StringBuilder out = new StringBuilder(words[0]);
        out.setCharAt(0, Character.toUpperCase(out.charAt(0)));
        for(int i = 1;i<words.length;i++){
            StringBuilder tmp = new StringBuilder(words[i].toLowerCase());
            tmp.setCharAt(0, Character.toUpperCase(tmp.charAt(0)));
            out.append(tmp);
        }
        return out.toString();
    }

}

// TODO: внутри наследников статическая переменная, хранящая имя и счётчик, использовать, если имя не задано пользоваталем
