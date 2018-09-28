package com.daoyintech.daoyin_release.service.impl.order.bargain;

import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.repository.order.BargainJoinerRepository;
import com.daoyintech.daoyin_release.service.order.bargain.BargainJoinerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pei on 2018/08/17
 */
@Slf4j
@Service
public class BargainJoinerServiceImpl implements BargainJoinerService {

    @Autowired
    private BargainJoinerRepository bargainJoinerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BargainJoinerRepository joinerRepository;

    @Override
    public List<BargainJoiner> getJoinersByBargainOrder(BargainOrder bargainOrder) {
        List<BargainJoiner> bargainJoiners = bargainJoinerRepository.findByBargainOrderId(bargainOrder.getId());
        if (bargainJoiners.size() != 0) {
            updateBargainJoinerInfo(bargainJoiners);
        }
        return bargainJoiners;
    }

    @Override
    public Boolean isUserJoin(BargainOrder bargainOrder, User user) {
        return joinerRepository.existsByJoinerIdAndBargainOrderId(user.getId(), bargainOrder.getId());
    }

    public void updateBargainJoinerInfo(List<BargainJoiner> joiners) {
        joiners.forEach(joiner -> {
            User user = userRepository.getOne(joiner.getJoinerId());
            joiner.setAvatar(user.getAvatar());
            /*String nickname = null;
            try {
                nickname = URLDecoder.decode(user.getNickName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
            }*/
            joiner.setNickName(user.getNickName());
            if (joiner.getType().equals(BargainHelpType.心理券) || joiner.getType().equals(BargainHelpType.法务券)){
                joiner.setType(BargainHelpType.线下活动卷);
            }
        });
    }
}
