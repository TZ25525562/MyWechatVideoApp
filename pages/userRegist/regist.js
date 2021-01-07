const app = getApp()

Page({
  data:{

  },

  //与后端交互注册
  regist:function(e){
    var inputInfo = e.detail.value;
    var username = inputInfo.username;
    var password = inputInfo.password;
      if(username == "" || password == ""){
        // 弹出信息框
        wx.showToast({
          title: '用户名或密码为空',
          icon: 'none',
          duration: 5000,
        })
      }else{
        var serverurl = app.serverUrl;
        // 显示loading框
        wx.showLoading({
          title:'加载中',
        })
        // 发送请求
        wx.request({
          url: serverurl + '/register',
          method:"POST",
          data:{
              username:inputInfo.username,
              password:inputInfo.password,
          },
          header: {
            'content-type': 'application/json' // 默认值
          },
          success(res){
            var status = res.data.status;
            //相应成功后loading提示框消失
            wx.hideLoading({
              // success: (res) => {},
            })
            //注册信息回显到页面
            if(status == 200){
                wx.showToast({
                  title: '注册成功！',
                  icon: 'none',
                  duration : 5000,
                })
                //app.userInfo = res.data.map.data;
                //修改原有的全局对象为本地缓存
                app.setGlobalUserInfo(res.data.map.data);
            }else{
              wx.showToast({
                title: res.data.message,
                icon: 'none',
                duration : 5000,
              })
            }
          }
        })
      }
  },

  //返回登录界面
  goLogin:function(){
      wx.navigateTo({
        url: '../userLogin/Login',
      })
  }

})