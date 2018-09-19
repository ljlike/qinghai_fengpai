package com.daoyintech.daoyin_release.utils.qiniu;

import com.daoyintech.daoyin_release.configs.bargain.WaterMarkImageProperty;
import com.daoyintech.daoyin_release.configs.bargain.WaterMarkTextProperty;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Slf4j
@Component
public class ImageScalaTool {

    @Value("${user.bg}")
    private String bg;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Autowired
    private QiniuUploadTool qiniuUploadTool;

    public static final Logger logger = LoggerFactory.getLogger(ImageScalaTool.class);

    private static final Integer Y = -50;

    public String generateUserPic(User user) throws QiniuException {
        /*String nickname = null;
        try {
            nickname = URLDecoder.decode(user.getNickName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
        }*/
        String string = user.getNickName() + ",邀请你";
        String str = "长按图片可保存至手机相册";
        return new StringBuilder(bg).append("?watermark/").append("3")
                .append(generateUserAvatr(user)).append(generateString(string)).append(generateUserQrCode(user)).append(generateStr(str)).toString();
    }

    public String generateUserQrCode(User user){

        WaterMarkImageProperty imageProperty = new WaterMarkImageProperty();
        String imageUrl = new StringBuilder(user.getAppletQrCode()).toString();
        imageProperty.setImageUrl(imageUrl);
        imageProperty.setDissolve(100);
        imageProperty.setDx(150);
        imageProperty.setDy(100);
        imageProperty.setGravity("SouthWest");
        return BargainImageScalaTool.generateImageWaterMark(imageProperty);

    }
    public String generateUserAvatr(User user) throws QiniuException {

        qiniuUploadTool.uploadAvatar(user);

        WaterMarkImageProperty waterMarkImageProperty = new WaterMarkImageProperty();
        String imageUrl = new  StringBuilder(user.getAvatarQiniu()).append("?roundPic/radius/!50p").toString();
        waterMarkImageProperty.setImageUrl(imageUrl);
        waterMarkImageProperty.setDissolve(100);
        waterMarkImageProperty.setDx(5);
        waterMarkImageProperty.setDy(300);
        waterMarkImageProperty.setGravity("North");
        return BargainImageScalaTool.generateImageWaterMark(waterMarkImageProperty);
    }

    public String generateString(String string){
        WaterMarkTextProperty waterMarkTextProperty = new WaterMarkTextProperty();
        waterMarkTextProperty.setText(string);
        waterMarkTextProperty.setColor("#971E2D");
        waterMarkTextProperty.setDissolve(100);
        waterMarkTextProperty.setFont(WaterMarkTextProperty.microsoft);
        waterMarkTextProperty.setFontSize(30);
        waterMarkTextProperty.setDx(0);
        waterMarkTextProperty.setDy(Y);
        waterMarkTextProperty.setGravity("Center");
        try {
            return BargainImageScalaTool.generateWordWaterMark(waterMarkTextProperty);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateStr(String str){
        WaterMarkTextProperty waterMarkTextProperty = new WaterMarkTextProperty();
        waterMarkTextProperty.setText(str);
        waterMarkTextProperty.setColor("black");
        waterMarkTextProperty.setDissolve(100);
        waterMarkTextProperty.setFont(WaterMarkTextProperty.microsoft);
        waterMarkTextProperty.setFontSize(20);
        waterMarkTextProperty.setDx(0);
        waterMarkTextProperty.setDy(480);
        waterMarkTextProperty.setGravity("Center");
        try {
            return BargainImageScalaTool.generateWordWaterMark(waterMarkTextProperty);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}