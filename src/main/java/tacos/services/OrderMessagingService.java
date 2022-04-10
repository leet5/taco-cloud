package tacos.services;

import tacos.domain.TacoOrder;

public interface OrderMessagingService {
    void sendOrder(TacoOrder order);
    TacoOrder receiveOrder();
}
