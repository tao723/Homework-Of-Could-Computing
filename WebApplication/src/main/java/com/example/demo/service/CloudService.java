package com.example.demo.service;

import com.example.demo.data.CloudMapper;
import com.example.demo.po.StreamItem;
import com.example.demo.vo.ResponseVO;
import com.example.demo.vo.StreamChartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 前端获取逻辑控制
 * @author debonet
 */
@Service
public class CloudService {

    @Autowired
    CloudMapper cloudMapper;

    //单次展示最大的条目数
    private static final int PRE_MAX = 9;

    /**
     * 获取数据库中所有的StreamChart一次性返回
     * @return
     */
    public ResponseVO getStream(){
        ArrayList<StreamChartVO> charts = new ArrayList<>();
        int maxIndex = cloudMapper.getMaxInsertIndex();
        int minIndex = cloudMapper.getMinInsertIndex();
        for(int idx=minIndex;idx<=maxIndex;idx++){
            StreamChartVO chart = getStreamChartByIndex(idx);
            charts.add(chart);
        }
        return ResponseVO.buildSuccess(charts);
    }

    /**
     * 获取插入idx为index的StreamChart
     * @param index
     * @return
     */
    public ResponseVO getStreamByIndex(int index){
        return ResponseVO.buildSuccess(getStreamChartByIndex(index));
    }

    /**
     * 获取数据库中最初的Chart的index，如果没有则返回-1
     * @return
     */
    public ResponseVO getStartIndex(){
        Integer startIndex = cloudMapper.getMinInsertIndex();
        if(startIndex==null)return ResponseVO.buildSuccess(-1);
        return ResponseVO.buildSuccess(startIndex);
    }

    public ResponseVO getLastIndex(){
        Integer lastIndex = cloudMapper.getMaxInsertIndex();
        if(lastIndex==null)return ResponseVO.buildSuccess(-1);
        return ResponseVO.buildSuccess(lastIndex);
    }

    /**
     * 根据idx返回StreamChartVO
     * @param idx
     * @return
     */
    private StreamChartVO getStreamChartByIndex(int idx){
        ArrayList<String> xData = new ArrayList<>();
        ArrayList<Integer> yData = new ArrayList<>();
        for(StreamItem item:cloudMapper.getStreamItemsByNum(idx)){
            if(item == null)continue;
            xData.add(item.getKeyword());
            yData.add(item.getCount());
        }
        for(int i=0;i<yData.size()-1;i++){
            for (int j=0; j<yData.size()-1-i; j++) {
                if (yData.get(j) < yData.get(j+1)) {
                    Integer ytemp = yData.get(j);
                    String xtemp = xData.get(j);
                    yData.set(j,yData.get(j+1));
                    xData.set(j,xData.get(j+1));
                    yData.set(j+1,ytemp);
                    xData.set(j+1,xtemp);
                }
            }
        }
        StreamChartVO chart;
        if(yData.size()>PRE_MAX){
            chart = new StreamChartVO(idx,xData.subList(0,PRE_MAX),yData.subList(0,PRE_MAX));
        }
        else {
            chart = new StreamChartVO(idx,xData,yData);
        }
        return chart;
    }
}
