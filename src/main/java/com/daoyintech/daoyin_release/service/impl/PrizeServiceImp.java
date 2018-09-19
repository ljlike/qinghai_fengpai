package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.draw.Prize;
import com.daoyintech.daoyin_release.repository.prize.PrizeRepository;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.service.PrizeService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class PrizeServiceImp implements PrizeService {

    @Autowired
    private PrizeRepository prizeRepository;

    @Override
    public ResultResponse findJackpotIntegral() {
        List<Prize> prizeList = prizeRepository.findAll();
        int integral = 0;
        for (Prize prize : prizeList) {
            int residueIntegral = prize.getTotalPrizePoint() - prize.getTodayPrizePoint();
            integral += residueIntegral;
        }
        return ResultResponseUtil.success(integral);
    }





}




