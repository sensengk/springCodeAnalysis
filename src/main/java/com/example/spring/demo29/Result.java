package com.example.spring.demo29;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result  {
	private int code;
	private String msg;
	private Object data;

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @JsonCreator
    public Result(@JsonProperty("code") int code,@JsonProperty("data") Object data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public static Result ok(){
        return new Result(200,"ok");
    }
    public static Result ok(Object data){
        return new Result(200,data);
    }
    public static Result error(String msg){
        return new Result(500, "服务内部的错误" + msg);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
