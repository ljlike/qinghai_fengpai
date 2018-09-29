package com.daoyintech.daoyin_release.controller.admin;

import com.daoyintech.daoyin_release.entity.card.WxCard;
import com.daoyintech.daoyin_release.service.admin.AdminWxCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author pei on 2018/09/28
 */
@Slf4j
@Controller
@RequestMapping(value = "/wxCard")
public class AdminWxCardController {

    @Autowired
    private AdminWxCardService adminWxCardService;


    @GetMapping
    public String wxCard() {
        return "function/wxCard";
    }


    /**
     * 新增活动卡券
     * */
    @PostMapping("/add")
    public String add(String title, String date, Integer type,Model model){
        String message = adminWxCardService.addWxCard(title,date,type);
        if (StringUtils.isEmpty(message)){
            return "function/wxCard";
        }
        model.addAttribute("message",message);
        return "result/result";
    }


    /**
     * 查询所有活动卡券
     * */
    @GetMapping("/list")
    public String wxCardList(Model model){
        List<WxCard> wxCards = adminWxCardService.wxCardList();
        model.addAttribute("wxCards", wxCards);
        return "show/wxCard_show_list";
    }

    /**
     * 上架或下架活动卡券
     * */
    @GetMapping("/{id}/upOrDownCard")
    public String upOrDownCard(@PathVariable Long id,Model model){
        List<WxCard> wxCards = adminWxCardService.upOrDownCard(id);
        model.addAttribute("wxCards", wxCards);
        return "show/wxCard_show_list";
    }


}





