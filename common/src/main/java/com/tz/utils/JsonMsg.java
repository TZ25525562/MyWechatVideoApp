package com.tz.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回json信息
 */
public class JsonMsg {

    //状态码
    private Integer status;

    //提示信息
    private String message;

    //返回的附加具体信息
    private Map<String,Object> map = new HashMap<>();

    /**
     * 处理成功返回的信息
     * @return
     */
    public static JsonMsg success(){
        JsonMsg msg = new JsonMsg();
        msg.setStatus(200);
        msg.setMessage("处理成功");
        return msg;
    }

    /**
     * 处理失败返回的信息
     * @return
     */
    public static JsonMsg fail(){
        JsonMsg msg = new JsonMsg();
        msg.setStatus(100);
        msg.setMessage("处理失败");
        return msg;
    }

    /**
     * 添加附加信息的方法
     * @param name
     * @param information
     * @return
     */
    public JsonMsg add(String name , Object information){
        this.map.put(name,information);
        return this;
    }

    public JsonMsg() {
    }

    public JsonMsg(Integer status, String message, Map<String, Object> map) {
        this.status = status;
        this.message = message;
        this.map = map;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }


}
