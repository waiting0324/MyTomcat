package com.waiting;

import java.io.IOException;
import java.io.InputStream;

/**
 * Create By Waiting on 2020/4/3
 */
public class Request {

    // 請求方式 GET/POST
    private String method;

    // 請求路徑 /, /index.html
    private String url;

    // 輸入流，其他屬性是透過輸入流解析出來的
    private InputStream inputStream;

    public Request(InputStream inputStream) throws Exception {
        this.inputStream = inputStream;

        // 從輸入流中獲取請求訊息
        // 由於網路傳輸延遲問題，需等待接收到內容後才能繼續執行
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        // 將請求內容讀入bytes中
        byte[] bytes = new byte[count];
        inputStream.read(bytes);

        String inputStr = new String(bytes);
        // 獲取第一行請求頭
        // GET / HTTP/1.1
        String firstLineStr = inputStr.split("\\n")[0];

        String[] strings = firstLineStr.split(" ");
        this.method = strings[0];
        this.url = strings[1];

        System.out.println("============>method: " + method);
        System.out.println("============>url: " + url);
    }



    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
