package br.com.dbc.devser.colabore.dto.fundraiser;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserUserContributionsDTO extends FundraiserGenericDTO {

    @ApiModelProperty(value = "Fechada ou Aberta")
    private Boolean status;

    @ApiModelProperty(value = "Total de doações do usuário")
    private BigDecimal totalContribution;

}
