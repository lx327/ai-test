package com.xuange.ai.aaa.file;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
//文件压缩与文件切割
//xuange
public class FileGetZIPHandle {
    public static void main(String[] args) {
        zipFolder("/home/xuange/音乐/com","/home/xuange/音乐/xx");
    }

    public static void zipFile(String sourceFile, String zipFile) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        FileInputStream fileInputStream = null;
        FileOutputStream outputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            // 创建文件输入流以读取源文件
            fileInputStream = new FileInputStream(sourceFile);
            // 创建文件输出流以写入压缩文件
            outputStream = new FileOutputStream(zipFile);
            // 创建Zip输出流
            zipOutputStream = new ZipOutputStream(outputStream);

            // zip元数据写入
            ZipEntry zipEntry = new ZipEntry(new File(sourceFile).getName());
            zipOutputStream.putNextEntry(zipEntry);

            // 读取源文件并写入压缩文件
            byte[] buffer = new byte[512];
            int length;
            while ((length = fileInputStream.read(buffer)) >= 0) {
                zipOutputStream.write(buffer, 0, length); // 使用zipOutputStream写入数据
            }
            zipOutputStream.closeEntry(); // 关闭当前ZipEntry
        } catch (IOException e) {
            e.printStackTrace(); // 输出异常信息
        } finally {
            // 关闭所有流
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace(); // 输出异常信息
            }
        }

    }

    public static void zipFolder(String sourceFolder, String zipFile) {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            File folder = new File(sourceFolder);
            if (!folder.exists() || !folder.isDirectory()) {
                throw new IllegalArgumentException("Source folder does not exist or is not a directory.");
            }
            zipOfDirectory(folder, folder.getName(), zos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("I/O error: " + e.getMessage(), e);
        }
    }

    public static void zipOfDirectory(File folder, String name, ZipOutputStream zos) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipOfDirectory(file, name + "/" + file.getName(), zos);
                } else {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(name + "/" + file.getName());
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[512];
                        int length;
                        while ((length = fis.read(buffer)) >= 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException("Error zipping file: " + file.getName(), e);
                    }
                }
            }
        }
    }
}


