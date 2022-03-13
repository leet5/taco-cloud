package tacos.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tacos.domain.Address;
import tacos.domain.Ingredient;
import tacos.domain.TacoOrder;
import tacos.domain.User;
import tacos.repositories.IngredientRepository;
import tacos.repositories.OrderRepository;
import tacos.repositories.UserRepository;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    public CommandLineRunner dataLoader(PasswordEncoder encoder, UserRepository userRepo, OrderRepository orderRepo, IngredientRepository ingredientRepo) {
        return args -> {
            // Cleanup
            orderRepo.deleteAll();
            userRepo.deleteAll();

            // Ingredients
            ingredientRepo.save(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
            ingredientRepo.save(new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
            ingredientRepo.save(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
            ingredientRepo.save(new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
            ingredientRepo.save(new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
            ingredientRepo.save(new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
            ingredientRepo.save(new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE));
            ingredientRepo.save(new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE));
            ingredientRepo.save(new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
            ingredientRepo.save(new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));

            // Users
            final Address address = new Address("Home", "Street", "City", "MW", "600000");
            final User user = new User("admin", encoder.encode("admin"), "admin", "+79999999999", address);
            userRepo.save(user);

            // Orders
            for (int i = 0; i < 20; i++) {
                final TacoOrder order = new TacoOrder();
                order.setCcNumber("0000000000000000");
                order.setCcExpiration("01/20");
                order.setCcCVV(100 + i + "");
                order.setUser(user);

                orderRepo.save(order);
            }
        };
    }
}
