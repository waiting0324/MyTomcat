package com.waiting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Create By Waiting on 2020/4/3
 */
public class StaticResourceUtil {

    /**
     * 取得靜態資源文件的絕對路徑
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path) {
        // '/'表示根目錄，是絕對路徑
        String absolutePath = StaticResourceUtil.class.getResource("/").getPath();
        return absolutePath.replaceAll("\\\\", "/") + path;
    }

    /**
     * 讀取靜態資源文件輸入流，再通過輸出流輸出
     * @param inputStream 靜態文件的輸入流
     * @param outputStream Http響應的輸出流
     */
    public static void outputStaticResource(InputStream inputStream, OutputStream outputStream) throws IOException {

        // 由於網路傳輸延遲問題，需等待接收到內容後才能繼續執行
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        // 靜態資源總長度
        int resourceSize = count;
        // 設定HTTP響應頭
        outputStream.write(HttpProtocalUtil.getHttpHeader200(resourceSize).getBytes());

        // 讀取內容輸出
        // 已讀取內容的長度
        long written = 0;
        // 讀取緩衝區設置
        int byteSize = 1024;
        byte[] bytes = new byte[byteSize];

        // 當已讀取長度小於資源長度則繼續執行
        while (written < resourceSize) {

            // 如果讀取大小不足1024，則按照真實長度處理
            if (written + byteSize > resourceSize) {
                byteSize = (int) (resourceSize - written);
                bytes = new byte[byteSize];
            }

            // 從輸入流讀取到緩衝區
            inputStream.read(bytes);
            // 從緩衝區輸出到輸入流
            outputStream.write(bytes);

            outputStream.flush();
            // 更新已讀取長度
            written += byteSize;
        }
    }

}
