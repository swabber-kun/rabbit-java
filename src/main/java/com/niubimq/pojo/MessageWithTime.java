package com.niubimq.pojo;

/**
 * 消息类
 * Created by junjin4838 on 17/3/17.
 */
public class MessageWithTime {

    private  Long time;

    private  Object message;

    public MessageWithTime(Long time,Object message){
        this.time = time;
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
