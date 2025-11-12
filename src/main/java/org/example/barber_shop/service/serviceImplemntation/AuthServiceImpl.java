package org.example.barber_shop.service.serviceImplemntation;
import lombok.RequiredArgsConstructor;
import org.example.barber_shop.dao.entities.User;
import org.example.barber_shop.dao.entities.UserRole;
import org.example.barber_shop.dto.request.LoginRequest;
import org.example.barber_shop.dto.request.RegisterRequest;
import org.example.barber_shop.dto.response.AuthResponse;
import org.example.barber_shop.dao.repositories.UserRepository;
import org.example.barber_shop.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // 1) Vérifier si l'email est déjà utilisé
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }
        //Creation du User
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.CUSTOMER);
        //sauvgarde du user
        userRepository.save(user);
        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                null
        );

    }
    @Override
    public AuthResponse login(LoginRequest request) {
        // 1) Récupération du user par email
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        //Vérification du mot de passe
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3) Retourner réponse sans JWT pour le moment
        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                null
        );
    }
}