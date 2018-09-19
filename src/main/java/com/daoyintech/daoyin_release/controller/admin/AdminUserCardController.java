package com.daoyintech.daoyin_release.controller.admin;

import com.daoyintech.daoyin_release.entity.card.UserCard;
import com.daoyintech.daoyin_release.repository.card.UserCardRepository;
import com.daoyintech.daoyin_release.response.card.UserCardResponse;
import com.daoyintech.daoyin_release.service.UserCardService;
import com.daoyintech.daoyin_release.service.admin.AdminUserCardsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pei on 2018/7/20
 *
 */

@Slf4j
@Controller
@RequestMapping(value = "/admin")
public class AdminUserCardController {

    @Autowired
    private AdminUserCardsService adminUserCardsService;

    @Autowired
    private UserCardRepository userCardRepository;

    @Autowired
    private UserCardService userCardService;



    @GetMapping
    public String search() {
        return "function/search";
    }


    @PostMapping
    public String query(@RequestParam("cardCode") String cardCode) {
        Boolean isExist = adminUserCardsService.queryUserCardByCardNo(cardCode);
        if(isExist) {
            return  "redirect:/admin/" + cardCode;
        }
        return "result/no_result";
    }


    @ApiOperation("用户联系客服使用卡卷,客服核实查卷")
    @GetMapping("/{cardCode}")
    public String cardNo(@PathVariable String cardCode, Model model) {
        UserCardResponse card = adminUserCardsService.getUserCardByCardNo(cardCode);
        if (card == null){
            return "function/search";
        }
        model.addAttribute("user_card", card);
        return "show/show";
    }


    @ApiOperation("已经使用,卡卷核销")
    @GetMapping("/{cardCode}/destroy")
    public String destroy(@PathVariable String cardCode, Model model) {
        String message = "卡卷核销成功";
        UserCard userCard = userCardRepository.findByUserCardCode(cardCode);
        userCard.setStatus(4);
        userCardRepository.save(userCard);
        model.addAttribute("message",message);
        return "result/result";
    }



    /**
     * 卡卷类表
     * */
    @ApiOperation("未使用卡卷类表")
    @GetMapping("/list")
    public String cardList(Model model){
       List<UserCard> userCardList = userCardService.findByStatus(2);
       model.addAttribute("userCardList", userCardList);
       return "show/show_list";
    }





}








