package tacos.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.domain.User;
import tacos.repositories.UserRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.data.rest.base-path}")
    private String restPath;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            final User user = userRepo.findByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, restPath + "/api-secured/ingredients/**").hasAuthority("SCOPE_writeIngredients")
                .antMatchers(HttpMethod.DELETE, restPath + "/api-secured/ingredients/**").hasAuthority("SCOPE_deleteIngredients")

                .antMatchers(restPath + "/orders/**").hasAuthority("SCOPE_orders")
                .antMatchers(restPath + "/tacos/**").hasAuthority("SCOPE_tacos")
                .antMatchers(restPath + "/users/**").hasAuthority("SCOPE_users")
                .antMatchers(restPath + "/ingredients/**").hasAuthority("SCOPE_ingredients")

                .antMatchers("/design/**", "/orders/**", "/api/**").hasRole("USER")
                .antMatchers("/", "/**").permitAll()
            .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/design", true)
            .and()
                .csrf().ignoringAntMatchers("/**")
            .and()
                .headers().frameOptions().sameOrigin()
            .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
