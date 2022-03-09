package tacos.security;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.domain.Address;
import tacos.domain.User;

import javax.validation.Valid;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String fullname;
    private String phone;
    @Valid
    private Address address;

    public User toUser(PasswordEncoder encoder) {
        return new User(username, encoder.encode(password), fullname, phone, address);
    }
}
