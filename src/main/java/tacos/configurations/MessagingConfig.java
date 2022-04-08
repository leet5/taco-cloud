package tacos.configurations;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import tacos.domain.TacoOrder;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessagingConfig {

    @Bean
    public Destination orderQueue() {
        return new ActiveMQQueue("tacocloud.order.queue");
    }

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        final MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("_typeId");

        final Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("order", TacoOrder.class);
        messageConverter.setTypeIdMappings(typeIdMappings);

        return messageConverter;
    }

}
