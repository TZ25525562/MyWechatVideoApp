package com.tz.controller;

import com.tz.pojo.Users;
import com.tz.service.UserService;
import com.tz.utils.FinalDataUtil;
import com.tz.utils.JsonMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

/**
 * 用户注册接口
 */
@RestController
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/register")
    public JsonMsg register(@RequestBody Users users){
        //1.判断用户名和密码是否为空
        if(StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())){
            return JsonMsg.fail().add("注册失败","用户名或密码为空");
        }

        //2.判断用户名是否存在
        boolean isExist = userService.judgeUsernameIsExist(users.getId());

        if (isExist == true){
            JsonMsg msg = JsonMsg.fail().add("注册失败", "用户名已存在");
            msg.setMessage("登录失败");
            return msg;
        }
        else {
            users.setFansCounts(0);
            users.setFollowCounts(0);
            users.setReceiveLikeCounts(0);
            users.setNickname(users.getUsername());
            //spring自带Md5对密码加密然后封装
            users.setPassword(DigestUtils.md5DigestAsHex(users.getPassword().getBytes()));
            //3.保存用户名信息
            userService.saveUser(users);
            //4.生成redis-session保存到redis中
            String session = FinalDataUtil.REDIS_SESSION + userService.getUserId(users);
//            System.out.println(session);
            //设置id信息，传入前端
            users.setId(userService.getUserId(users));
            redisTemplate.opsForValue().set(session, userService.getUserId(users),60,TimeUnit.MINUTES);
//            redisTemplate.expire(session,100,TimeUnit.MINUTES);
            return JsonMsg.success().add("注册成功", null).add("data",users);
        }
    }
}
