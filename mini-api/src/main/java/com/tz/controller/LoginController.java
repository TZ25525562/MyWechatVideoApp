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
 * 用户登录接口
 */
@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/login")
    public JsonMsg login(@RequestBody Users users){
        //获取用户名和密码
        String username = users.getUsername();
        String password = users.getPassword();
        //1.判断用户名和密码是否为空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return JsonMsg.fail().add("登录失败","用户名或密码为空");
        }

        //2.根据用户名和密码判断用户名和密码是否正确
        boolean isLogin = userService.judgeForLogin(username,DigestUtils.md5DigestAsHex(password.getBytes()));

        if (isLogin == false){
            JsonMsg msg = JsonMsg.fail().add("登录失败", "用户名和密码不正确");
            msg.setMessage("登录失败");
            return msg;
        }
        else {
            //3.登陆成功后生成redis-session保存到redis中
            String session = FinalDataUtil.REDIS_SESSION + userService.getUserId(users);
            //设置id信息，传入前端
            users.setId(userService.getUserId(users));
            redisTemplate.opsForValue().set(session, userService.getUserId(users),60,TimeUnit.MINUTES);
//            redisTemplate.expire(session,1,TimeUnit.MINUTES);
            return JsonMsg.success().add("登录成功", null).add("data",users);
        }
    }
}
