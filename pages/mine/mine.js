const app = getApp()

Page({
  data:{
      faceUrl:'../resources/images/face.jpg',
      fanscounts:null,
      followcounts:null,
      receivelikecounts:null,
      nickname:null,
  },

  logout:function(){
    //获取用户信息
    //var user = app.userInfo;
    //获取本地缓存
    var user = app.getGlobalUserInfo();
    var serverurl = app.serverUrl;
      // 显示loading框
    wx.showLoading({
      title:'加载中',
    })
    // 发送请求
    wx.request({
      url: serverurl + '/logout?userId=' + user.id,
      method:"POST",
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res){
        //  console.log(res);
        var status = res.data.status;
         //相应成功后loading提示框消失
         wx.hideLoading({
           // success: (res) => {},
         })
        //回显到登录页面
        if(status == 200){
            wx.showToast({
            title: '注销成功！',
            icon: 'success',
            duration : 5000,
          })
            wx.navigateTo({
              url: '../userLogin/Login',
            })
            //清空个人信息
            //App.userInfo = null;
            //从本地缓存中移除指定 key
            wx.removeStorageSync('userInfo');
        }else{
          wx.showToast({
            title: res.data.message,
            icon: 'none',
            duration : 5000,
            })
          }
        }
      })
    },

    chooseImage:function(){
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
          success (res) {
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
              success(res){
                //格式化为Json数据
                var data = JSON.parse(res.data);
                // console.log(data);
                var status = data.status;
                wx.hideLoading({
                })
                if(status == 200){
                  wx.showToast({
                    title: '上传成功',
                    icon: 'success',
                    duration: 5000
                  })
                  var imageUrl = serverurl + data.map.data;
                  me.setData({
                    faceUrl:imageUrl,
                  })
                  //console.log(imageUrl);
                }else{
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
    onLoad:function(){
      //var user = app.userInfo;
      //获取本地缓存
      var user = app.getGlobalUserInfo();
      var serverurl = app.serverUrl;
      var me = this;
      // 显示loading框
      wx.showLoading({
        title:'加载中',
      })
      // 发送请求
      wx.request({
        url: serverurl + '/user/query?userId=' + user.id,
        method:"POST",
        header: {
          'content-type': 'application/json' // 默认值
        },
        success(res){
          //console.log(res);
          var status = res.data.status;
          //相应成功后loading提示框消失
          wx.hideLoading({
          })
          //回显到个人信息页面
          if(status == 200){
            var data = res.data.map.data;
            var imageUrl = data.faceImage;
            var faceUrl = "../resources/images/face.jpg";
            if(imageUrl != null && imageUrl != '' && 
            imageUrl != undefined){
              faceUrl = serverurl + imageUrl;
            }
            me.setData({
              faceUrl:faceUrl,
              fanscounts:data.fansCounts,
              followcounts:data.followCounts,
              receivelikecounts:data.receiveLikeCounts,
              nickname:data.nickname,
            })
            }
          }
        })
      },

      //上传视频
      uploadVedio:function(){
          wx.chooseVideo({
            sourceType: ['album','camera'],
            maxDuration: 60,
            camera: 'back',
            compressed:true,
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
              if(tempDuration > 300){
                wx.showToast({
                  title: '视频长度不能超过300秒',
                  icon:'none',
                  duration:5000,
                })
              }else if(tempDuration < 1){
                wx.showToast({
                  title: '视频长度不能小于1秒',
                  icon:'none',
                  duration:5000,
                })
                // 选择Bgm
              }else{
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
      }
})