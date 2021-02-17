package com.cqi.hr.vo;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.annotations.SerializedName;;

public class ResponseVo extends Observable{
	@SerializedName("ecode")
    private Integer ecode;
	
	@SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private Object data;

    public ResponseVo(Integer ecode, String msg, Object data) {
		this.ecode = ecode;
		this.msg = msg;
		this.data = data;
	}
    
    public ResponseVo() {
		// TODO Auto-generated constructor stub
	}

	public Integer getEcode() {
        return ecode;
    }

    public void setEcode(Integer ecode) {
        this.ecode = ecode;
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
}
