package com.backend.seoul00.domain.collection.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ResponseCounter<T>  {
    private int count;
    private T data;

    public ResponseCounter(T data){
        this.data = data;
        if(data instanceof List){
            this.count = ((List<?>)data).size();
        } else{
            this.count = 1;
        }
    }

}
