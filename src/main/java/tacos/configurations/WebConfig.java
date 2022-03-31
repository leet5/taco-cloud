package tacos.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tacos.domain.*;
import tacos.repositories.IngredientRepository;
import tacos.repositories.OrderRepository;
import tacos.repositories.TacoRepository;
import tacos.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner dataLoader(PasswordEncoder encoder, UserRepository userRepo, OrderRepository orderRepo, IngredientRepository ingredientRepo, TacoRepository tacoRepo) {
        return args -> {
            // Ingredients
            final Ingredient i_flto = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
            final Ingredient i_coto = new Ingredient("COTO", "Corn Tortilla",  Ingredient.Type.WRAP);
            final Ingredient i_grbf = new Ingredient("GRBF", "Ground Beef",    Ingredient.Type.PROTEIN);
            final Ingredient i_carn = new Ingredient("CARN", "Carnitas",       Ingredient.Type.PROTEIN);
            final Ingredient i_tmto = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
            final Ingredient i_letc = new Ingredient("LETC", "Lettuce",        Ingredient.Type.VEGGIES);
            final Ingredient i_ched = new Ingredient("CHED", "Cheddar",        Ingredient.Type.CHEESE);
            final Ingredient i_jack = new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
            final Ingredient i_slsa = new Ingredient("SLSA", "Salsa",          Ingredient.Type.SAUCE);
            final Ingredient i_srcr = new Ingredient("SRCR", "Sour Cream",     Ingredient.Type.SAUCE);
            ingredientRepo.saveAll(List.of(i_flto, i_coto, i_grbf, i_carn, i_tmto, i_letc, i_ched, i_jack, i_slsa, i_srcr));

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

            // Tacos
            final Taco taco1 = new Taco();
            taco1.setName("Carnivore");
            taco1.setIngredients(Arrays.asList(i_flto, i_grbf, i_carn, i_srcr, i_slsa, i_ched));

            final Taco taco2 = new Taco();
            taco2.setName("Bovine Bounty");
            taco2.setIngredients(Arrays.asList(i_coto, i_grbf, i_ched, i_jack, i_srcr));

            final Taco taco3 = new Taco();
            taco3.setName("Veg-Out");
            taco3.setIngredients(Arrays.asList(i_flto, i_coto, i_tmto, i_letc, i_slsa));

            tacoRepo.saveAll(List.of(taco1, taco2, taco3));
        };
    }
}
