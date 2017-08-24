package com.seckill.dto;
//封装json结果
public class SeckillResult<T> {
    private boolean success;
    private  T data;
    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SeckillResult(boolean success, T data, String error) {

        this.success = success;
        this.data = data;
        this.error = error;
    }
}
