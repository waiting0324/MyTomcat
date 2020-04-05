package com.waiting;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.SAXParser;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Create By Waiting on 2020/4/3
 */
public class Bootstrap {


    // 默認監聽的端口號
    private int port = 8080;

    /**
     * Minicat初始化
     */
    public void start() throws Exception {

        // 解析web.xml
        loadServlet();

        // 定義一個線程池
        int corePoolSize = 10; int maximumPoolSize = 50; long keepAliveTime = 100L; TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);

        // 設置要監聽的端口
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>> Minicat start on port： " + port);

        while (true) {
            // 開啟端口監聽
            Socket socket = serverSocket.accept();
            // 使用多線程處理請求
            RequestProcessor processor = new RequestProcessor(socket, servletMap);
            threadPoolExecutor.execute(processor);
        }

    }

    // 存放url與Servlet的對應關係
    private Map<String, HttpServlet> servletMap = new HashMap<String, HttpServlet>();

    // 解析web.xml
    private void loadServlet() {

        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");

        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");

            for (Element servletNode : selectNodes) {

                // <servlet-name>lagou</servlet-name>
                Element servletNameElement = (Element) servletNode.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();

                // <servlet-class>server.LagouServlet</servlet-class>
                Element serlvetClassElement = (Element) servletNode.selectSingleNode("servlet-class");
                String servletClass = serlvetClassElement.getStringValue();

                // 根據servletName的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();

                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap(); bootstrap.start();
    }
}
