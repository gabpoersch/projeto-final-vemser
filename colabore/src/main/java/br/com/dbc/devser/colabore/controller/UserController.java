package br.com.dbc.devser.colabore.controller;

import br.com.dbc.devser.colabore.dto.user.UserCreateDTO;
import br.com.dbc.devser.colabore.dto.user.UserDTO;
import br.com.dbc.devser.colabore.exception.BusinessRuleException;
import br.com.dbc.devser.colabore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public List<UserDTO> list () {
        return userService.list();
    }

    @PostMapping
    public UserDTO create (@Valid @RequestBody UserCreateDTO userCreateDTO) throws BusinessRuleException {
        return userService.create(userCreateDTO);
    }

    @PutMapping
    public UserDTO update (@Valid @RequestBody UserDTO userDTO) throws BusinessRuleException {
        String authUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.update(userDTO, Integer.parseInt(authUserId));
    }

    @DeleteMapping
    public UserDTO delete (@PathVariable("idUser") Integer userDeleteId) throws BusinessRuleException {
        userDeleteId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.delete(userDeleteId);
    }
}
