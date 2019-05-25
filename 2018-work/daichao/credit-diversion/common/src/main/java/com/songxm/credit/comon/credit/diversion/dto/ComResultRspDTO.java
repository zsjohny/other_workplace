package com.songxm.credit.comon.credit.diversion.dto;

import lombok.Data;

/**
 * @author: sxm
 * @date: 2017/11/3 16:09
 * @version: v1.0.0
 */
@Data
public class ComResultRspDTO<T> {
    Integer status;
    String msg;
    T data;

    public ComResultRspDTO(){}

    public ComResultRspDTO(Integer status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public ComResultRspDTO(Integer status, String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
