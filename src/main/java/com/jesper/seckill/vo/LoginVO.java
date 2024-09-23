package com.jesper.seckill.vo;

import com.jesper.seckill.validator.IsMobile;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class LoginVO {

    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    private String password;

    @Override
    public String toString() {
        return "LoginVO{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
