package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import br.com.dbc.devser.colabore.entity.UserEntity;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.repository.RoleRepository;
import br.com.dbc.devser.colabore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;

    public UserDTO create(UserCreateDTO userDTO) throws BusinessRuleException, UserColaboreException {
        verifyIfEmailExists(userDTO);

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        userEntity.setRoles(roleRepository.findById(1).orElseThrow(() -> new BusinessRuleException("Role not found!")));
        try {
            MultipartFile profilePhoto = userDTO.getProfilePhoto();
            if (profilePhoto != null) {
                userEntity.setProfilePhoto(profilePhoto.getBytes());
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return buildExposedDTO(userRepository.save(userEntity));
    }

    public List<UserDTO> list() {
        return userRepository.findAll().stream()
                .map(user -> objectMapper.convertValue(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserDTO update(UserCreateDTO updateUserDTO, Integer userId) throws BusinessRuleException, UserColaboreException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserColaboreException("User not found!"));

        verifyIfEmailExists(updateUserDTO);

        try {
            userEntity.setEmail(updateUserDTO.getEmail());
            userEntity.setName(updateUserDTO.getName());
            userEntity.setPassword(new BCryptPasswordEncoder().encode(updateUserDTO.getPassword()));
            userEntity.setProfilePhoto(updateUserDTO.getProfilePhoto().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buildExposedDTO(userRepository.save(userEntity));
    }

    public UserDTO delete(Integer userId) throws UserColaboreException {
        userRepository.findById(userId).orElseThrow(() -> new UserColaboreException("User not found!"));
        userRepository.deleteById(userId);
        return null;
    }

    public UserEntity findByLogin(String email) {
        return userRepository.findByEmail(email);
    }

    public Integer getLoggedUserId() throws BusinessRuleException, UserColaboreException {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity loggedUser = userRepository.findById(Integer.parseInt(userId)).orElseThrow(() -> new UserColaboreException("User not found!"));
        return loggedUser.getUserId();
    }

    private void verifyIfEmailExists(UserCreateDTO userDTO) throws UserColaboreException {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserColaboreException("Email already exists.");
        }
    }

    private UserDTO buildExposedDTO(UserEntity userEntity) {
        UserEntity newUser = userRepository.save(userEntity);

        UserDTO exposedDTO = new UserDTO();
        exposedDTO.setUserId(newUser.getUserId());
        exposedDTO.setEmail(newUser.getEmail());
        if (newUser.getProfilePhoto() != null) {
            exposedDTO.setProfilePhoto(Base64.getEncoder().encodeToString(newUser.getProfilePhoto()));
        }
        return exposedDTO;
    }
}
