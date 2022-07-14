package com.hz.platform.master.core.protocol.response.whitelist;

/**
 * @author yoko
 * @desc 众邦-白名单回调
 * @create 2022-07-13 18:00
 **/
public class ResponseWhitelist {
    public ResponseWhitelist(){

    }

    public String user_serial_no;

    public Integer order_status;// 1初始化，2失败，3成功

    public String res_code;

    public String res_explain;


    public String getUser_serial_no() {
        return user_serial_no;
    }

    public void setUser_serial_no(String user_serial_no) {
        this.user_serial_no = user_serial_no;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public String getRes_code() {
        return res_code;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public String getRes_explain() {
        return res_explain;
    }

    public void setRes_explain(String res_explain) {
        this.res_explain = res_explain;
    }
}
