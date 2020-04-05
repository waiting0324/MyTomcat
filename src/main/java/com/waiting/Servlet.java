package com.waiting;

/**
 * Create By Waiting on 2020/4/3
 */
public interface Servlet {

    void init() throws Exception;

    void destroy() throws Exception;

    void service(Request request, Response response) throws Exception;

}
