package com.xuange.ai.aaa.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//xuange
@RestController
@CrossOrigin
@Component
@RequestMapping("video")
//视频压缩与切割，使用了ffmpge
public class VideoController {

    String SourcePath="";
    String OutputPath ="";
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        //TODO 接收视频
        String originalFilename = file.getOriginalFilename();
        String Sourcename=SourcePath+originalFilename;

        //TODO 压缩文件


        //TODO 分片
        return "File uploaded successfully";
    }
    public static void compressVideo(String sourcePath, String outputPath) {
        // 创建一个进程构建器，用于执行外部命令（这里是 ffmpeg）
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",              // 调用 ffmpeg 命令
                "-i", sourcePath,     // 输入文件路径
                "-codec:v", "libx264", // 使用 libx264 编码器进行视频压缩
                "-preset", "slow",    // 设置压缩预设为 'slow'，以获得更好的压缩质量
                "-crf", "23",         // 设置 CRF（常量速率因子），数值越小质量越高，范围为 0-51
                outputPath            // 输出文件路径
        );

        try {
            // 启动进程
            Process process = processBuilder.start();

            // 创建 BufferedReader 以读取 ffmpeg 的错误输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;

            // 逐行读取 ffmpeg 的错误输出并打印到控制台
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程完成
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            // 捕获并打印异常信息
            e.printStackTrace();
        }
    }
    public static void  sliceVideo(String sourcePath, String outputPath, int startTime, int duration) {
        // 创建一个进程构建器，用于执行外部命令（这里是 ffmpeg）
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",               // 调用 ffmpeg 命令
                "-i", sourcePath,      // 输入文件路径
                "-ss", String.valueOf(startTime), // 设置开始时间，单位为秒
                "-t", String.valueOf(duration),    // 设置剪切的持续时间，单位为秒
                "-c", "copy",          // 使用 'copy' 编码器，直接复制音视频流，不进行重新编码
                outputPath             // 输出文件路径
        );

        try {
            // 启动进程
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;

            // 逐行读取 ffmpeg 的错误输出并打印到控制台
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 等待进程完成
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            // 捕获并打印异常信息
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        compressVideo("/home/xuange/视频/录屏/xxdojadjka.mp4","/home/xuange/视频/录屏/");
        sliceVideo("/home/xuange/视频/录屏/xxdojadjka.mp4","/home/xuange/视频/录屏/",0,3);
    }



}
//const formData = new FormData();
//formData.append('file', fileInput.files[0]);
//
//fetch('/upload', {
//    method: 'POST',
//            body: formData
//})
//        .then(response => response.json())
//        .then(data => console.log(data))
//        .catch(error => console.error('Error:', error));