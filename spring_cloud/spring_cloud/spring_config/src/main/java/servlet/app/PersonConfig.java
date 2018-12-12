package servlet.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonConfig {

    @Bean
    public User createUser() {
        User user = new User();
        user.setName("tom");

        return user;
    }

}
