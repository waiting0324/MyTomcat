package com.waiting;

/**
 * 提供響應頭訊息
 * Create By Waiting on 2020/4/3
 */
public class HttpProtocalUtil {

    /**
     * 響應碼200的請求頭訊息
     */
    public static String getHttpHeader200(long contentLength) {

        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + contentLength + " \n" +
                "\r\n";
    }

    /**
     * 響應碼404的請求頭訊息(包含內容)
     */
    public static String getHttpHeader404() {

        String str404 = "<h1>404 not found</h1>";

        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type: text/html \n" +
                "Content-Length: " + str404.getBytes().length + " \n" +
                "\r\n" + str404;
    }

}
