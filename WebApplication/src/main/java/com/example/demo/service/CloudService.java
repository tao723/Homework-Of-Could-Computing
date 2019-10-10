package com.example.demo.service;

import com.example.demo.vo.ResponseVO;
import org.springframework.stereotype.Service;

/**
 * 前端获取逻辑控制
 * @author debonet
 */
@Service
public class CloudService {

    public ResponseVO getStream(){
        System.out.println("reach cloudservice");
        WebSocketServer.sendInfo("haha");
        return ResponseVO.buildSuccess("test");
    }
}
