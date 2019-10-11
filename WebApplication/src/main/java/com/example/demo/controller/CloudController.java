package com.example.demo.controller;

import com.example.demo.service.CloudService;
import com.example.demo.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 主业务逻辑控制
 * @author debonet
 */
@RestController
public class CloudController {

    @Autowired
    CloudService cloudService;

    @GetMapping("stream/get")
    public ResponseVO getStream(){
        return cloudService.getStream();
    }
}
