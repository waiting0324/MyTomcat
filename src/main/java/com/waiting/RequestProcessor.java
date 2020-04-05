package com.waiting;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Create By Waiting on 2020/4/4
 */
public class RequestProcessor extends Thread {

    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();

            // 封裝Request對象和Response對象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 在ServletMap中找不到url對應的Servlet，則認定是靜態資源
            if (servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            } else {
                // 動態資源servlet請求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request, response);
            }
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
