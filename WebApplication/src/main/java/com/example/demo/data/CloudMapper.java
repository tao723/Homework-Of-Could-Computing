package com.example.demo.data;


import com.example.demo.po.StreamItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CloudMapper {

    Integer testGet();

    List<StreamItem> getStreamItemsByNum(@Param("num")int num);

    Integer getMaxInsertIndex();

    Integer getMinInsertIndex();

}
