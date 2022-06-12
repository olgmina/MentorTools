package com.dfd.plugin.generator.code.model.blocks;

public class Line {

    private int id; // id cell
    private int source; // id cell source
    private int target; // id cell target

    public Line(){
        id = -1;
        source = -1;
        target = -1;
    }

    public Line(String _id, String s, String t){
        id = Integer.parseInt(_id);
        source = Integer.parseInt(s);
        target = Integer.parseInt(t);
    }

    public Line(int _id, int s, int t){
        id = _id;
        source = s;
        target = t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public String toString(){
        return "Line: id source - " + source + ", id target - " + target;
    }
}
