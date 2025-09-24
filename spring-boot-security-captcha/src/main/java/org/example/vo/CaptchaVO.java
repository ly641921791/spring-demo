package org.example.vo;

import lombok.Data;

@Data
public class CaptchaVO {

    /**
     * 验证码标识符
     */
    private String captchaKey;
    /**
     * 验证码过期时间
     */
    private Long expire;
    /**
     * base64字符串
     */
    private String base64Img;

}
