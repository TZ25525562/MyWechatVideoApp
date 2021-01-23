package com.tz.controller;
import com.tz.service.UserService;
import com.tz.utils.FinalDataUtil;
import com.tz.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户注销接口
 */
@RestController
public class LogoutController {

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/logout")
    public JsonMsg logout(String userId){
        //删除redis中对应数据
        redisTemplate.delete(FinalDataUtil.REDIS_SESSION + userId);
        return JsonMsg.success().add("注销成功",null);
    }
}
