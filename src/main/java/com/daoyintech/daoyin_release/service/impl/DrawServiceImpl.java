package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.draw.Prize;
import com.daoyintech.daoyin_release.repository.prize.PrizeRepository;
import com.daoyintech.daoyin_release.service.DrawService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author pei on 2018/09/10
 */
@Service
public class DrawServiceImpl implements DrawService {


    @Autowired
    private PrizeRepository prizeRepository;

    /**
     * 基本获取最低限额
     */
    @Value("${draw.basicMin}")
    private Integer basicMinPrize;
    /**
     * 基本获取最高限额
     */
    @Value("${draw.basicMax}")
    private Integer basicMaxPrize;

    @Override
    public synchronized Integer drawJackpotIntegral() {
        Prize prize = randomIntegralRule();
        Integer integral = randomUserIntegral(prize);
        if (integral == 0){
            integral = integral+1;
        }
        //规则数据更新
        prize.setTodayPrizePersonsCount(prize.getTodayPrizePersonsCount()+1);
        prize.setTodayPrizePoint(prize.getTodayPrizePoint()+integral);
        prizeRepository.save(prize);
        return integral;
    }

    /**
     * 随机奖池规则,从哪抽奖
     * */
    public Prize randomIntegralRule(){
        List<Map.Entry<Integer, Prize>> prizeArrayList = new ArrayList<>();
        List<Prize> prizes = prizeRepository.findAllByOrderByMaxPrizePersonsCountDesc();
        int max = 0;
        for(int i = 0; i < prizes.size(); i++) {
            Prize prize = prizes.get(i);
            if (prize.getMaxPrizePersonsCount() - prize.getTodayPrizePersonsCount() <= 0) {
                continue;
            }
            max +=  prize.getMaxPrizePersonsCount() - prize.getTodayPrizePersonsCount();
            prizeArrayList.add(Maps.immutableEntry(max, prize));
        }
        if (prizeArrayList.size() == 0) {
            //所有名额已抽完
            return prizes.get(0);
        }
        //随机区间积分
        Integer ruleIntegral = new Random().nextInt(max)+1;
        Optional<Map.Entry<Integer, Prize>> result = prizeArrayList.stream()
                .filter(item -> ruleIntegral < item.getKey()).findFirst();
        return result.isPresent() ? result.get().getValue() : prizes.get(0);
    }

    /**
     * 随机为用户抽取积分
     * */
    public Integer randomUserIntegral(Prize prize){
        int userPoint = new Random().nextInt(prize.getMax() - prize.getMin() + 1) + prize.getMin();
        return prize.getTodayPrizePoint() + userPoint > prize.getTotalPrizePoint() ? drawBasicPrize(prize) : userPoint;
    }

    /**
     * 最基本抽取的积分
     * */
    private Integer drawBasicPrize(Prize prize){
        int integral = prize.getTotalPrizePoint() - prize.getTodayPrizePoint();
        return integral  >= basicMaxPrize ? integral : new Random().nextInt(basicMaxPrize - basicMinPrize + 1) + basicMinPrize;
    }

}









