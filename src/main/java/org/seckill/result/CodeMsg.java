package org.seckill.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeMsg {

    private int code;
    private String message;

    public static CodeMsg SUCCESS = new CodeMsg(0, "success");

    /*通用错误码5001XX*/
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500104, "访问高峰期，请稍等！");

    /*登录模块5002XX*/
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg PRIMARY_ERROR = new CodeMsg(500216, "主键冲突");

    /*商品模块5003XX*/

    /*订单模块5004XX*/
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400, "订单不存在");

    /*秒杀模块5005XX*/
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
    public static CodeMsg REPEAT_SECKILL = new CodeMsg(500501, "不能重复秒杀");

    private CodeMsg() {
    }

    private CodeMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.message, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", message=" + message + "]";
    }
    
}
