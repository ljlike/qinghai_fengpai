package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.order.Cart;
import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.mappers.LineItemMapper;
import com.daoyintech.daoyin_release.repository.order.CartRepository;
import com.daoyintech.daoyin_release.repository.order.LineItemRepository;
import com.daoyintech.daoyin_release.response.result.LineItemResult;
import com.daoyintech.daoyin_release.service.CartService;
import com.daoyintech.daoyin_release.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private LineItemMapper lineItemMapper;

    @Autowired
    private LineItemRepository lineItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Override
    public Integer countByCart(String unionId) {
        return lineItemRepository.countByCartId(getCurrentCartIdByUnionId(unionId));
    }

    @Override
    public List<LineItemResult> getLineItemResultsByUserId(String unionId) {
        return LineItemResult.lineItemResultsBuild(lineItemMapper.selectLineItemResultsByUserId(unionId));
    }

    @Override
    public synchronized LineItemResult saveOrUpdateLineItemByLineItemId(LineItem receivedItem) {
        LineItem lineItem = lineItemRepository.findByCartIdAndProductIdAndFormatIdAndColorId(
                receivedItem.getCartId(),
                receivedItem.getProductId(),
                receivedItem.getFormatId(),
                receivedItem.getColorId());
        LineItemResult result = null;
        LineItem item = null;
        if (lineItem == null) {
            item = lineItemRepository.save(receivedItem);
        } else {
            lineItem.setQuantity(lineItem.getQuantity() + receivedItem.getQuantity());
            item = lineItemRepository.save(lineItem);
        }
        result = lineItemMapper.selectLineItemResultByLineItemId(item.getId());
        return LineItemResult.lineItemResultBuild(result);
    }

    @Override
    public void delete(Long lineItemId) {
        lineItemRepository.deleteById(lineItemId);
    }

    @Override
    public Long getCurrentCartIdByUnionId(String unionId) {
        User user = userService.findByUnionId(unionId);
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(user.getId());
            Cart save = cartRepository.save(cart);
            return save.getId();
        }
        return cart.getId();
    }
}