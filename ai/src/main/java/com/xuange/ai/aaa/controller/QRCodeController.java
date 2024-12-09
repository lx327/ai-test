package com.xuange.ai.aaa.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.xuange.ai.aaa.common.WxConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//xuange
@RestController
@CrossOrigin
@Component
@RequestMapping("try")
public class QRCodeController {
    @Autowired
    private WxConfigProperties wxConfigProperties;

    @PostMapping("/saoMa")
    public  void generateQrCode() throws IOException {

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = null;
        try {
            String url=String.format("https://login.weixin.qq.com/l/qrconnect?appid=%s&redirect_uri=%s&response_type=%s&scope=%s&state=STATE"
                    ,wxConfigProperties.getAppId(),"http://localhost:9099/login/callback", wxConfigProperties.getSecret());
            RestTemplate restTemplate = new RestTemplate();
           String content = restTemplate.getForObject(url, String.class);
            System.out.println(content);
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        BufferedImage bufferedImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        System.out.println(bufferedImage);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y)? 0xFF000000 : 0xFFFFFFFF);
            }
        }

         String filePath = "/home/xuange/IdeaProjects\n";
        ImageIO.write(bufferedImage, "png", new File(filePath));
    }

//    public static void main(String[] args) {
//        try {
//            generateQrCode();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}

//+---------------------+
//        |  1. 用户访问登录页   |
//        +---------------------+
//        |
//v
//+---------------------+
//        |  2. 生成二维码URL   |
//        |  https://open.weixin.|
//        |  qq.com/connect/qr   |
//        +---------------------+
//        |
//v
//+---------------------+
//        |  3. 显示二维码       |
//        +---------------------+
//        |
//v
//+---------------------+
//        |  4. 用户扫描二维码   |
//        +---------------------+
//        |
//v
//+---------------------+
//        |  5. 用户同意授权     |
//        +---------------------+
//        |
//v
//+---------------------+
//        |  6. 微信重定向回调  |
//        |  URL，附带code参数   |
//        +---------------------+
//        |
//v
//+---------------------+
//        |  7. 使用code请求     |
//        |  Access Token       |
//        +---------------------+
//        |
//v
//+---------------------+
//        |  8. 获取Access Token |
//        +---------------------+
//        |
//v
//+---------------------+
//        |  9. 使用Access Token |
//        |  请求用户信息       |
//        +---------------------+
//        |
//v
//+---------------------+
//        | 10. 获取用户信息     |
//        +---------------------+
//        |
//v
//+---------------------+
//        | 11. 登录成功，进入   |
//        | 用户首页或其他页面   |
//        +---------------------+
