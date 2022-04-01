package tacos.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.domain.TacoOrder;
import tacos.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/orders", produces = "application/json")
public class OrderRestController {
    private final OrderRepository orderRepo;

    @Autowired
    public OrderRestController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping(params = "recent")
    public List<TacoOrder> getRecentOrders() {
        final PageRequest page = PageRequest.of(0, 10, Sort.by("id").ascending());
        return orderRepo.findAll(page).getContent();
    }

    @PutMapping(path = "/{orderId}", consumes = "application/json")
    public TacoOrder putOrder(@PathVariable Long orderId, @RequestBody TacoOrder order) {
        order.setId(orderId);
        return orderRepo.save(order);
    }

    @PatchMapping(path="/{orderId}", consumes = "application/json")
    public ResponseEntity<TacoOrder> patchOrder(@PathVariable Long orderId, @RequestBody TacoOrder patch) {
        final Optional<TacoOrder> optOrder = orderRepo.findById(orderId);
        if (optOrder.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        final TacoOrder order = optOrder.get();
        if (patch.getCcNumber() != null) order.setCcNumber(patch.getCcNumber());
        if (patch.getCcExpiration() != null) order.setCcExpiration(patch.getCcExpiration());
        if (patch.getCcCVV() != null) order.setCcCVV(patch.getCcCVV());

        if (!patch.getTacos().isEmpty()) order.setTacos(patch.getTacos());
        return new ResponseEntity<>(orderRepo.save(order), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{orderId}")
    public ResponseEntity<TacoOrder> deleteOrder(@PathVariable Long orderId) {
        try {
            orderRepo.deleteById(orderId);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
