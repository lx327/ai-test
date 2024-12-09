package com.xuange.ai.aaa.photo;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;

public class photoGetFTP {
    public static void main(String[] args) {
        FTPClient ftpClient = new FTPClient();
        String server = "127.0.0.1";
        int port = 21;
        String user = "xuange";
        String password = "@123852Uxuan";

        try {
            // 连接到 FTP 服务器
            ftpClient.connect(server, port);
            ftpClient.login(user, password);

            // 设置为二进制传输模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 下载文件
            String remoteFile = "/hone/vuange/test/";
            String localFile = "/home/xuange/tes";
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                boolean success = ftpClient.retrieveFile(remoteFile, fos);
                if (success) {
                    System.out.println("File downloaded successfully!");
                } else {
                    System.out.println("Failed to download file.");
                }
            }

            // 登出并断开连接
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
