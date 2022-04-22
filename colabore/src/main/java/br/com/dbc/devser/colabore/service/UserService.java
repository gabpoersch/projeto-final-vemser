package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.repository.RoleRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;


    public UserDTO create(UserCreateDTO userDTO) throws BusinessRuleException {
        UserEntity userEntity = objectMapper.convertValue(userDTO, UserEntity.class);
        userEntity.setPassword(new BCryptPasswordEncoder().encode(userEntity.getPassword()));
        userEntity.setRoles(roleRepository.findById(1).orElseThrow(() -> new BusinessRuleException ("Role not found!")));

        return objectMapper.convertValue(userRepository.save(userEntity), UserDTO.class);
    }

    public List<UserDTO> list() {
        return userRepository.findAll().stream()
                .map(user -> objectMapper.convertValue(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserDTO update(UserCreateDTO updateUserDTO, Integer userId) throws BusinessRuleException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new BusinessRuleException("User not found!"));
        userEntity.setEmail(updateUserDTO.getEmail());
        userEntity.setName(updateUserDTO.getName());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(updateUserDTO.getPassword()));

        return objectMapper.convertValue((userRepository.save(userEntity)), UserDTO.class);
    }

    public UserDTO delete(Integer userId) throws BusinessRuleException {
        userRepository.findById(userId).orElseThrow(() -> new BusinessRuleException("User not found!"));
        userRepository.deleteById(userId);
        return null;
    }

    public UserEntity findByLogin(String email) {
        return userRepository.findByEmail(email);
    }

    public Integer getLoggedUserId() throws BusinessRuleException {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity loggedUser = userRepository.findById(Integer.parseInt(userId)).orElseThrow(() -> new BusinessRuleException("User not found!"));
        return loggedUser.getUserId();
    }
}
