package com.e_commerce.miscroservice;

import com.e_commerce.miscroservice.commons.annotation.colligate.init.Start;
import com.e_commerce.miscroservice.commons.entity.service.UserAuth;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

import static org.powermock.api.mockito.PowerMockito.mock;

@Start
public class UserStartMain implements CommandLineRunner {
    @Autowired
    private ApplicationContext applicationContext;


    public static void main(String[] args) {
        ApplicationContextUtil.run(UserStartMain.class, Boolean.FALSE, args);
        AuthorizeRpcService bean = (AuthorizeRpcService) ApplicationContextUtil.getBean(AuthorizeRpcService.class.getCanonicalName());

        System.out.println(bean.findUserByName("admin"));
    }


    @Override
    public void run(String... args) throws Exception {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        defaultListableBeanFactory.removeBeanDefinition(AuthorizeRpcService.class.getCanonicalName());

        AuthorizeRpcService mock = mock(AuthorizeRpcService.class, invocation ->
                {
//                    Object[] arguments = invocation.getArguments();
//                    System.out.println(Arrays.asList(arguments));
//                    String name = invocation.getMethod().getReturnType().getCanonicalName();
//                    System.out.println(name);
//                    Class.forName(name);
                    return new UserAuth();

                }
        );

        defaultListableBeanFactory.registerSingleton(AuthorizeRpcService.class.getCanonicalName(), mock);


    }
}
