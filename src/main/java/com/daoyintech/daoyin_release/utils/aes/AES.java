package com.daoyintech.daoyin_release.utils.aes;

import com.daoyintech.daoyin_release.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * @author Administrator
 */
@Slf4j
public class AES {
    public static boolean initialized = false;

    /**
     * AES解密
     * @param content 密文
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchProviderException
     */
    static {
        initialize();
    }
    public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws Exception {
            //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //Cipher cipher = Cipher.getInstance("aes/CBC/PKCS7Padding");
            //Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));
            // 初始化
            byte[] result = cipher.doFinal(content);
        if (result == null){
            log.error("{}小程序通过解密算法报错", DateUtils.getStringDate());
        }
        return result;
    }

    public static void initialize(){
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
    //生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception{
        AlgorithmParameters params = AlgorithmParameters.getInstance("aes");
        params.init(new IvParameterSpec(iv));
        return params;
    }






}