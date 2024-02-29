package com.cppzeal.rdavatar.utils;

import android.content.Context;
import android.util.Log;

import com.cppzeal.rdavatar.data.Mp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {

    private static final String TAG = "FileDownloader";

    public static void downloadFile(Context context) {
        Log.d(TAG, "try");
        // 文件的 URL
        String FILE_URL = Mp.getDownloadUrl(context);
        try {
            URL url = new URL(FILE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            Log.d(TAG, connection.toString());

            // 获取文件名
            String fileName = getFileNameFromUrl(connection);
            // 创建输入流
            InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

            File fileDir = FileUtil.getAvatarDir(context);
            // 创建输出流
            FileOutputStream outputStream = new FileOutputStream(new File(fileDir, fileName));

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.d(TAG, "Downloaded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            e.printStackTrace(); // 输出异常堆栈信息
        }
    }
    private static String getFileNameFromUrl(HttpURLConnection connection) {
        String fileName = null;
        String contentDisposition = connection.getHeaderField("Content-Disposition");
        if (contentDisposition != null && contentDisposition.indexOf("filename=") != -1) {
            int index = contentDisposition.indexOf("filename=");
            fileName = contentDisposition.substring(index + 9);
            // Remove quotes if any
            if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
                fileName = fileName.substring(1, fileName.length() - 1);
            }
        }
        if (fileName == null || fileName.isEmpty()) {
            fileName = "file.txt"; // 如果未找到文件名，则使用默认的文件名
        }
        return fileName;
    }
}
