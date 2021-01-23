package com.tz.mapper;

import com.tz.pojo.VideosPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义mapper
 */
@Mapper
public interface VideosPlusMapper {

    /**
     * 查询所有videosPlus信息
     * @param videoDesc
     * @return
     */
    List<VideosPlus> selectVideosPlusAll(@Param("videoDesc") String videoDesc,@Param("userId") String userId);

    /**
     * 视频受喜欢数加一
     * @param videoId
     */
    void addVideoLikeCounts(String videoId);

    /**
     * 视频受喜欢数减一
     * @param videoId
     */
    void reduceVideoLikeCounts(String videoId);


    /**
     * 查询用户喜欢视频
     * @param userId
     * @return
     */
    List<VideosPlus> queryLikeVideos( String userId);


    /**
     * 查询用户关注者视频
     * @param userId
     * @return
     */
    List<VideosPlus> queryFollowVideos( String userId);
}
