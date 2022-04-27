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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;

    public UserDTO create(UserCreateDTO userDTO) throws UserColaboreException {
        verifyIfEmailExists(userDTO, false);

        UserEntity user = objectMapper.convertValue(userDTO, UserEntity.class);
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        user.setRoles(roleRepository.findById(1)
                .orElseThrow(() -> new UserColaboreException("Role not found!")));

        return buildExposedDTO(setPhotoBytes(user, userDTO));
    }

    public List<UserDTO> list() {
        return userRepository.findAll().stream()
                .map(this::buildExposedDTO)
                .collect(Collectors.toList());
    }

    public UserDTO update(UserCreateDTO updateUserDTO) throws UserColaboreException {

        UserEntity userEntity = verifyIfEmailExists(updateUserDTO, true);

        userEntity.setEmail(updateUserDTO.getEmail());
        userEntity.setName(updateUserDTO.getName());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(updateUserDTO.getPassword()));
        userEntity.setRoles(userEntity.getRoles());

        return buildExposedDTO(setPhotoBytes(userEntity, updateUserDTO));
    }

    public void delete() throws UserColaboreException {
        userRepository.deleteById(getLoggedUserId().getUserId());
    }

    public UserEntity findByLogin(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity getLoggedUserId() throws UserColaboreException {
        String findUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(Long.valueOf(findUserId)).orElseThrow(() ->
                new UserColaboreException("User not found!"));
    }

    private UserEntity verifyIfEmailExists(UserCreateDTO userDTO, boolean verificationFlag) throws UserColaboreException {
        UserEntity oldUser = null;
        if (verificationFlag) {
            /*Faz a verificação do usuário apenas uma vez*/
            oldUser = getLoggedUserId();

            if (!Objects.equals(oldUser.getEmail(), userDTO.getEmail())) {
                verificationEmail(userDTO);
            }
        } else {
            verificationEmail(userDTO);
        }
        return oldUser;
    }

    private void verificationEmail(UserCreateDTO userDTO) throws UserColaboreException {
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
        exposedDTO.setName(newUser.getName());
        exposedDTO.setEmail(newUser.getEmail());

        byte[] file = newUser.getPhoto();
        if (file != null) {
            exposedDTO.setProfilePhoto(Base64.getEncoder().encodeToString(file));
        }
        return exposedDTO;
    }

}
