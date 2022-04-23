package br.com.dbc.devser.colabore.service;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.entity.UserEntity;
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

    public UserDTO create(UserCreateDTO userDTO) throws UserColaboreException {
        verifyIfEmailExists(userDTO);

        UserEntity user = objectMapper.convertValue(userDTO, UserEntity.class);
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        user.setRoles(roleRepository.findById(1)
                .orElseThrow(() -> new UserColaboreException("Role not found!")));

        return buildExposedDTO(userRepository.save(setPhotoBytes(user, userDTO)));
    }

    public List<UserDTO> list() {
        return userRepository.findAll().stream()
                .map(this::buildExposedDTO)
                .collect(Collectors.toList());
    }

    public UserDTO update(UserCreateDTO updateUserDTO) throws UserColaboreException {
        UserEntity userEntity = userRepository.findById(getLoggedUserId())
                .orElseThrow(() -> new UserColaboreException("User not found!"));

        verifyIfEmailExists(updateUserDTO);

        userEntity.setEmail(updateUserDTO.getEmail());
        userEntity.setName(updateUserDTO.getName());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(updateUserDTO.getPassword()));

        return buildExposedDTO(userRepository.save(setPhotoBytes(userEntity, updateUserDTO)));
    }

    public void delete() {
        userRepository.deleteById(getLoggedUserId());
    }

    public UserEntity findByLogin(String email) {
        return userRepository.findByEmail(email);
    }

    public Long getLoggedUserId() {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.getLong(userId);
    }

    private void verifyIfEmailExists(UserCreateDTO userDTO) throws UserColaboreException {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserColaboreException("Email already exists.");
        }
    }

    private UserEntity setPhotoBytes(UserEntity userEntity, UserCreateDTO userDTO) {
        MultipartFile profilePhoto = userDTO.getProfilePhoto();
        if (profilePhoto != null) {
            try {
                userEntity.setPhoto(profilePhoto.getBytes());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return userEntity;
    }

    private UserDTO buildExposedDTO(UserEntity userEntity) {
        UserEntity newUser = userRepository.save(userEntity);

        UserDTO exposedDTO = new UserDTO();
        exposedDTO.setUserId(newUser.getUserId());
        exposedDTO.setEmail(newUser.getEmail());

        byte[] file = newUser.getPhoto();
        if (file != null) {
            exposedDTO.setProfilePhoto(Base64.getEncoder().encodeToString(file));
        }
        return exposedDTO;
    }
}
