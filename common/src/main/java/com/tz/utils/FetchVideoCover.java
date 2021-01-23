package com.tz.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FetchVideoCover {
    //可执行文件位置
    private String ffmpegExe;

    public FetchVideoCover(String ffmpegExe) {
        this.ffmpegExe = ffmpegExe;
    }


    /**
     * 选取视频第一秒作为封面
     * @param videoInputPath
     * @param videoOutputPath
     */
    public void getCover(String videoInputPath ,String videoOutputPath) throws Exception {
        //ffmpeg.exe -ss 00:00:01 -i 1.mp4 -y -vframes 1 out.jpg
        List<String> command = new ArrayList();
        //拼成命令
        command.add(ffmpegExe);
        command.add("-ss");
        command.add("00:00:01");
        command.add("-i");
        command.add(videoInputPath);
        command.add("-y");
        command.add("-vframes");
        command.add("1");
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
