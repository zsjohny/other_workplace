//package project.generator;
//
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.servlet.FilterHolder;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.springframework.web.context.ContextLoaderListener;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.filter.DelegatingFilterProxy;
//import org.springframework.web.servlet.DispatcherServlet;
//
//import javax.servlet.DispatcherType;
//import java.io.IOException;
//import java.util.EnumSet;
//
//public class TestA {
//    private static WebApplicationContext getWebApplicationContext() {
//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.setConfigLocation("project.config.web");
//        return context;
//    }
//
//
//    public static void main(String[] args) throws Exception {
//
//
//        Server server = new Server(8080);
//
//
//        server.setHandler(getServletContextHandler());
//
//        server.start();
//        System.out.println("____ok_______");
//        server.join();
//    }
//
//
//    private static ServletContextHandler getServletContextHandler() throws IOException {
//        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS); // SESSIONS requerido para JSP
//        contextHandler.setErrorHandler(null);
////        contextHandler.setResourceBase(new ClassPathResource("webapp").getURI().toString());
//        contextHandler.setResourceBase("E:\\jiuy-biz\\jiuy-supplier-admin\\src\\main\\webapp");
//        contextHandler.setContextPath("/");
//
//        // Spring
//        WebApplicationContext webAppContext = getWebApplicationContext();
//        DispatcherServlet dispatcherServlet = new DispatcherServlet(webAppContext);
////        dispatcherServlet
//        ServletHolder springServletHolder = new ServletHolder("default", dispatcherServlet);
//        contextHandler.addServlet(springServletHolder, "/*");
//
//
//        FilterHolder filterHolder = new ServletHandler().addFilterWithMapping(DelegatingFilterProxy.class,
//                "/*", EnumSet.of(DispatcherType.REQUEST));
//        filterHolder.setInitParameter("targetFilterLifecycle", "true");
//        filterHolder.setName("shiroFilter");
//        contextHandler.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));
//
//        contextHandler.addEventListener(new ContextLoaderListener(webAppContext));
//
//        return contextHandler;
//    }
//}
