package com.tz.controller;

import com.tz.pojo.UserFans;
import com.tz.pojo.Users;
import com.tz.pojo.UsersReport;
import com.tz.service.UserService;
import com.tz.utils.JsonMsg;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 用户相关操作接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    /**
     * 上传头像
     * @param userId
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFace")
    public JsonMsg uploadFace(String userId , @RequestParam("file")MultipartFile[] files) throws Exception{

        if(StringUtils.isBlank(userId)){
            return JsonMsg.fail().add("上传失败","用户名为空");
        }

        //文件保存的命名空间
        String fileSpace = "D:\\仿抖音小程序\\file-dev";
        //保存到数据库的相对路径
        String uploadPathDB = "/" + userId + "/face";

        //初始化文件字节流
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            //获取文件名
            String filename = files[0].getOriginalFilename();
//            System.out.println(filename);
            if(!StringUtils.isBlank(filename)){
                //文件的最终保存位置
                String fileFacePath = fileSpace + uploadPathDB + "/" + filename;
//                System.out.println(fileFacePath);
                //数据库保存的路径
                uploadPathDB += ("/" + filename);

                File file = new File(fileFacePath);
                //文件上层目录路径存在或者上层不为文件目录
                if(file.getParentFile() != null || !file.getParentFile().isDirectory()){
                    //创建父级文件目录
                    file.getParentFile().mkdirs();
                }
                fileOutputStream = new FileOutputStream(file);
                inputStream = files[0].getInputStream();
                IOUtils.copy(inputStream,fileOutputStream);
            }else {
                return JsonMsg.fail().add("上传失败","文件为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        //保存图像路径至数据库中
        Users users = new Users();
        users.setId(userId);
        users.setFaceImage(uploadPathDB);
        userService.updateUserInfo(users);

        return JsonMsg.success().add("上传成功","图片已经保存至指定位置").add("data",uploadPathDB);
    }

    /**
     * 获取用户相关信息
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/query")
    public JsonMsg query(String userId ,String fanId) throws Exception{
        if (StringUtils.isBlank(userId)){
            return JsonMsg.fail().add("获取失败","用户名为空");
        }

        Users usersInfo = userService.getUsersInfo(userId);
        boolean userFans = userService.isUserFans(userId, fanId);
        UserFans fans = new UserFans();
        fans.setUsers(usersInfo);
        fans.setFollow(userFans);
        return JsonMsg.success().add("data",fans);
    }


    /**
     * 查询视频发布者相关信息
     * @param loginUserId
     * @param videoId
     * @param publisherId
     * @return
     * @throws Exception
     */
    @PostMapping("/queryPublisher")
    public JsonMsg queryPublisher(String loginUserId ,String videoId,String publisherId) throws Exception{
        if (StringUtils.isBlank(publisherId)){
            return JsonMsg.fail().add("获取失败","id为空");
        }
//      1.查询视频发布者信息
        Users usersInfo = userService.getUsersInfo(publisherId);

//      2.判断用户是否喜欢该视频
        boolean userLikeVideos = userService.isUserLikeVideos(loginUserId, videoId);

        return JsonMsg.success().add("data",usersInfo).add("isLikeVideo",userLikeVideos);
    }


    /**
     * 成为粉丝
     * @param userId
     * @param fanId
     * @return
     * @throws Exception
     */
    @PostMapping("/befans")
    public JsonMsg beFans(String userId,String fanId ) throws Exception{
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(fanId)){
            return JsonMsg.fail().add("关注失败","用户名或者粉丝名为空");
        }
        userService.addUsersFans(userId,fanId);
        return JsonMsg.success();
    }

    /**
     * 取消成为粉丝
     * @param userId
     * @param fanId
     * @return
     * @throws Exception
     */
    @PostMapping("/neverbefans")
    public JsonMsg neverBeFans(String userId,String fanId ) throws Exception{
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(fanId)){
            return JsonMsg.fail().add("取消关注失败","用户名或者粉丝名为空");
        }
        userService.reduceUsersFans(userId,fanId);
        return JsonMsg.success();
    }

    /**
     * 用户举报接口
     * @param usersReport
     * @return
     * @throws Exception
     */
    @PostMapping("/reportUser")
    public JsonMsg reportUser(@RequestBody UsersReport usersReport) throws Exception{

        userService.reportUser(usersReport);

        return JsonMsg.success().add("response","举报成功!");
    }
}
