package tacos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import tacos.domain.TacoOrder;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

@Service
public class JmsOrderMessagingService implements OrderMessagingService {
    private final JmsTemplate jms;
    private final Destination dest;
    private final MessageConverter converter;

    @Autowired
    public JmsOrderMessagingService(JmsTemplate jms, @Qualifier("orderQueue") Destination dest, MessageConverter converter) {
        this.jms = jms;
        this.dest = dest;
        this.converter = converter;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        jms.convertAndSend(dest, order, message -> {
            // In case, we have WEB and STORE types of shops
            message.setStringProperty("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }

    @Override
    public TacoOrder receiveOrder() throws JMSException {
        final Message message = jms.receive(dest);
        assert message != null;
        return (TacoOrder) converter.fromMessage(message);
    }
}
