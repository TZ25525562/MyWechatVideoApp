// ReferenceError: app is not defined的解决办法
var app = getApp();

Page({
  data: {
    // 分页数据
    page : 1,//当前页数
    totalPage: 1,//总页数
    videoList:[],//视频列表
    //屏幕初始宽度
    screenWidth:350,
    serverUrl:app.serverUrl,
    //热搜词判断词
    isSaveRecord:"",
    // 视频描述信息
    videoDesc:"",
  },

  onLoad: function (params) {
    var me = this;
    var page  = me.data.page;
    // console.log(params);
    //获取系统信息的屏幕宽度
    var screenWidth = wx.getSystemInfoSync.screenWidth;
    //获取传入的搜索词和判断条件
    var isSaveRecord = params.isSaveRecord;
    var videoDesc = params.search;
    //搜索词为空
    if(isSaveRecord == null || isSaveRecord == "" || isSaveRecord == undefined){
      isSaveRecord = 0;
    }
    me.setData({
      screenWidth: screenWidth,
      isSaveRecord:isSaveRecord,
      videoDesc:videoDesc,
    })

    me.showCurrentPageVideo(page,isSaveRecord);

  },

  //将显示当前页视频抽取成函数
  showCurrentPageVideo:function(page){
    var me = this;
    var isSaveRecord = me.data.isSaveRecord;
    var videoDesc = me.data.videoDesc;
    wx.showLoading({
      title: '加载中，请稍后',
    })
    //发送请求
    wx.request({
    url: me.data.serverUrl + '/video/showAll?page=' + page + '&isSaveRecord=' + isSaveRecord,
    method:'POST',
    data:{
      videoDesc:videoDesc,
    },
    success(res){
      wx.hideLoading({
      });
      // 在当前页面隐藏导航条加载动画
      wx.hideNavigationBarLoading();
      // 停止当前页面下拉刷新。
      wx.stopPullDownRefresh();
      var data = res.data.map.data;
      // console.log(res);
      //如果第一次刷新则从头开始
      if(page == 1){
        me.setData({
          videoList:[],
        })
      }
      var currentPage = data.page;
      var totalPage = data.records;
      var newVideoList = me.data.videoList;
      var videoList = data.rows;
      me.setData({
        videoList: newVideoList.concat(videoList),
        totalPage: totalPage,
        page:currentPage,
      })
    }
  })
  },

  // 页面上拉触底事件的处理函数
  onReachBottom:function(){
      var me = this;
      var currentPage = me.data.page;
      var totalPage = me.data.totalPage;
// 判断当前页数和总页数是否相等，相等无需查询
      if(currentPage == totalPage){
        wx.showToast({
          title: '已经到视频列表底部了！',
          icon:'none',
          duration:5000,
        })
        return;
      }

      var page = currentPage + 1;

      //加载下一页内容
      me.showCurrentPageVideo(page,0);

  },

  // 监听用户下拉动作
  onPullDownRefresh:function(){
      wx.showNavigationBarLoading();
      this.showCurrentPageVideo(1,0);
  },

  showVideoInfo:function(e){
      var me = this;
      // 获取视频下标
      var videoList = me.data.videoList;
      var index = e.target.dataset.arrindex;
      // 转成String类型传参
      var videoInfo = JSON.stringify(videoList[index]);
      wx.redirectTo({
        url: '../videoInfo/videoInfo?videoInfo=' + videoInfo,
      })
  }

})
