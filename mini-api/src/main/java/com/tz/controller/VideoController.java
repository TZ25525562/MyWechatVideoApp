package com.tz.controller;


import com.tz.pojo.Comments;
import com.tz.pojo.Videos;
import com.tz.service.UserService;
import com.tz.service.VideoService;
import com.tz.utils.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 视频相关操作接口
 */
@RestController
@RequestMapping("/video")
public class VideoController extends FinalDataUtil{

    @Autowired
    UserService userService;

    @Autowired
    VideoService videoService;


    /**
     * 上传视频
     * @param userId
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/uploadVideo",headers = "content-type=multipart/form-data")
    public JsonMsg uploadVideo(String userId , MultipartFile file, double videoDuration,
                               String bgmId , double videoHeight, double videoWidth, String desc) throws Exception{

        if(StringUtils.isBlank(userId)){
            return JsonMsg.fail().add("上传失败","用户名为空");
        }

        String fileVideoPath = "";

        //文件保存的命名空间
        String fileSpace = "D:\\仿抖音小程序\\file-dev";
        //保存到数据库的相对路径
        String uploadPathDB = "/" + userId + "/video";
        //       封面相对路径
        String coverPathDB = "/" + userId + "/video";

        //初始化文件字节流
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            //获取文件名
            String filename = file.getOriginalFilename();
//            System.out.println(filename);
            if(!StringUtils.isBlank(filename)){
                //取xxx.mp4前端
                String fileNamePrefix = filename.split("\\.")[0];
                //文件的最终保存位置
                fileVideoPath = fileSpace + uploadPathDB + "/" + filename;
                //数据库保存的路径
                uploadPathDB += ("/" + filename);
                coverPathDB += ("/" + fileNamePrefix);

                File videoFile = new File(fileVideoPath);
                //文件上层目录路径存在或者上层不为文件目录
                if(videoFile.getParentFile() != null || !videoFile.getParentFile().isDirectory()){
                    //创建父级文件目录
                    videoFile.getParentFile().mkdirs();
                }
                fileOutputStream = new FileOutputStream(videoFile);
                inputStream = file.getInputStream();
                IOUtils.copy(inputStream,fileOutputStream);
            }else {
                return JsonMsg.fail().add("上传失败","视频为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        //调用ffmpeg方法截取图片
        FetchVideoCover ffmpeg = new FetchVideoCover("D:\\ffmpeg\\bin\\ffmpeg.exe");
        ffmpeg.getCover(fileVideoPath,fileSpace + coverPathDB + ".jpg");

        Videos videos = new Videos();
        videos.setAudioId(bgmId);
        videos.setVideoDesc(desc);
        videos.setVideoHeight((int)videoHeight);
        videos.setVideoWidth((int)videoWidth);
        videos.setVideoPath(uploadPathDB);
        videos.setVideoSeconds((float)videoDuration);
        //设置视频状态
        videos.setStatus(VideoStatus.SUCCESS.getValue());
        videos.setCreateTime(new Date());
        videos.setUserId(userId);
//      设置封面
        videos.setCoverPath(coverPathDB + ".jpg");

        //保存视频至数据库
        String videoId = videoService.saveVideo(videos);


        return JsonMsg.success().add("上传成功","视频已经保存至指定位置").add("videoId",videoId);
    }

    @PostMapping(value = "/uploadCover",headers = "content-type=multipart/form-data")
    public JsonMsg uploadCover(String videoId , String userId, MultipartFile file) throws Exception{

        if(StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)){
            return JsonMsg.fail().add("上传失败","视频为空");
        }

        //文件保存的命名空间
        String fileSpace = "D:\\仿抖音小程序\\file-dev";
        //保存到数据库的相对路径
        String uploadPathDB = "/" + userId + "/video";
        //初始化文件字节流
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            //获取文件名
            String filename = file.getOriginalFilename();
            if(!StringUtils.isBlank(filename)){
                //文件的最终保存位置
                String fileCoverPath = fileSpace + uploadPathDB + "/" + filename;
                //数据库保存的路径
                uploadPathDB += ("/" + filename);

                File videoFile = new File(fileCoverPath);
                //文件上层目录路径存在或者上层不为文件目录
                if(videoFile.getParentFile() != null || !videoFile.getParentFile().isDirectory()){
                    //创建父级文件目录
                    videoFile.getParentFile().mkdirs();
                }
                fileOutputStream = new FileOutputStream(videoFile);
                inputStream = file.getInputStream();
                IOUtils.copy(inputStream,fileOutputStream);
            }else {
                return JsonMsg.fail().add("上传失败","视频为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        videoService.updateVideo(videoId,uploadPathDB);

        return JsonMsg.success().add("上传成功","封面已经保存至指定位置");
    }

    /**
     * 分页和搜索查询视频列表，
     * isSaveRecord ：1 - 保存热搜词
     *                0 - 不保存热搜词
     * @param page
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/showAll")
    public JsonMsg showAll(@RequestBody Videos videos, Integer isSaveRecord,Integer page) throws Exception{
        if (page == null){
            page = 1;
        }


        PageResult result = videoService.getAllVideosPlus(videos,isSaveRecord,page, PAGE_SIZE);


        return  JsonMsg.success().add("data", result);


    }

    /**
     * 显示用户收藏视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/showLike")
    public JsonMsg showLike(String userId, Integer page, Integer pageSize) throws Exception{

        if(StringUtils.isBlank(userId)){
            return JsonMsg.fail().add("error","用户名为空");
        }

        if (page == null){
            page = 1;
        }

        if (pageSize == null){
            pageSize = 6;
        }

        PageResult result = videoService.showLikeVideo(userId,page,pageSize);
//        System.out.println(result);
        return  JsonMsg.success().add("data", result);


    }

    @PostMapping(value = "/showFollow")
    public JsonMsg showFollow(String userId, Integer page, Integer pageSize) throws Exception{

        if(StringUtils.isBlank(userId)){
            return JsonMsg.fail().add("error","用户名为空");
        }

        if (page == null){
            page = 1;
        }

        if (pageSize == null){
            pageSize = 6;
        }

        PageResult result = videoService.showFollowVideo(userId,page,pageSize);

        return  JsonMsg.success().add("data", result);

    }

    /**
     * 获取所有热搜词按照搜索次数排序
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/hot")
    public JsonMsg getHotWords() throws Exception{

        List<String> hotWords = videoService.getHotWords();
        return  JsonMsg.success().add("data",hotWords);

    }

    /**
     * 用户喜欢视频点赞接口
     * @param userId
     * @param videoId
     * @param videoCreatorId
     * @return
     */
    @PostMapping("/userLike")
    public JsonMsg userLikeVideo(String userId , String videoId , String videoCreatorId){
        videoService.userLikeVideo(userId,videoId,videoCreatorId);
        return JsonMsg.success();
    }

    /**
     * 用户不喜欢视频取消点赞接口
     * @param userId
     * @param videoId
     * @param videoCreatorId
     * @return
     */
    @PostMapping("/userUnLike")
    public JsonMsg userUnLikeVideo(String userId , String videoId , String videoCreatorId){
        videoService.userUnLikeVideo(userId,videoId,videoCreatorId);
        return JsonMsg.success();
    }

    /**
     * 保存留言接口
     * @param comments
     * @return
     */
    @PostMapping("/saveComment")
    public JsonMsg saveComment(@RequestBody Comments comments,String toUserId,String fatherCommentId){
        comments.setFatherCommentId(fatherCommentId);
        comments.setToUserId(toUserId);
        videoService.saveComment(comments);
        return JsonMsg.success();
    }


    /**
     * 查询视频下所有用户的评论，并且分页显示
     * @param videoId
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/getAllComments")
    public JsonMsg getAllComments(String videoId, Integer page,Integer pageSize) throws Exception{

        if (page == null){
            page = 1;
        }

        if (pageSize == null){
            pageSize = 10;
        }


        PageResult result = videoService.getAllComments(videoId,page,pageSize);


        return  JsonMsg.success().add("data", result);


    }



}
