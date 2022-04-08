package tacos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import tacos.domain.TacoOrder;

import javax.jms.Destination;

@Service
public class JmsOrderMessagingService implements OrderMessagingService {
    private final JmsTemplate jms;
    private final Destination dest;

    @Autowired
    public JmsOrderMessagingService(JmsTemplate jms, @Qualifier("orderQueue") Destination dest) {
        this.jms = jms;
        this.dest = dest;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        jms.convertAndSend(dest, order, message -> {
            // In case, we have WEB and STORE types of shops
            message.setStringProperty("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }
}
