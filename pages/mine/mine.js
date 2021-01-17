const app = getApp()
var videoUtil = require('../../utils/videoUtil.js')
Page({
  data: {
    faceUrl: '../resources/images/face.jpg',
    fanscounts: "",
    followcounts: "",
    receivelikecounts: "",
    nickname: "",
    // 用户的页面还是跳转别人的页面
    isMe: true,
    // 是否关注
    isFollow: false,
    // 视频发布者id
    publisherId: "",
    // 视频介绍栏的样式
    videoInfo:"video-info",
    videoInfoWork:"",
    videoInfoLike:"",
    videoInfoFollow:"",
  },

  logout: function () {
    //获取用户信息
    //var user = app.userInfo;
    //获取本地缓存
    var user = app.getGlobalUserInfo();
    var serverurl = app.serverUrl;
    // 显示loading框
    wx.showLoading({
      title: '加载中',
    })
    // 发送请求
    wx.request({
      url: serverurl + '/logout?userId=' + user.id,
      method: "POST",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        //  console.log(res);
        var status = res.data.status;
        //相应成功后loading提示框消失
        wx.hideLoading({
          // success: (res) => {},
        })
        //回显到登录页面
        if (status == 200) {
          wx.showToast({
            title: '注销成功！',
            icon: 'success',
            duration: 5000,
          })
          wx.navigateTo({
            url: '../userLogin/Login',
          })
          //清空个人信息
          //App.userInfo = null;
          //从本地缓存中移除指定 key
          wx.removeStorageSync('userInfo');
        } else {
          wx.showToast({
            title: res.data.message,
            icon: 'none',
            duration: 5000,
          })
        }
      }
    })
  },

  chooseImage: function () {
    var me = this;
    //var user = app.userInfo;
    //获取本地缓存
    var user = app.getGlobalUserInfo();
    var serverurl = app.serverUrl;
    wx.chooseImage({
      //所选图片数目
      count: 1,
      //图片尺寸类型
      sizeType: ['original', 'compressed'],
      // 图片来源
      sourceType: ['album', 'camera'],
      success(res) {
        wx.showLoading({
          title: '上传中',
        })
        // tempFilePath可以作为img标签的src属性显示图片
        var tempFilePaths = res.tempFilePaths
        wx.uploadFile({
          filePath: tempFilePaths[0],
          //对应后端的key以供获取
          name: 'file',
          url: serverurl + '/user/uploadFace?userId=' + user.id,
          header: {
            'content-type': 'multipart/form-data' // 默认值
          },
          success(res) {
            //格式化为Json数据
            console.log(res);
            var data = JSON.parse(res.data);
            // console.log(data);
            var status = data.status;
            wx.hideLoading({
            })
            if (status == 200) {
              wx.showToast({
                title: '上传成功',
                icon: 'success',
                duration: 5000
              })
              var imageUrl = serverurl + data.map.data;
              me.setData({
                faceUrl: imageUrl,
              })
              //console.log(imageUrl);
            } else {
              wx.showToast({
                title: data.message,
                icon: 'none',
                duration: 5000
              })
            }
          }
        })
      }
    })


  },

  //页面加载显示用户信息
  onLoad: function (params) {
    //var user = app.userInfo;
    //获取本地缓存
    var user = app.getGlobalUserInfo();
    // console.log(user);
    var serverurl = app.serverUrl;
    var me = this;
    var userId = user.id;
    var publisherId = params.publisherId;
    if (publisherId != null && publisherId != "" && publisherId != undefined) {
      userId = publisherId;
      me.setData({
        isMe: false,
        publisherId: publisherId,
      })
    }
    // 显示loading框
    wx.showLoading({
      title: '加载中',
    })
    // 发送请求
    wx.request({
      url: serverurl + '/user/query?userId=' + userId + "&fanId=" + user.id,
      method: "POST",
      header: {
        'content-type': 'application/json',// 默认值
        'userId': user.id,
      },
      success(res) {
        //console.log(res);
        var status = res.data.status;
        //相应成功后loading提示框消失
        wx.hideLoading({
        })
        //回显到个人信息页面
        if (status == 200) {
          var data = res.data.map.data.users;
          var isFollow = res.data.map.data.follow;
          // console.log(res);
          var imageUrl = data.faceImage;
          var faceUrl = "../resources/images/face.jpg";
          if (imageUrl != null && imageUrl != '' &&
            imageUrl != undefined) {
            faceUrl = serverurl + imageUrl;
          }
          // 设置全局变量功关注点击函数来实时刷新
          me.fansCounts =  data.fansCounts;
          me.setData({
            faceUrl: faceUrl,
            fanscounts: data.fansCounts,
            followcounts: data.followCounts,
            receivelikecounts: data.receiveLikeCounts,
            nickname: data.nickname,
            isFollow:isFollow,
          })
        }
      }
    })
  },

  // 关注点击函数
  followMe: function (e) {
    // console.log(e);
    var me = this;
    var followMe = e.currentTarget.dataset.followme;
    var user = app.getGlobalUserInfo();
    var url = "";
    // 0:未关注 1：已关注
    if (followMe == "0") {
      url = "/user/befans?userId=" + me.data.publisherId + "&fanId=" + user.id;
    } else  {
      url = "/user/neverbefans?userId=" + me.data.publisherId + "&fanId=" + user.id;
    }
    wx.showLoading();
    wx.request({
      url: app.serverUrl + url,
      method: "POST",
      header: {
        'content-type': 'application/json',// 默认值
        'userId': user.id,
      },
      success: function () {
        wx.hideLoading();
        // console.log(followMe);
        console.log(me.data.fansCounts);
        if (followMe == "0") {
          me.setData({
            isFollow: true,
            // 实时刷新粉丝数
            fanscounts: ++me.fansCounts,
          })
        } else  {
          me.setData({
            isFollow: false,
            fanscounts: --me.fansCounts,
          })
        }
        // console.log(me.data.isFollow);
      }
    })
  },

  //上传视频
  uploadVedio: function () {
    // videoUtil.uploadVedio();
    wx.chooseVideo({
      sourceType: ['album', 'camera'],
      maxDuration: 60,
      camera: 'back',
      compressed: true,
      success(res) {
        wx.showLoading({
          title: '上传中',
        })
        // console.log(res)
        var tempDuration = res.duration;
        var tempHeight = res.height;
        var tempFilePath = res.tempFilePath;
        var thumbTempFilePath = res.thumbTempFilePath;
        var tempWidth = res.width;
        // 视频超过11秒
        if (tempDuration > 300) {
          wx.showToast({
            title: '视频长度不能超过300秒',
            icon: 'none',
            duration: 5000,
          })
        } else if (tempDuration < 1) {
          wx.showToast({
            title: '视频长度不能小于1秒',
            icon: 'none',
            duration: 5000,
          })
          // 选择Bgm
        } else {
          wx.navigateTo({
            url: '../chooseAudio/chooseAudio?tempDuration=' + tempDuration +
              '&tempHeight=' + tempHeight +
              '&tempFilePath=' + tempFilePath +
              '&thumbTempFilePath=' + thumbTempFilePath +
              '&tempWidth=' + tempWidth,
          })
        }
      }
    })
  },

  // 下面三个函数为视频介绍栏的点击函数
  selectWork:function(){
    var me = this;
    me.setData({
      videoInfoWork:"video-info-work",
      videoInfoFollow:"",
      videoInfoLike:"",
    })
  },

  selectLike:function(){
    var me = this;
    me.setData({
      videoInfoFollow:"",
      videoInfoWork:"",
      videoInfoLike:"video-info-like",
    })
  },

  selectFollow:function(){
    var me = this;
    me.setData({
      videoInfoWork:"",
      videoInfoLike:"",
      videoInfoFollow:"video-info-follow",
    })
  },
})
