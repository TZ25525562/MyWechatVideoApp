const app = getApp()

Page({
  data:{

  },

  logout:function(){
    //获取用户信息
    var user = app.userInfo;
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
            App.userInfo = null;
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
      var user = app.userInfo;
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
                var status = res.data.status;
                wx.hideLoading({
                })
                if(status == 200){
                  wx.showToast({
                    title: '上传成功',
                    icon: 'success',
                    duration: 5000
                  })
                }else{
                  wx.showToast({
                    title: res.data.message,
                    icon: 'none',
                    duration: 5000
                  })
                }
              }
            })

          }
        })


    }
  

})