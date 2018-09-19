package com.daoyintech.daoyin_release.utils.qiniu;

import com.daoyintech.daoyin_release.configs.qiniu.QiniuProperties;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


@Data
@Slf4j
public class QiniuUploadTool {
    private Long expireTime = System.currentTimeMillis() / 1000;
    private String bucket;
    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private String upToken;
    private Auth auth;

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private UserRepository userRepository;

    public DefaultPutRet upload(byte[] bytes, String key) throws IOException {
        if(System.currentTimeMillis() / 1000 >= expireTime) {
            generateUpToken();
        }
        Response response = uploadManager.put(bytes, key, upToken);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet;
    }

    public DefaultPutRet delete(String key) throws QiniuException {
        Response response = bucketManager.delete(bucket,key);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet;
    }

    private void generateUpToken(){
        this.expireTime = System.currentTimeMillis() / 1000 + 3600;
        this.upToken = auth.uploadToken(bucket);
    }

    public void uploadAvatar(User user) {
        try {
            FetchRet fetchRet = bucketManager.fetch(user.getAvatar(), qiniuProperties.getBucket(), "users/" + user.getId() + "/avatar.jpeg");
            log.info(fetchRet.hash);
            log.info(fetchRet.key);
            log.info(fetchRet.mimeType);
            log.info(fetchRet.fsize + "");
            user.setAvatarQiniu( DefinitionResponse.getImgUrl(fetchRet.key));
            userRepository.save(user);
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }

}
