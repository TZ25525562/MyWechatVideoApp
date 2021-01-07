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
  }
  
})