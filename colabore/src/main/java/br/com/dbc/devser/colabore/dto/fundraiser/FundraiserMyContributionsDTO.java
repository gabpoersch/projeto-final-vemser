package br.com.dbc.devser.colabore.dto.fundraiser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserMyContributionsDTO extends FundraiserGenericDTO{

    private Boolean status;
    private BigDecimal totalContribution;
}
