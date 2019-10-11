package com.example.demo.vo;

import java.util.List;

public class StreamVO {

    private List<StreamChartVO> charts;

    public StreamVO(List<StreamChartVO> charts) {
        this.charts = charts;
    }

    public List<StreamChartVO> getCharts() {
        return charts;
    }

    public void setCharts(List<StreamChartVO> charts) {
        this.charts = charts;
    }
}
