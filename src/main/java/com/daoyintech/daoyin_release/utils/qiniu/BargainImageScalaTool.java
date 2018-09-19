package com.daoyintech.daoyin_release.utils.qiniu;


import com.daoyintech.daoyin_release.configs.bargain.WaterMarkImageProperty;
import com.daoyintech.daoyin_release.configs.bargain.WaterMarkTextProperty;
import com.daoyintech.daoyin_release.configs.qiniu.QiniuProperties;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.response.DefinitionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Base64;

@Component
public class BargainImageScalaTool {

    @Value("${bargain.bg}")
    private String bg;

    @Value("${bargain.zhiwen}")
    private String zhiwen;

    private static final String BG_SCALA = "?imageView2/1/w/750/h/1300/format/png/q/100";
    private static final Integer Y_STEP = 40;
    private static final Integer Y = 20;


    @Autowired
    private QiniuProperties qiniuProperties;

    public String generateBargainOrderPic(BargainOrder bargainOrder, Product product, ProductFormat format) {
        return new StringBuilder(bg).append(BG_SCALA).append("|").append("watermark/").append("3")
                .append(generateProductImageWaterMark(product))
                .append(generateProductNameWaterMark(product))
                .append(generateProductPriceWaterMark(format))
                .append(generateText())
                .append(generateProductQrCode(bargainOrder))
                .append(generateZhiWen(zhiwen))
                .toString();
    }

    private String generateZhiWen(String zhiwen) {
        WaterMarkImageProperty waterMarkImageProperty = new WaterMarkImageProperty();
        String imageUrl = new StringBuilder(zhiwen).append("?imageView2/0/w/180/h/180").toString();
        waterMarkImageProperty.setImageUrl(imageUrl);
        waterMarkImageProperty.setDissolve(100);
        waterMarkImageProperty.setDx(150);
        waterMarkImageProperty.setDy(10);
        waterMarkImageProperty.setGravity("SouthEast");
        return generateImageWaterMark(waterMarkImageProperty);
    }

    private String generateText() {
        WaterMarkTextProperty waterMarkTextProperty = new WaterMarkTextProperty();
        waterMarkTextProperty.setText("长按指纹，识别二维码");
        waterMarkTextProperty.setColor("#ffffff");
        waterMarkTextProperty.setDissolve(100);
        waterMarkTextProperty.setFont(WaterMarkTextProperty.microsoft);
        waterMarkTextProperty.setFontSize(30);
        waterMarkTextProperty.setDx(10);
        waterMarkTextProperty.setDy(210);
        waterMarkTextProperty.setGravity("South");
        try {
            return generateWordWaterMark(waterMarkTextProperty);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateProductImageWaterMark(Product product) {
        WaterMarkImageProperty waterMarkImageProperty = new WaterMarkImageProperty();
        String imageUrl = new StringBuilder(DefinitionResponse.getImgUrl(product.getIconKey())).append("?imageView2/0/w/450/h/450").toString();
        waterMarkImageProperty.setImageUrl(imageUrl);
        waterMarkImageProperty.setDissolve(100);
        waterMarkImageProperty.setDx(5);
        waterMarkImageProperty.setDy(80);
        waterMarkImageProperty.setGravity("North");
        return generateImageWaterMark(waterMarkImageProperty);
    }


    public String generateProductNameWaterMark(Product product) {
        WaterMarkTextProperty waterMarkTextProperty = new WaterMarkTextProperty();
        waterMarkTextProperty.setText(product.getName());
        waterMarkTextProperty.setColor("#000000");
        waterMarkTextProperty.setDissolve(100);
        waterMarkTextProperty.setFont(WaterMarkTextProperty.microsoft);
        waterMarkTextProperty.setFontSize(40);
        waterMarkTextProperty.setDx(0);
        waterMarkTextProperty.setDy(Y);
        waterMarkTextProperty.setGravity("Center");
        try {
            return generateWordWaterMark(waterMarkTextProperty);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateProductSubTitleWaterMark(ProductFormat format) {
        WaterMarkTextProperty waterMarkTextProperty = new WaterMarkTextProperty();
        waterMarkTextProperty.setText("(" + format.getName() + ")");
        waterMarkTextProperty.setColor("#B1B1B1");
        waterMarkTextProperty.setDissolve(100);
        waterMarkTextProperty.setFont(WaterMarkTextProperty.microsoft);
        waterMarkTextProperty.setFontSize(30);
        waterMarkTextProperty.setDx(10);
        waterMarkTextProperty.setDy(Y + Y_STEP + 10);
        waterMarkTextProperty.setGravity("Center");
        try {
            return generateWordWaterMark(waterMarkTextProperty);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateProductPriceWaterMark(ProductFormat productFormat) {
        WaterMarkTextProperty waterMarkTextProperty = new WaterMarkTextProperty();
        waterMarkTextProperty.setText("原价: ￥" + productFormat.getSellPrice().setScale(1, BigDecimal.ROUND_HALF_UP));
        waterMarkTextProperty.setColor("#979797");
        waterMarkTextProperty.setDissolve(100);
        waterMarkTextProperty.setFont(WaterMarkTextProperty.microsoft);
        waterMarkTextProperty.setFontSize(40);
        waterMarkTextProperty.setDx(10);
        waterMarkTextProperty.setDy(Y + Y_STEP * 2 + 30);
        waterMarkTextProperty.setGravity("Center");
        try {
            return generateWordWaterMark(waterMarkTextProperty);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //背景图，头像，XX邀请你，二维码

    public static String generateImageWaterMark(WaterMarkImageProperty waterMarkImageProperty) {
        StringBuilder sb = new StringBuilder();
        String imageBase64 = null;
        try {
            imageBase64 = Base64.getUrlEncoder().encodeToString(waterMarkImageProperty.getImageUrl().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append("/image/");
        sb.append(imageBase64);
        sb.append("/dissolve/");
        sb.append(waterMarkImageProperty.getDissolve());
        sb.append("/gravity/");
        sb.append(waterMarkImageProperty.getGravity());
        sb.append("/dx/");
        sb.append(waterMarkImageProperty.getDx());
        sb.append("/dy/");
        sb.append(waterMarkImageProperty.getDy());
        return sb.toString();
    }

    public static String generateWordWaterMark(WaterMarkTextProperty textProperty) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("/text/");
        sb.append(Base64.getUrlEncoder().encodeToString(textProperty.getText().getBytes("UTF-8")));
        sb.append("/font/");
        sb.append(Base64.getUrlEncoder().encodeToString(textProperty.getFont().getBytes("UTF-8")));
        sb.append("/fontsize/");
        sb.append(textProperty.getFontSize() * 20);
        sb.append("/fill/");
        sb.append(Base64.getUrlEncoder().encodeToString(textProperty.getColor().getBytes("UTF-8")));
        sb.append("/dissolve/");
        sb.append(textProperty.getDissolve());
        sb.append("/gravity/");
        sb.append(textProperty.getGravity());
        sb.append("/dx/");
        sb.append(textProperty.getDx());
        sb.append("/dy/");
        sb.append(textProperty.getDy());
        return sb.toString();
    }

    public String generateProductQrCode(BargainOrder bargainOrder) {
        WaterMarkImageProperty imageProperty = new WaterMarkImageProperty();
        String imageUrl = new StringBuilder(qiniuProperties.getDomain()).append("/").append(bargainOrder.getQrCodeUrl()).append("?imageView2/0/w/180/h/180").toString();
        imageProperty.setImageUrl(imageUrl);
        imageProperty.setDissolve(100);
        imageProperty.setDx(150);
        imageProperty.setDy(10);
        imageProperty.setGravity("SouthWest");
        return generateImageWaterMark(imageProperty);
    }

    public String generateWaterMarkWithCurrentBargainPrice(BargainOrder bargainOrder, ProductFormat productFormat) {
        WaterMarkTextProperty waterMarkTextProperty = new WaterMarkTextProperty();
        BigDecimal cutPrice = productFormat.getSellPrice().subtract(bargainOrder.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP);
        waterMarkTextProperty.setText("成交价: ￥" + bargainOrder.getPrice().setScale(0, BigDecimal.ROUND_HALF_UP) + "(帮助了" + cutPrice + "元)");
        waterMarkTextProperty.setColor("#FD7D7D");
        waterMarkTextProperty.setDissolve(100);
        waterMarkTextProperty.setFont(WaterMarkTextProperty.microsoft);
        waterMarkTextProperty.setFontSize(40);
        waterMarkTextProperty.setDx(10);
        waterMarkTextProperty.setDy(Y + Y_STEP * 4 + 30);
        waterMarkTextProperty.setGravity("Center");
        try {
            return generateWordWaterMark(waterMarkTextProperty);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
