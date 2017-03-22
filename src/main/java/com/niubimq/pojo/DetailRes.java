package com.niubimq.pojo;

/**
 * 消息处理返回结果集
 * Created by junjin4838 on 17/3/15.
 * <p>
 * 注意1：
 * POJO类中的任何布尔类型的变量，都不要加is，否则部分框架解析会引起序列化错误
 * （isSuccess 比如FastJson在解析的时候会以为对应的属性名称是success，导致属性获取不到，进而抛出异常）
 */
public class DetailRes {

    Boolean success;

    String errMsg;

    public DetailRes(Boolean success, String errMsg) {
        this.success = success;
        this.errMsg = errMsg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "DetailRes{" + "success=" + success + ", errMsg='" + errMsg + '\'' + '}';
    }
}
