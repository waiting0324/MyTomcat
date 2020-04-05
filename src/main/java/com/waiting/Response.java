package com.waiting;

import java.io.*;

/**
 * Create By Waiting on 2020/4/3
 */
public class Response {

    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void output(String content) throws Exception {
        outputStream.write(content.getBytes());
    }

    public void outputHtml(String path) throws Exception {

        // 獲取靜態資源文件的絕對路徑
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);

        File file = new File(absoluteResourcePath);

        // 如果靜態資源存在
        if (file.exists() && file.isFile()) {
            // 讀取靜態資源文件，並輸出靜態資源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file), outputStream);
        } else {
            // 輸出404
            output(HttpProtocalUtil.getHttpHeader404());
        }

    }
}
