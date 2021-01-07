//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    bgmList:[],
    videoInfo:{},
  },

  // 页面加载读取歌曲列表
  onLoad:function(videoInfo){
    // console.log(videoInfo)
    var me = this;
    var serverUrl = app.serverUrl;
    me.setData({
      videoInfo:videoInfo,
    })
    wx.showLoading({
      title: '读取中...',
    })
    wx.request({
      url: serverUrl +'/bgm/list',
      header: {
        'content-type': 'application/json' // 默认值
      },
      method:'POST',
      success (res) {
        wx.hideLoading({
        });
        // console.log(res);
        var length = res.data.map.bgmList.length;
        for(var i = 0; i < length; i++){
          // 加上服务器地址，不然无法渲染
          res.data.map.bgmList[i].path = serverUrl + res.data.map.bgmList[i].path;
        }
        var bgmList = res.data.map.bgmList;
        // console.log(res);
        me.setData({
          bgmList:bgmList,
        })
      }
    })
  },

  //提交信息函数
  bgmSubmit:function(e){
    var me = this;
    //获取本地缓存
    var userInfo = app.getGlobalUserInfo();
    var userId = userInfo.id;
    var bgmId = e.detail.value.bgmId;
    var desc = e.detail.value.description;
    // 强转成浮点型
    var tempDuration = Number(me.data.videoInfo.tempDuration);
    var tempHeight = Number(me.data.videoInfo.tempHeight);
    var tempFilePath = me.data.videoInfo.tempFilePath;
    var thumbTempFilePath = me.data.videoInfo.thumbTempFilePath;
    var tempWidth = Number(me.data.videoInfo.tempWidth);
    wx.showLoading({
      title: '上传中',
    })
    // 上传视频
    wx.uploadFile({
      url: app.serverUrl + '/video/uploadVideo',
      formData:{
        // 后端参数：前端参数
        userId:userId,
        videoDuration:tempDuration,
        bgmId:bgmId,
        videoHeight:tempHeight,
        videoWidth:tempWidth,
        desc:desc,
      },
      filePath: tempFilePath,
      //跟后端对应
      name: 'file',
      header:{
        'content-type':'application/json'
      },
      success(res){
        var data = JSON.parse(res.data);
        // console.log(data);
        var status = data.status;
        if(status == 200){
          wx.hideLoading({
          })
          wx.showToast({
            title: '上传成功',
            icon:'success',
            duration:5000,
          })
          // 返回至个人界面
          wx.navigateTo({
            url:'../mine/mine',
            })
            // 手机端出现封面显示bug，不做此代码，后端补充
          // var videoId = data.map.videoId;
          // // 上传封面
          // wx.uploadFile({
          //   url: app.serverUrl + '/video/uploadCover',
          //   formData:{
          //     // 后端参数：前端参数
          //     userId:userId,
          //     videoId:videoId,
          //   },
          //   filePath: thumbTempFilePath,
          //   //跟后端对应
          //   name: 'file',
          //   header:{
          //     'content-type':'application/json'
          //   },
          //   success(res){
          //     var data = JSON.parse(res.data);
          //     var status = data.status;
          //     if(status == 200){
          //       wx.showToast({
          //         title: '上传成功',
          //         icon:'success',
          //         duration:5000,
          //       })
          //       // 返回至个人界面
          //       wx.navigateTo({
          //         url:'../mine/mine',
          //       })
          //     }else{
          //       wx.showToast({
          //         title:data.message,
          //         icon:'none',
          //         duration:5000,
          //       })
          //     }
          //   }
          // }) 
        }else{
          wx.showToast({
            title:data.message,
            icon:'none',
            duration:5000,
          })
        }
      }
    })   
},


  })
