package com.example.demo.controller;

import com.example.demo.service.CloudService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * 主业务逻辑控制
 * @author debonet
 */
@RestController
public class CloudController {

    @Autowired
    CloudService cloudService;

    @GetMapping("graphx/computeNode/{id}")
    public ResponseVO computNode(@PathVariable int id){
        return cloudService.computNode(id);
    }

    @GetMapping("stream/get")
    public ResponseVO getStream(){
        return cloudService.getStream();
    }

    @GetMapping("stream/get/{idx}")
    public ResponseVO getStreamByIndex(@PathVariable int idx){
        return cloudService.getStreamByIndex(idx);
    }

    @GetMapping("stream/getStartIndex")
    public ResponseVO getStartIndex(){
        return cloudService.getStartIndex();
    }

    @GetMapping("stream/getLastIndex")
    public ResponseVO getLastIndex(){
        return cloudService.getLastIndex();
    }

    @GetMapping("stream/getCity/{idx}")
    public ResponseVO getCityByIndex(@PathVariable int idx){
        return cloudService.getCityByIndex(idx);
    }

    @GetMapping("stream/getCityStartIndex")
    public ResponseVO getCityStartIndex(){
        return cloudService.getCityStartIndex();
    }

    @GetMapping("stream/getCityLastIndex")
    public ResponseVO getCityLastIndex(){
        return cloudService.getCityLastIndex();
    }
}
