package tacos.repositories.implementation;

import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tacos.domain.IngredientRef;
import tacos.domain.Taco;
import tacos.domain.TacoOrder;
import tacos.repositories.OrderRepository;

import java.sql.Types;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final JdbcOperations jdbcOperations;

    @Autowired
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder order) {
        final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                    "INSERT INTO Taco_Order (delivery_name, delivery_street, delivery_city, delivery_state, delivery_zip," +
                    "cc_number, cc_expiration, cc_cvv, placed_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);

        order.setPlacedAt(new Date());
        final PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                List.of(order.getDeliveryName(), order.getDeliveryStreet(), order.getDeliveryCity(), order.getDeliveryState(),
                        order.getDeliveryZip(), order.getCcNumber(), order.getCcExpiration(), order.getCcCVV(), order.getPlacedAt())
        );

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        final long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        final List<Taco> tacos = order.getTacos();
        for (int i = 0; i < tacos.size(); i++) {
            saveTaco(orderId, i, tacos.get(i));
        }

        return order;
    }

    private long saveTaco(Long orderId, int orderKey, Taco taco) {
        taco.setCreatedAt(new Date());
        final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "INSERT INTO Taco(name, taco_order, taco_order_key, created_at) " +
                "values (?, ?, ?, ?)",
                Types.VARCHAR, Type.LONG, Type.LONG, Types.TIMESTAMP);
        pscf.setReturnGeneratedKeys(true);

        final PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                List.of(taco.getName(), orderId, orderKey, taco.getCreatedAt())
        );

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients());

        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {
        int key = 0;
        for (IngredientRef ingredientRef : ingredientRefs) {
            jdbcOperations.update(
                    "INSERT INTO Ingredient_Ref (ingredient, taco, taco_key)" +
                    "VALUES (?, ?, ?)",
                    ingredientRef.getIngredient(), tacoId, key++);
        }
    }
}
