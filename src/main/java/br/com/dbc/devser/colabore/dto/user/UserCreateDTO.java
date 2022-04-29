package br.com.dbc.devser.colabore.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

//    @NotBlank(message = "You must provide a name")
    @ApiModelProperty(value = "Nome do usuário")
    private String name;

    @Email(message = "The email field is not valid")
    @ApiModelProperty(value = "Email do usuário")
    private String email;

//    @NotBlank(message = "You must provide a password")
    @ApiModelProperty(value = "Senha do usuário")
    private String password;

    @JsonIgnore
    @ApiModelProperty(value = "Foto de perfil")
    private MultipartFile profilePhoto;

}