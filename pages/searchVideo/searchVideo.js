// ReferenceError: app is not defined的解决办法
const app = getApp();
var WxSearch = require('../../wxSearch-master/wxSearch/wxSearch');
Page({
  data: {
  
  },


  //初始化
  onLoad: function () {
    var that = this
    var serverUrl = app.serverUrl;

    //获取热搜词
    wx.request({
      url: serverUrl + '/video/hot',
      method:"POST",
      success:function(res){
        // console.log(res);
        var hotWords = res.data.map.data;
        //第一个为热搜词，第二个为热搜匹配词
        WxSearch.init(that,43,hotWords);
        WxSearch.initMindKeys(hotWords);
      }
    })
},

// 搜索的回调函数
wxSearchFn: function(e){
   var value =  e.detail.value.value;
   wx.navigateTo({
     url: '../index/index?isSaveRecord=1&search=' + value,
   })
  
},
wxSearchInput: function(e){
  var that = this
  WxSearch.wxSearchInput(e,that);
},
wxSerchFocus: function(e){
  var that = this
  WxSearch.wxSearchFocus(e,that);
},
wxSearchBlur: function(e){
  var that = this
  WxSearch.wxSearchBlur(e,that);
},
wxSearchKeyTap:function(e){
  var that = this
  WxSearch.wxSearchKeyTap(e,that);
},
wxSearchDeleteKey: function(e){
  var that = this
  WxSearch.wxSearchDeleteKey(e,that);
},
wxSearchDeleteAll: function(e){
  var that = this;
  WxSearch.wxSearchDeleteAll(that);
},
wxSearchTap: function(e){
  var that = this
  WxSearch.wxSearchHiddenPancel(that);
}

})


