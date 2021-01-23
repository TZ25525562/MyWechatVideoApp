package com.tz.serviceImpl;

import com.tz.mapper.UsersFansMapper;
import com.tz.mapper.UsersLikeVideosMapper;
import com.tz.mapper.UsersMapper;
import com.tz.mapper.UsersReportMapper;
import com.tz.pojo.*;
import com.tz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.JdkIdGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    UsersFansMapper usersFansMapper;

    @Autowired
    UsersReportMapper usersReportMapper;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean judgeUsernameIsExist(String id) {
        Users users = usersMapper.selectByPrimaryKey(id);
//        System.out.println(users);
        //不存在,可以注册 存在，不可以注册
        return !(users == null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(Users users) {
        //生成唯一id
        JdkIdGenerator idGenerator = new JdkIdGenerator();
        String id = idGenerator.generateId().toString();
        users.setId(id);
        usersMapper.insert(users);


    }

    @Override
    public boolean judgeForLogin(String username, String password) {
        //附加用户名和密码信息
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(password);
        //查询判断是否正确
        List<Users> users = usersMapper.selectByExample(example);
        if (users.size() == 0 ){
            //没有匹配的用户信息
            return false;
        }
        //有匹配的用户信息
        return true;
    }

    @Override
    public String getUserId(Users users) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(users.getUsername());
        List<Users> list = usersMapper.selectByExample(example);
        String id = null;
        for (Users user : list) {
            id = user.getId();
        }
        return id;
    }

    @Override
    public void updateUserInfo(Users users) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(users.getId());
        usersMapper.updateByExampleSelective(users,example);
    }

    @Override
    public Users getUsersInfo(String userId) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(userId);
        Users users = new Users();
        List<Users> usersList = usersMapper.selectByExample(example);
        for (Users user : usersList) {
            users = user;
        }
        return users;
    }

    @Override
    public boolean isUserLikeVideos(String userId, String videoId) {
        UsersLikeVideosExample example = new UsersLikeVideosExample();
        UsersLikeVideosExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andVideoIdEqualTo(videoId);
        List<UsersLikeVideos> usersLikeVideos = usersLikeVideosMapper.selectByExample(example);
        //关系表中有数据，代表用户喜欢过该视频
        if(usersLikeVideos != null && usersLikeVideos.size() != 0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUsersFans(String userId, String fanId) {
        String Sid = UUID.randomUUID().toString();
        UsersFans usersFans = new UsersFans();
        usersFans.setId(Sid);
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);
//        表中添加数据
        usersFansMapper.insert(usersFans);

        usersMapper.addFans(userId);
        usersMapper.addFollowers(fanId);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void reduceUsersFans(String userId, String fanId) {
        UsersFansExample example = new UsersFansExample();
        UsersFansExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andFanIdEqualTo(fanId);
//        表中删除数据
        usersFansMapper.deleteByExample(example);
        usersMapper.reduceFans(userId);
        usersMapper.reduceFollowers(fanId);
    }

    @Override
    public boolean isUserFans(String userId, String fanId) {
        UsersFansExample example = new UsersFansExample();
        UsersFansExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andFanIdEqualTo(fanId);
        List<UsersFans> list = usersFansMapper.selectByExample(example);
        if(list != null && list.isEmpty() == false && list.size() >0 ){
            return true;
        }
        return false;
    }

    @Override
    public void reportUser(UsersReport usersReport) {
        String Sid = UUID.randomUUID().toString();
        usersReport.setId(Sid);
//        //时间格式转换
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        String s = format.format(date);
        usersReport.setCreateTime(new Date());
        usersReportMapper.insert(usersReport);
    }
}
