package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.exception.UserColaboreException;
import br.com.dbc.devser.colabore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> list() {
        return userService.list();
    }

    @PostMapping("/register")
    public UserDTO create(@Valid @RequestBody UserCreateDTO userCreateDTO) throws BusinessRuleException, UserColaboreException {
        return userService.create(userCreateDTO);
    }

    @PutMapping
    public UserDTO update(@Valid @RequestBody UserCreateDTO userUpdateDTO) throws BusinessRuleException, UserColaboreException {
        return userService.update(userUpdateDTO, userService.getLoggedUserId());
    }

    @DeleteMapping
    public UserDTO delete() throws BusinessRuleException, UserColaboreException {
        return userService.delete(userService.getLoggedUserId());
    }
}
