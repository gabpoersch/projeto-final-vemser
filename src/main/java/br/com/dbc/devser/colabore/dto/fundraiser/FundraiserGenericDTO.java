package br.com.dbc.devser.colabore.dto.fundraiser;

import br.com.dbc.devser.colabore.dto.user.UserDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserGenericDTO {

    @ApiModelProperty(value = "Id da campanha")
    private Long fundraiserId;

    @ApiModelProperty(value = "Foto de capa")
    private String coverPhoto;

    @ApiModelProperty(value = "Título da campanha")
    private String title;

    @ApiModelProperty(value = "Meta da campanha")
    private BigDecimal goal;

    @ApiModelProperty(value = "Valor alcançado até o momento")
    private BigDecimal currentValue;

    @ApiModelProperty(value = "Criador/autor da campanha")
    private UserDTO fundraiserCreator;

    @ApiModelProperty(value = "Lista de categorias")
    private Set<String> categories;

    @ApiModelProperty(value = "Última atualização")
    private LocalDateTime lastUpdate;

    //>>>>>>>> PLUS

    @ApiModelProperty(value = "Data de criação")
    private LocalDateTime creationDate;
}
