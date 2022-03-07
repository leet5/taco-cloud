package tacos.repositories;

import org.springframework.data.repository.CrudRepository;
import tacos.domain.TacoOrder;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, String> {
    List<TacoOrder> findByPlacedAtBetween(Date start, Date end);
}
