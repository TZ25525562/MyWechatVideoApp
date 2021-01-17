// ReferenceError: app is not defined的解决办法
var app = getApp();
var videoUtil = require('../../utils/videoUtil.js')
Page({
  data: {
    serverUrl: app.serverUrl,
    showStyle: "cover",
    // 视频地址
    src: "",
    videoId: "",
    videoInfo: {},
    // 用户是否喜欢视频
    userLikeVideo: false,
    // 登录用户id
    loginUserId: "",
    // 视频发布者信息
    publisher: "",
  },

  //全局变量，用来保存videoContent
  videoContent: {},

  onLoad: function (params) {
    var me = this;
    // 获取videoContent
    me.videoContent = wx.createVideoContext('myVideo', me);
    // 获取视频信息，转成JSON
    var videoInfo = JSON.parse(params.videoInfo);
    //  console.log(videoInfo);

    var height = videoInfo.videoHeight;
    var width = videoInfo.videoWidth;
    var showStyle = "cover";
    // 视频是宽的，则不覆盖
    if (width >= height) {
      showStyle = "";
    }
    me.setData({
      src: app.serverUrl + videoInfo.videoPath,
      videoId: videoInfo.id,
      videoInfo: videoInfo,
      showStyle: showStyle,
    })

    var user = app.getGlobalUserInfo();
    var loginUserId = "";
    if (user != null || user != "" || user != undefined) {
      loginUserId = user.id;
    }
    // 页面初始化展示调用
    wx.request({
      url: me.data.serverUrl + '/user/queryPublisher?loginUserId=' + loginUserId + "&videoId="
        + videoInfo.id + "&publisherId=" + videoInfo.userId,
      method: "POST",
      success: function (res) {
        // console.log(res);
        var userLikeVideo = res.data.map.isLikeVideo;
        var publisher = res.data.map.data;

        me.setData({
          userLikeVideo: userLikeVideo,
          publisher: publisher,
        })
      }
    })
  },


  // 点击发布者头像函数
  showPublisher: function () {
    var me = this;
    //登录成功后的回调页面（本页）,? 和= 传入与下面重复，无法被读取
    // var videoInfo = JSON.stringify(me.data.videoInfo);
    var videoInfo = me.data.videoInfo;
    var realUrl = "../mine/mine#publisherId@" + videoInfo.userId;
    var user = app.getGlobalUserInfo();
    // 用户未登录，跳转到登录页面，否则跳转到此界面
    if (user == null || user == "" || user == undefined) {
      wx.navigateTo({
        url: '../userLogin/Login?realUrl=' + realUrl,
      })
    } else {
      wx.navigateTo({
        url: '../mine/mine?publisherId=' + videoInfo.userId,
      })
    }
  },


  onShow: function () {
    // 播放声音
    var me = this;
    me.videoContent.play();
  },

  onHide: function () {
    // 跳转暂停播放声音
    var me = this;
    me.videoContent.pause();
  },

  // 搜索函数
  search: function () {
    wx.navigateTo({
      url: '../searchVideo/searchVideo',
    })
  },

  // 上传视频函数
  upload: function () {
    var me = this;
    //登录成功后的回调页面（本页）,? 和= 传入与下面重复，无法被读取
    var videoInfo = JSON.stringify(me.data.videoInfo);
    var realUrl = "../videoInfo/videoInfo#videoInfo@" + videoInfo;
    var user = app.getGlobalUserInfo();
    // 用户未登录，跳转到登录页面，否则跳转到此界面
    if (user == null || user == "" || user == undefined) {
      wx.redirectTo({
        url: '../userLogin/Login?realUrl=' + realUrl,
      })
    } else {
      videoUtil.uploadVedio();
    }
  },

  // 返回主页函数
  backHome: function () {
    wx.redirectTo({
      url: '../index/index',
    })
  },

  //回到我的主页
  showMine: function () {
    var user = app.getGlobalUserInfo();
    // debugger;
    // 用户未登录，跳转到登录页面，否则跳转到我的主页
    if (user == null || user == "" || user == undefined) {
      wx.redirectTo({
        url: '../userLogin/Login',
      })
    } else {
      wx.redirectTo({
        url: '../mine/mine',
      })
    }
  },

  //用户点赞视频函数 
  userLikeVideoOrNot: function () {
    var me = this;
    var videoInfo = me.data.videoInfo;
    var user = app.getGlobalUserInfo();
    // 用户未登录，跳转到登录页面，否则跳转到我的主页
    if (user == null || user == "" || user == undefined) {
      wx.redirectTo({
        url: '../userLogin/Login',
      })
    } else {
      var userLikeVideo = me.data.userLikeVideo;
      var url = "/video/userLike?userId=" + user.id + "&videoId=" +
        videoInfo.id + "&videoCreatorId=" + videoInfo.userId;
      // 取消点赞触发相应接口
      if (userLikeVideo) {
        var url = "/video/userUnLike?userId=" + user.id + "&videoId=" +
          videoInfo.id + "&videoCreatorId=" + videoInfo.userId;
      }
      var serverUrl = app.serverUrl;
      wx.showLoading({
        title: '...',
      })
      wx.request({
        url: serverUrl + url,
        method: "POST",
        header: {
          'content-type': 'application/json',// 默认值
          'userId': user.id,//通行拦截器
        },
        success: function (res) {
          wx.hideLoading();
          me.setData({
            userLikeVideo: !userLikeVideo,//变红或者变白
          })
        }
      })
    }
  },

  // 分享，举报，下载按钮函数
  shareMe: function () {
    var me = this;
    var serverUrl = app.serverUrl;
    wx.showActionSheet({
      itemList: ['下载到本地', '举报用户', '分享到微信', '分享到QQ', '分享到微博'],
      success: function (res) {
        // 弹出框的选项索引
        var index = res.tapIndex;
        if (index == 0) {
          //下载到本地 
          // 下载文件，返回临时文件路径
          wx.showLoading();
          wx.downloadFile({
            url: serverUrl + me.data.videoInfo.videoPath, 
            success (res) {
              // 只要服务器有响应数据，就会把响应内容写入文件并进入success回调
              if (res.statusCode === 200) {
                // 临时文件路径
                var filePath = res.tempFilePath
                // 保存到本地
                wx.saveVideoToPhotosAlbum({
                  filePath: filePath,
                  success () {
                    // console.log(res.errMsg)
                    wx.hideLoading();
                  }
                })
              }
            }
          })
        } else if (index == 2 || index == 3 || index == 4) {
          wx.showToast({
            title: '微信暂不支持',
            icon: 'none',
            duration: 5000,
          })
        } else {
          var publisherId = me.data.publisher.id;
          var videoId = me.data.videoId;
          // console.log(publisherId);
          //举报用户
          var user = app.getGlobalUserInfo();
          // 用户未登录，跳转到登录页面，否则跳转到我的主页
          if (user == null || user == "" || user == undefined) {
            wx.redirectTo({
              url: '../userLogin/Login',
            })
          } else {
              wx.navigateTo({
                url: '../report/report?videoId=' + videoId + "&publisherId=" + publisherId,
              })
           }
        }
      }
    })
  },

  // 开启分享至微信好友功能
  onShareAppMessage:function(){
      var me = this;
      var videoInfo = me.data.videoInfo; 
      // 转发的标题和URL以及回调函数
      return{
        title:"短视频",
        path:"../videoInfo/videoInfo?videoInfo=" + JSON.stringify(videoInfo),
        success:function(){
          wx.showToast({
            title: '分享成功!',
          })
        }
      }
  },

  // 分享至朋友圈
  onShareTimeline: function() {
    var me = this;
    var videoInfo = me.data.videoInfo; 
    return {
      title: '短视频',
      path: "../videoInfo/videoInfo?videoInfo=" + JSON.stringify(videoInfo),
    }
},


})
