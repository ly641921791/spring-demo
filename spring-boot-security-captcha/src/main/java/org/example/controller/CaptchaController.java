package org.example.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.example.vo.CaptchaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private DefaultKaptcha producer;

    @ResponseBody
    @GetMapping("/get")
    public CaptchaVO getCaptcha() throws IOException {
        // 生成文字验证码
        String content = producer.createText();
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = producer.createImage(content);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        // 对字节数组Base64编码
        String str = "data:image/jpeg;base64,";
        String base64Img = str + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setBase64Img(base64Img);
        return captchaVO;
    }

}