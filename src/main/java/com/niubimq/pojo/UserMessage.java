package com.niubimq.pojo;

/**
 * Created by junjin4838 on 17/3/15.
 *
 * 注意1：
 *    POJO类必须写toString方法，如果继承了另外一个POJO类，主要要在前面加一个super.toString()
 *    在方法抛出异常的时候，可以直接调用toString()方法打印属性值，便于排查问题
 *
 * 注意2：
 *    所有的POJO类属性必须使用包装数据类型
 */
public class UserMessage {

    Integer id;

    String name;

    public UserMessage(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserMessage{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
