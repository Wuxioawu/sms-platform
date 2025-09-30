package com.peng.sms.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.peng.sms.constant.WebMasterConstants;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class KaptchaController {
    private final String JPG = "jpg";

    @Autowired
    private DefaultKaptcha kaptcha;

    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse resp) {

        resp.setHeader("Cache-Control", "no-store,no-cahe");
        resp.setContentType("image/jpg");

        String text = kaptcha.createText();
        SecurityUtils.getSubject().getSession().setAttribute(WebMasterConstants.KAPTCHA, text);
        BufferedImage image = kaptcha.createImage(text);

        try {
            ServletOutputStream outputStream = resp.getOutputStream();
            ImageIO.write(image, JPG, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
