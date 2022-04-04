package tacos.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.domain.User;
import tacos.repositories.UserRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
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
                .antMatchers(HttpMethod.POST, "/api-secured/ingredients/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api-secured/ingredients/**").hasRole("ADMIN")

                .antMatchers("/design/**", "/orders/**", "/api/**").hasRole("USER")
                .antMatchers("/", "/**").permitAll()
            .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/design", true)
            .and()
                .csrf().ignoringAntMatchers("/**")
            .and()
                .headers().frameOptions().sameOrigin();
    }
}
