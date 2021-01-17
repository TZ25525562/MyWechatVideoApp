//app.js
App({
  // 服务器地址
  serverUrl: "http://192.168.31.134:8088",
  //全局用户信息
  userInfo: null,
  //将数据存储在本地缓存中指定的 key 中
  setGlobalUserInfo:function(user){
    wx.setStorageSync('userInfo', user);
  },

  //从本地缓存中异步获取指定 key 的内容
  getGlobalUserInfo:function(){
    return wx.getStorageSync('userInfo');
  },

  reportReasonArray:[
    "色情低俗",
    "政治敏感",
    "涉嫌诈骗",
    "辱骂谩骂",
    "广告垃圾",
    "诱导分享",
    "引人不适",
    "过于暴力",
    "违法违纪",
    "其他原因",
  ],
  
})