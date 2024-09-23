package com.jesper.seckill.exception;

import com.jesper.seckill.result.CodeMsg;
import lombok.Getter;

/**
 * 自定义全局异常类
 */
@Getter
public class GlobalException extends RuntimeException {

    private static final long servialVersionUID = 1L;

    private final CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

}
