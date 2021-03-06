package com.example.demo.vo;

import java.util.List;

public class CityChartVO {
    private int num;
    private List<String> xData;
    private List<Integer> yData;

    public CityChartVO(int num, List<String> xData, List<Integer> yData) {
        this.num = num;
        this.xData = xData;
        this.yData = yData;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<String> getxData() {
        return xData;
    }

    public void setxData(List<String> xData) {
        this.xData = xData;
    }

    public List<Integer> getyData() {
        return yData;
    }

    public void setyData(List<Integer> yData) {
        this.yData = yData;
    }
}
