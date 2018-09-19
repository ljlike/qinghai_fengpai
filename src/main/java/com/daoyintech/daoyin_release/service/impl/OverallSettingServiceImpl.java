package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.setting.OverallSetting;
import com.daoyintech.daoyin_release.enums.setting.OverallSettingStatus;
import com.daoyintech.daoyin_release.repository.setting.OverallSettingRepository;
import com.daoyintech.daoyin_release.service.OverallSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pei on 2018/08/22
 */
@Service
public class OverallSettingServiceImpl implements OverallSettingService {

    @Autowired
    private OverallSettingRepository overallSettingRepository;

    @Override
    public OverallSetting findByName(String name) {
        return overallSettingRepository.findByName(name);
    }
}
