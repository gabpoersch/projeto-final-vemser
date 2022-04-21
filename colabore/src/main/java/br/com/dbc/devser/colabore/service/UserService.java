package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;


    public UserDTO create (UserCreateDTO userDTO) {
        UserEntity userEntity = objectMapper.convertValue(userDTO, UserEntity.class);
        userEntity.setPassword(new BCryptPasswordEncoder().encode(userEntity.getPassword()));

        return objectMapper.convertValue(userRepository.save(userEntity), UserDTO.class);
    }

    public UserDTO update (UserDTO updateUserDTO, Integer userId) throws BusinessRuleException {
        userRepository.findById(userId).orElseThrow(() -> new BusinessRuleException("User not found!"));
        UserEntity userEntity = userRepository.getById(userId);

        userEntity = objectMapper.convertValue(updateUserDTO, UserEntity.class);
        userEntity.setEmail(updateUserDTO.getEmail());
        userEntity.setName(updateUserDTO.getName());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(updateUserDTO.getPassword()));

        return objectMapper.convertValue((userRepository.save(userEntity)), UserDTO.class);
    }

    public void delete (Integer userId) throws BusinessRuleException {
        userRepository.findById(userId).orElseThrow(() -> new BusinessRuleException("User not found!"));
        userRepository.deleteById(userId);
    }

    public UserEntity findByLogin(String email) {
        return userRepository.findByEmail(email);
    }
}
