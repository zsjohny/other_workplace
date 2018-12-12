import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Ness on 2017/5/18.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test.class)
public class Test {


    private String exchange = "hello";
    private final String queue = "1111";
    private String routingKey = "22455";

    @Bean
    private CachingConnectionFactory createConnect() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("192.168.1.65");
        return factory;

    }

    @Bean
    public DirectExchange createExchange() {
        return new DirectExchange(exchange, true, false);
    }


    @Bean
    public Queue createQueue() {
        return new Queue(queue);
    }

    @Bean
    public Binding createBindIng(DirectExchange directExchange, Queue queue) {

        return BindingBuilder.bind(queue).to(directExchange).with(routingKey);

    }


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Binding binding;

    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private Queue queues;

    @Bean
    public RabbitTemplate createTemp(CachingConnectionFactory cachingConnectionFactorys) {

        RabbitTemplate template = new RabbitTemplate();
        template.setConnectionFactory(cachingConnectionFactorys);
        template.setExchange(exchange);
        template.setQueue(queue);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setRoutingKey(routingKey);
        template.afterPropertiesSet();
        return template;
    }


//    @RabbitListener(queues = queue, containerFactory = "cachingConnectionFactory",bindings = )
    private class handler {
        @RabbitHandler
        public void process(Message message) {

            System.out.println("111111111111111111");
            System.out.println(message.getBody().toString());
        }
    }


    @org.junit.Test
    public void test1() throws InterruptedException {
        rabbitTemplate.convertAndSend("12345");
        Thread.sleep(40000);
    }

}
