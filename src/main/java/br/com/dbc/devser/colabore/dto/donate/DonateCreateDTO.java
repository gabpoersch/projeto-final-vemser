package br.com.dbc.devser.colabore.dto.donate;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonateCreateDTO {

    @NotNull(message = "Value field cannot be null")
    @ApiModelProperty(value = "Valor doado")
    private BigDecimal value;

}
