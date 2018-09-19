package com.daoyintech.daoyin_release.response;

import com.daoyintech.daoyin_release.entity.banner.Banner;
import com.google.common.collect.Lists;
import lombok.Data;
import java.util.List;

@Data
public class BannerResponse {

    private String link;

    private String url;

    public static List<BannerResponse> bannerResponseBuild(List<Banner> banners) {
        return Lists.transform(banners,input -> {
            BannerResponse response = new BannerResponse();
            //String link = input.getLink();
            //response.setProductId(Long.valueOf(link.substring(link.lastIndexOf("/")+1)));
            response.setLink(input.getLink());
            response.setUrl(DefinitionResponse.getImgUrl(input.getIconKey()));
            return response;
        });
    }

}
