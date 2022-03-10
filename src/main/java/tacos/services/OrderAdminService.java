package tacos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tacos.domain.TacoOrder;
import tacos.repositories.OrderRepository;

@Service
public class OrderAdminService {
    private final OrderRepository orderRepo;

    @Autowired
    public OrderAdminService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public void deleteAllOrders() {
        orderRepo.deleteAll();
    }

    public TacoOrder getOrder(long id) {
        return orderRepo.findById(id).orElse(null);
    }
}
