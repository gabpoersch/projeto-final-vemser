package br.com.dbc.devser.colabore.security;

import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.findByEmail(login));
        if (userEntityOptional.isPresent()) {
            return userEntityOptional.get();
        }
        throw new UsernameNotFoundException("User not found!");
    }

}
