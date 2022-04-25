package br.com.dbc.devser.colabore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserContributions {
    private FundraiserEntity fundraiserEntity;
    private BigDecimal value;
}
