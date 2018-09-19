package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.setting.OverallSetting;

/**
 * @author pei on 2018/08/22
 */
public interface OverallSettingService {

    OverallSetting findByName(String name);


}
