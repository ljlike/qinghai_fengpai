package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.enums.ProductTypeStatus;
import com.daoyintech.daoyin_release.response.WelcomeResponse;

public interface WelcomeService {

    WelcomeResponse findWelcomeResponseBy(ProductTypeStatus status, Boolean isSell, Long userId);

}
