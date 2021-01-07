// ReferenceError: app is not defined的解决办法
var app = getApp();

Page({
  data: {
    showStyle:"cover",
  },

  search:function(){
    wx.navigateTo({
      url: '../searchVideo/searchVideo',
    })
  }

})
