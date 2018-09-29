package com.daoyintech.daoyin_release.service.impl.admin;

import com.daoyintech.daoyin_release.entity.card.WxCard;
import com.daoyintech.daoyin_release.repository.card.CardRepository;
import com.daoyintech.daoyin_release.service.admin.AdminWxCardService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author pei on 2018/09/28
 */
@Service
public class AdminWxCardServiceImpl implements AdminWxCardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public String addWxCard(String title, String date, Integer type) {
        String msg = null;
        WxCard wxCard = cardRepository.findByTypeAndStatus(type, 0);
        if (!StringUtils.isEmpty(wxCard)){
            msg = "此类型的卡券已经存在,请先下架类型为"+type+"的卡券!";
            return msg;
        }
        wxCard = new WxCard();
        wxCard.setTitle(title.replaceAll(" ",""));
        wxCard.setType(type);
        wxCard.setStatus(0);
        wxCard.setExpire(DateUtils.dateStringShortTypeToLong(date));
        cardRepository.save(wxCard);
        return msg;
    }

    @Override
    public List<WxCard> wxCardList() {
        return cardRepository.findAll();
    }

    @Override
    public List<WxCard> upOrDownCard(Long id) {
        WxCard wxCard = cardRepository.findById(id).orElse(null);
        if (wxCard.getStatus() == 0){
            wxCard.setStatus(1);
        }else{
            wxCard.setStatus(0);
        }
        cardRepository.save(wxCard);
        return cardRepository.findAll();
    }


}




