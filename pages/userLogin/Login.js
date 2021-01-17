const app = getApp()

Page({
  data: {

  },

  //用来接收videoInfo 的realUrl
  onLoad: function (params) {
    var me = this;
    var redircetUrl = params.realUrl;
    // 使用正则表达式来替换
    if (redircetUrl != null && redircetUrl != "" && redircetUrl != undefined) {
      redircetUrl = redircetUrl.replace(/#/g, "?");
      redircetUrl = redircetUrl.replace(/@/g, "=");
    }
    // debugger;
    //添加到me的全局变量中
    me.redircetUrl = redircetUrl;
  },

  // 与后端交互登录
  login: function (e) {
    var me = this;
    var inputInfo = e.detail.value;
    var username = inputInfo.username;
    var password = inputInfo.password;
    if (username == "" || password == "") {
      // 弹出信息框
      wx.showToast({
        title: '用户名或密码为空',
        icon: 'none',
        duration: 5000,
      })
    } else {
      var serverurl = app.serverUrl;
      // 显示loading框
      wx.showLoading({
        title: '加载中',
      })
      // 发送请求
      wx.request({
        url: serverurl + '/login',
        method: "POST",
        data: {
          username: inputInfo.username,
          password: inputInfo.password,
        },
        header: {
          'content-type': 'application/json' // 默认值
        },
        success(res) {
          // console.log(res);
          var status = res.data.status;
          //相应成功后loading提示框消失
          wx.hideLoading({
            // success: (res) => {},
          })
          //回显到个人信息页面
          if (status == 200) {
            wx.showToast({
              title: '登录成功！',
              icon: 'success',
              duration: 5000,
            })
            //app.userInfo = res.data.map.data;
            //修改原有的全局对象为本地缓存
            app.setGlobalUserInfo(res.data.map.data);
            //从videoInfo来的回显到原来界面，否则跳转到本人界面
            var redircetUrl = me.redircetUrl;
            if (redircetUrl != null && redircetUrl != "" && redircetUrl != undefined) {
              wx.redirectTo({
                url: redircetUrl,
              })
            } else {
              wx.navigateTo({
                url: '../mine/mine',
              })
            }
          } else {
            wx.showToast({
              title: res.data.message,
              icon: 'none',
              duration: 5000,
            })
          }
        }
      })
    }
  },

  // 前往注册界面
  goRegist: function () {
    wx.navigateTo({
      url: '../userRegist/regist',
    })
  }

})