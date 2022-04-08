package tacos.services;

import tacos.domain.TacoOrder;

import javax.jms.JMSException;

public interface OrderMessagingService {
    void sendOrder(TacoOrder order);
    TacoOrder receiveOrder() throws JMSException;
}
