package br.com.dbc.devser.colabore.security;

import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserEntity loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userService.findByLogin(login));
        if (userEntityOptional.isPresent()) {
            return userEntityOptional.get();
        }
        throw new UsernameNotFoundException("User not found!");
    }
}
