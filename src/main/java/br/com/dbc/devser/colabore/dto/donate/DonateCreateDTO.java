package br.com.dbc.devser.colabore.dto.donate;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonateCreateDTO {

    @ApiModelProperty(value = "Mensagem do doador")
    private String message;

    @ApiModelProperty(value = "Valor doado")
    private BigDecimal value;

}
