package com.daoyintech.daoyin_release.configs.qiniu;


import com.daoyintech.daoyin_release.utils.qiniu.QiniuUploadTool;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(QiniuProperties.class)
@org.springframework.context.annotation.Configuration
public class QiniuConfiguration {

    @Autowired
    private QiniuProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public UploadManager uploadManager(Configuration configuration){
        UploadManager uploadManager = new UploadManager(configuration);
        return uploadManager;
    }

    @Bean
    public Configuration configuration(){
        return new Configuration(Zone.zone2());
    }

    @Bean
    @ConditionalOnMissingBean
    public Auth auth(){
        Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
        return auth;
    }

    @Bean
    @ConditionalOnMissingBean
    public BucketManager bucketManager(Auth auth, Configuration configuration){
        BucketManager bucketManager = new BucketManager(auth,configuration);
        return bucketManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public QiniuUploadTool qiniuUpload(Auth auth, UploadManager uploadManager, BucketManager bucketManager){
        QiniuUploadTool upload = new QiniuUploadTool();
        upload.setAuth(auth);
        upload.setBucket(properties.getBucket());
        upload.setUploadManager(uploadManager);
        upload.setBucketManager(bucketManager);
        return upload;
    }


}
