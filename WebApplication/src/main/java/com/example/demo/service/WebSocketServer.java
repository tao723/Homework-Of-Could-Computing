package com.example.demo.service;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * WebSocket协议接受和处理
 * @author debonet
 */

@ServerEndpoint("/websocket")
public class WebSocketServer {

    static Log log=LogFactory.get(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //concurrent包的线程安全set，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashSet<WebSocketServer> wssSet = new ConcurrentHashSet<>();

    /**
     * 建立连接成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        wssSet.add(this);
        addOnlineCount();
        log.info("1个客户端成功连接，当前连接数为"+getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        wssSet.remove(this);
        subOnlineCount();
        log.info("1个客户端断开连接，当前连接数为"+getOnlineCount());
    }

    /**
     * 连接发生错误调用的方法
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session,Error error){
        log.error("websocket连接发生未知错误！");
        error.printStackTrace();
    }

    /**
     * 向单个会话发送信息
     * @param msg
     * @throws IOException
     */
    public void sendMsg(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    public static void sendInfo(String msg){
        System.out.println("send info");
        for(WebSocketServer wss:wssSet){
            try {
                wss.sendMsg(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
