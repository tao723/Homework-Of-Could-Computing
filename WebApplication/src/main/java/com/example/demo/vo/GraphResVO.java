package com.example.demo.vo;

public class GraphResVO {
    private int id1;
    private int id2;
    private String name1;
    private String name2;

    public GraphResVO() {
    }

    public GraphResVO(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public GraphResVO(int id1, int id2, String name1, String name2) {
        this.id1 = id1;
        this.id2 = id2;
        this.name1 = name1;
        this.name2 = name2;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

}
