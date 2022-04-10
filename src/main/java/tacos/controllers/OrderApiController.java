package tacos.controllers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tacos.domain.TacoOrder;
import tacos.repositories.OrderRepository;
import tacos.services.OrderMessagingService;

@RestController
@RequestMapping(path = "/amqp/orders", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class OrderApiController {
    private final OrderRepository repo;
    private final OrderMessagingService messageService;

    @Autowired
    public OrderApiController(OrderRepository repo, OrderMessagingService messageService) {
        this.repo = repo;
        this.messageService = messageService;
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public TacoOrder postOrder(@RequestBody TacoOrder order) {
        messageService.sendOrder(order);
        return repo.save(order);
    }

    @GetMapping
    public void getOrder() {
        final TacoOrder result = messageService.receiveOrder();
        System.out.println(result);
    }

    @RabbitListener(queues = {"${taco.orders.routing-key}"})
    public void getOrderByListener(TacoOrder order) {
        System.out.println(order);
    }
}
