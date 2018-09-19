package com.daoyintech.daoyin_release.configs.qiniu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "qiniu")
@Data
public class QiniuProperties {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String domain;

}

