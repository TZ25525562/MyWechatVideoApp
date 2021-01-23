package com.tz.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tz.mapper.*;
import com.tz.pojo.*;
import com.tz.service.VideoService;
import com.tz.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    VideosMapper videosMapper;

    @Autowired
    SearchRecordsMapper searchRecordsMapper;

    @Autowired
    VideosPlusMapper videosPlusMapper;

    @Autowired
    UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    CommentsMapper  commentsMapper;

    @Override
    public String saveVideo(Videos videos) {
        String videoId = UUID.randomUUID().toString();
        videos.setId(videoId);
        videosMapper.insertSelective(videos);
        return videoId;
    }

    @Override
    public void updateVideo(String videoId, String coverPath) {
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(videos);

    }

    @Override
    public PageResult getAllVideosPlus(Videos videos, Integer isSaveRecord,Integer page, Integer pageSize) {

        String desc = videos.getVideoDesc();
        String userId = videos.getUserId();

//        保存热搜词
        if(isSaveRecord != null && isSaveRecord == 1){
            SearchRecords records = new SearchRecords();
            String s = UUID.randomUUID().toString();
            records.setId(s);
            records.setContent(desc);
            searchRecordsMapper.insert(records);
        }

        //pageHelper分页，并且封装
        PageHelper.startPage(page,pageSize);
        List<VideosPlus> list = videosPlusMapper.selectVideosPlusAll(desc,userId);
        PageInfo<VideosPlus> info = new PageInfo<>(list);

        PageResult result = new PageResult();
        result.setPage(page);
        result.setPageSize(info.getPageSize());
        result.setRecords(info.getPages());
        result.setRows(list);
        return result;

    }

    @Override
    public List<String> getHotWords() {
        return searchRecordsMapper.getAllRecords();
    }


    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreatorId) {

//        1.向用户和视频的关联表中插入数据
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        String Sid = UUID.randomUUID().toString();
        usersLikeVideos.setId(Sid);
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideosMapper.insert(usersLikeVideos);

//        2.视频受喜欢数加一
        videosPlusMapper.addVideoLikeCounts(videoId);

//        3.用户受喜欢数加一
        usersMapper.addUserLikeCounts(userId);
    }

    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreatorId) {
//        1.删除用户和视频的关联表中指定数据
        UsersLikeVideosExample example = new UsersLikeVideosExample();
        UsersLikeVideosExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andVideoIdEqualTo(videoId);
        usersLikeVideosMapper.deleteByExample(example);

//        2.视频受喜欢数减一
        videosPlusMapper.reduceVideoLikeCounts(videoId);

//        3.用户受喜欢数减一
        usersMapper.reduceUserLikeCounts(userId);
    }

    @Override
    public void saveComment(Comments comments) {
        String Sid = UUID.randomUUID().toString();
        comments.setId(Sid);
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
    }

    @Override
    public PageResult getAllComments(String videoId, Integer page, Integer pageSize) {

        List<UsersComments> usersComments = commentsMapper.queryComments(videoId);

        //pageHelper分页，并且封装
        PageHelper.startPage(page,pageSize);
        PageInfo<UsersComments> info = new PageInfo<>(usersComments);
        PageResult result = new PageResult();
        result.setPage(page);
        result.setPageSize(info.getPageSize());
        result.setRecords(info.getPages());
        result.setRows(usersComments);
        return result;
    }

    @Override
    public PageResult showLikeVideo(String userId, Integer page, Integer pageSize) {

        List<VideosPlus> videos = videosPlusMapper.queryLikeVideos(userId);
        //pageHelper分页，并且封装
//        for (VideosPlus video : videos) {
//            System.out.println(video.getCoverPath());
//        }
        PageHelper.startPage(page,pageSize);
        PageInfo<VideosPlus> info = new PageInfo<>(videos);
        PageResult result = new PageResult();
        result.setPage(page);
        result.setPageSize(info.getPageSize());
        result.setRecords(info.getPages());
        result.setRows(videos);
        return result;
    }

    @Override
    public PageResult showFollowVideo(String userId, Integer page, Integer pageSize) {
        List<VideosPlus> videos = videosPlusMapper.queryFollowVideos(userId);

        //pageHelper分页，并且封装
        PageHelper.startPage(page,pageSize);
        PageInfo<VideosPlus> info = new PageInfo<>(videos);
        PageResult result = new PageResult();
        result.setPage(page);
        result.setPageSize(info.getPageSize());
        result.setRecords(info.getPages());
        result.setRows(videos);
        return result;
    }
}
