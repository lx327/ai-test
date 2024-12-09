package com.xuange.ai.aaa.common;
//xuange
public class FromMessage {
    public long getKeyid() {
        return keyid;
    }

    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }

    private long keyid;
    private String from;
    private String msg;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FromMessage(long id, String from, String msg) {
        this.keyid=id;
        this.from = from;
        this.msg = msg;
    }
}
