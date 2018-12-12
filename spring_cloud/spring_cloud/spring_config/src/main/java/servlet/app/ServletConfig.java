package servlet.app;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebApplicationContext webApplicationContext =
                WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());

        User bean = webApplicationContext.getBean(User.class);
        sce.getServletContext().setAttribute("user", bean);
        System.out.println("init....");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
