package com.fiap.fastfood.order.common.exceptions.custom;

import com.fiap.fastfood.order.common.exceptions.model.CustomException;

public class BusinessException extends CustomException {
    public BusinessException(String code, String message) {
        super(code, message);
    }
}