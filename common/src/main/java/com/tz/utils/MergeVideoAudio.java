package com.tz.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用ffmpeg整合video 和 bgm
 */
public class MergeVideoAudio {
    //可执行文件位置
    private String ffmpegExe;

    public MergeVideoAudio(String ffmpegExe) {
        this.ffmpegExe = ffmpegExe;
    }


    /**
     * 视频和音频整合
     * @param videoInputPath
     * @param bgmInputPath
     * @param videoOutputPath
     * @param seconds
     */
    public void convert(String videoInputPath ,String bgmInputPath,String videoOutputPath,double seconds) throws Exception {
        //ffmpeg.exe -t 299 -i C:\1.mp4 -i D:\仿抖音小程序\file-dev\bgm\晴天.mp3 -y newVideo.mp4
        List<String> command = new ArrayList();
        //拼成命令
        command.add(ffmpegExe);
        command.add("-t");
        command.add(String.valueOf(seconds));
        command.add("-i");
        command.add(videoInputPath);
        command.add("-i");
        command.add(bgmInputPath);
        command.add("-y");
        command.add(videoOutputPath);
        //启动命令
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
//        处理输入流
        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
        }

        if(errorStream != null){
            errorStream.close();
        }
        if(inputStreamReader != null){
            inputStreamReader.close();
        }
        if(bufferedReader != null){
            bufferedReader.close();
        }
    }

}
