package com.tz.service;


import com.tz.pojo.Comments;
import com.tz.pojo.Videos;
import com.tz.utils.PageResult;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * 处理视频相关接口
 */
public interface VideoService {

    /**
     * 保存视频信息，并且返回视频id
     * @param videos
     * @return
     */
    String saveVideo(Videos videos);

    /**
     * 根据视频id，更新视频信息(修改封面)
     * @param videoId
     * @param coverPath
     */
    void updateVideo(String videoId ,String coverPath);


    /**
     * 获取所有VideosPlus数据并且分页显示,并根据isSaveRecord来判断是否保存热搜词
     * @param page
     * @param pageSize
     * @return
     */
    PageResult getAllVideosPlus(Videos videos, Integer isSaveRecord,Integer page , Integer pageSize);

    /**
     * 获取所有的热搜词并按数据库次数排序
     * @return
     */
    List<String> getHotWords();

    /**
     * 用户喜欢/点赞视频
     * @param userId
     * @param videoId
     * @param videoCreatorId 视频创建者id
     */
    void userLikeVideo(String userId ,String videoId, String videoCreatorId);

    /**
     * 用户不喜欢/取消点赞视频
     * @param userId
     * @param videoId
     * @param videoCreatorId 视频创建者id
     */
    void userUnLikeVideo(String userId ,String videoId, String videoCreatorId);

    /**
     * 保存留言
     * @param comments
     */
    void saveComment(Comments comments);

    /**
     * 留言分页
     * @param videoId
     * @param page
     * @param pageSize
     * @return
     */
    PageResult getAllComments(String videoId,Integer page, Integer pageSize);

    /**
     * 显示收藏视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PageResult showLikeVideo(String userId , Integer page , Integer pageSize);

    /**
     * 显示关注者的视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PageResult showFollowVideo(String userId , Integer page , Integer pageSize);

}
