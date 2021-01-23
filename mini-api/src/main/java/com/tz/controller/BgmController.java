package com.tz.controller;

import com.tz.pojo.Bgm;
import com.tz.service.BgmService;
import com.tz.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * bgm Controller
 */
@RestController
@RequestMapping("/bgm")
public class BgmController {

    @Autowired
    BgmService bgmService;

    @PostMapping("/list")
    public JsonMsg getBgmList(){
        List<Bgm> bgms = bgmService.queryBgmList();
        return JsonMsg.success().add("bgmList",bgms);
    }
}
