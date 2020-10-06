package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.RegisterRequest;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.Address;
import hu.progmasters.gmistore.model.Cart;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.ConfirmationTokenRepository;
import hu.progmasters.gmistore.repository.UserRepository;
import hu.progmasters.gmistore.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private AuthService authService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Mock
    private SessionRegistry sessionRegistryMock;

    @Mock
    private JwtProvider jwtProviderMock;

    @Mock
    private EmailSenderService emailSenderServiceMock;

    @BeforeEach
    public void setup() {
        authService = new AuthService(userRepositoryMock,
                confirmationTokenRepositoryMock,
                passwordEncoderMock,
                authenticationManagerMock,
                sessionRegistryMock,
                jwtProviderMock,
                emailSenderServiceMock);
    }

    private final Supplier<RegisterRequest> registerRequestSupplier = () -> {
        RegisterRequest registerRequestDto = new RegisterRequest();
        registerRequestDto.setUsername("TesztElek01");
        registerRequestDto.setLastName("Teszt");
        registerRequestDto.setFirstName("Elek");
        registerRequestDto.setPassword("Teszt01@");
        registerRequestDto.setConfirmPassword("Teszt01@");
        registerRequestDto.setEmail("teszt-elek@gmail.com");
        registerRequestDto.setSeller(true);
        registerRequestDto.setActive(true);
        return registerRequestDto;
    };

    private final Supplier<User> userSupplier = () -> {
        User user = new User();
        user.setId(1L);
        user.setUsername("tesztElek01");
        user.setLastName("Teszt");
        user.setFirstName("Elek");
        user.setShippingAddress(new Address());
        user.setBillingAddress(new Address());
        user.setPassword("Teszt01@");
        user.setEmail("teszt-elek@gmail.com");
        user.setPhoneNumber("06324356644");
        user.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
        user.setRegistered(LocalDateTime.now());
        user.setActive(true);
        user.setCart(new Cart());
        user.setFavoriteProducts(new HashSet<>());
        user.setOrderList(new ArrayList<>());
        return user;
    };


    @Test
    public void testSaveUser() {
        RegisterRequest registerRequestDto = registerRequestSupplier.get();

        when(userRepositoryMock.save(any(User.class))).thenAnswer(returnsFirstArg());

        User savedUser = authService.registerUser(registerRequestDto);

        assertEquals("TesztElek01", savedUser.getUsername());
    }

}
