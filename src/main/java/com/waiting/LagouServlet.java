package com.waiting;

/**
 * Create By Waiting on 2020/4/3
 */
public class LagouServlet extends HttpServlet {


    @Override
    public void doGet(Request request, Response response) {

        try {
            Thread.sleep(100000);
            String content = "<h1>LagouServlet get</h1>";
            response.output(HttpProtocalUtil.getHttpHeader200(content.getBytes().length) + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }
}
