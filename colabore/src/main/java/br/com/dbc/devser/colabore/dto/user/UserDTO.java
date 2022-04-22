package br.com.dbc.devser.colabore.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends UserCreateDTO {

    @ApiModelProperty(value = "Id do usuário")
    private Integer userId;
}