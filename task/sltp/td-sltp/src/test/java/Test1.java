import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by Ness on 2017/5/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Test1.class)
public class Test1 {

   @Bean
    public CachingConnectionFactory myConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

       connectionFactory.setHost("192.168.1.65");
//        connectionFactory.setVirtualHost("/myHost");
        return connectionFactory;
    }

    //  生成CachingConnectionFactory  也可以使用下面的方式,在application.properties
    //  中定义好属性即可

    //     @Autowired
    //    ConnectionFactory connectionFactory;
    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange("myExchangeDemo", true, false);
    }

    @Bean
    public Queue myQueue() {
        return new Queue("myQueueDemo", true);
    }

    @Bean
    public Binding myExchangeBinding(@Qualifier("myExchange") DirectExchange directExchange,
                                     @Qualifier("myQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with("routeDemo");
    }

    @Bean
    public RabbitTemplate myExchangeTemlate() {
        RabbitTemplate r = new RabbitTemplate(myConnectionFactory());
        r.setExchange("myExchangeDemo");
        r.setRoutingKey("routeDemo");
        String string = "Hello RabbitmQ";
        System.out.println("________________________________");
        r.convertAndSend(string);
        return r;
    }


    /**
     *  发送消息,工业使用需要自己做个性化实现
     */
    @org.junit.Test
    public void sendMessage() {

    }

    /**
     *  接受消息,工业使用时需要在监听类中实现process逻辑
     */
    @RabbitListener(queues = "myQueueDemo")
    public void process(Message message) {
        System.out.println("__________" + message.getBody().toString() + "__________");
        try {
            this.wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
