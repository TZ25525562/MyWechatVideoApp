// ReferenceError: app is not defined的解决办法
var app = getApp();

Page({
  data: {
    reasonType: "请选择原因",
    reportReasonArray: app.reportReasonArray,
    publisherId: "",
    videoId: "",
  },

  // 选择提交原因
  selectReason: function (e) {
    var me = this
    var index = e.detail.value;
    var reasonType = me.data.reportReasonArray[index];
    me.setData({
      reasonType: reasonType,
    })
  },

  // 获取上一界面的参数
  onLoad: function (params) {
    // console.log(params);
    var me = this;
    var publisherId = params.publisherId;
    var videoId = params.videoId;
    me.setData({
      publisherId: publisherId,
      videoId: videoId,
    })
  },

  // 提交按钮
  formSubmit: function (e) {
    // console.log(e);
    var user = app.getGlobalUserInfo();
    var me = this;
    var index = e.detail.value.reason;
    var content = e.detail.value.content;
    var reasonType = me.data.reportReasonArray[index];
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '上传中...',
    })
    // 没有选择举报理由，无法提交
    if (index == null || index == "" || index == undefined) {
      wx.showToast({
        title: '请选择举报理由',
        icon: 'none',
        duration: 5000,
      })
      return;
    }
    wx.request({
      url: serverUrl + '/user/reportUser',
      method: 'POST',
      // UsersReport中的数据
      data: {
        dealUserId: me.data.publisherId,
        dealVideoId: me.data.videoId,
        title: reasonType,
        content: content,
        userid: user.id,
      },
      header: {
        'content-type': 'application/json',// 默认值
        'userId': user.id,//通行拦截器
      },
      success: function (res) {
        wx.hideLoading();
        // console.log(res);
        wx.showToast({
          title: res.data.map.response,
          icon: 'none',
          duration: 5000,
          // 成功后返回上一界面
          success:function(){
            wx.navigateBack();
          }
        })
      }
    })
  },

})
