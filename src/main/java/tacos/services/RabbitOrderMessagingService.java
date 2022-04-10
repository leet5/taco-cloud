package tacos.services;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import tacos.domain.TacoOrder;

@Service
public class RabbitOrderMessagingService implements OrderMessagingService {
    private final RabbitTemplate rabbit;

    @Value("${taco.orders.exchange}")
    private String exchange;

    @Value("${taco.orders.routing-key}")
    private String routingKey;

    @Autowired
    public RabbitOrderMessagingService(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        rabbit.convertAndSend(exchange, routingKey, order, message -> {
            final MessageProperties props = message.getMessageProperties();
            props.setHeader("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }

    @Override
    public TacoOrder receiveOrder() {
        return rabbit.receiveAndConvert(routingKey, new ParameterizedTypeReference<TacoOrder>(){});
    }
}
