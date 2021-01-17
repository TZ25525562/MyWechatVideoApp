const app = getApp()


  //上传视频
  function uploadVedio(){
    wx.chooseVideo({
      sourceType: ['album', 'camera'],
      maxDuration: 60,
      camera: 'back',
      compressed: true,
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
        if (tempDuration > 300) {
          wx.showToast({
            title: '视频长度不能超过300秒',
            icon: 'none',
            duration: 5000,
          })
        } else if (tempDuration < 1) {
          wx.showToast({
            title: '视频长度不能小于1秒',
            icon: 'none',
            duration: 5000,
          })
          // 选择Bgm
        } else {
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


module.exports = {
  // 暴露上传视频方法接口
  uploadVedio: uploadVedio
}