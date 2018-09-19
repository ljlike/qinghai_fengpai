package com.daoyintech.daoyin_release.repository.setting;

import com.daoyintech.daoyin_release.entity.setting.OverallSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverallSettingRepository extends JpaRepository<OverallSetting,Long> {

    OverallSetting findByName(String name);

}
