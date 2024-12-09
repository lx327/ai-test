package com.xuange.ai.aaa.common;
//xuange
public class FromMessageTo {
    private String from;
    private String msg;
    private String to;

    public FromMessageTo(String from, String msg, String to) {
        this.from = from;
        this.msg = msg;
        this.to = to;
    }

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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "FromMessageTo{" +
                "from='" + from + '\'' +
                ", msg='" + msg + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
    public static FromMessageTo fromString(String str) {
        String[] parts = str.split(", ");
        String from = parts[0].split("=")[1].replace("'", "");
        String msg = parts[1].split("=")[1].replace("'", "");
        String to = parts[2].split("=")[1].replace("'", "").replace("}", "");
        return new FromMessageTo(from, msg, to);
    }
}
