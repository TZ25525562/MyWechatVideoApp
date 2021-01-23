package com.tz.service;

import com.tz.pojo.Users;
import com.tz.pojo.UsersReport;

/**
 * 用户注册相关接口
 */
public interface UserService {

    /**
     * 判断用户名是否存在: true 存在 ;false 不存在
     * @param username
     * @return
     */
    boolean judgeUsernameIsExist(String username);


    /**
     * 保存用户信息
     * @param users
     */
    void saveUser(Users users);

    /**
     * 根据用户名和密码判断用户信息是否存在
     * @param username
     * @param password
     * @return
     */
    boolean judgeForLogin(String username, String password);

    /**
     * 获取用户id
     * @param users
     * @return
     */
    String getUserId(Users users);

    /**
     * 更新用户信息
     * @param users
     */
    void updateUserInfo(Users users);

    /**
     * 获取用户所有信息
     * @param UserId
     * @return
     */
    Users getUsersInfo(String UserId);

    /**
     * 判断用户是否喜欢该视频
     * @param userId
     * @param videoId
     * @return
     */
    boolean isUserLikeVideos(String userId, String videoId);

    /**
     * 每添加一个粉丝数，就添加对应用户和粉丝之间的关系
     * @param userId
     * @param fanId
     */
    void addUsersFans(String userId , String fanId);

    /**
     * 每减少一个粉丝数，就删除对应用户和粉丝之间的关系
     * @param userId
     * @param fanId
     */
    void reduceUsersFans(String userId , String fanId);

    /**
     * 判断是否为用户粉丝
     * @param userId
     * @param fanId
     * @return
     */
    boolean isUserFans(String userId , String fanId);


    /**
     * 举报用户接口
     * @param usersReport
     */
    void reportUser(UsersReport usersReport);

}
