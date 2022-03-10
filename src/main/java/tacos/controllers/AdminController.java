package tacos.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.domain.TacoOrder;
import tacos.services.OrderAdminService;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final OrderAdminService adminService;

    @Autowired
    public AdminController(OrderAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/deleteOrders")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAllOrders() {
        adminService.deleteAllOrders();
        return "redirect:/admin";
    }

    @PostAuthorize("hasRole('ADMIN') || (returnObject.user.username == authentication.name)")
    public TacoOrder getOrderById(long id) throws Exception {
        final TacoOrder order = adminService.getOrder(id);
        if (order != null) {
            log.info(order.toString());
            return order;
        } else {
            throw new Exception("Order with id:'" + id + "' not found.");
        }
    }
}
