package br.com.dbc.devser.colabore.dto.donate;

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
    private String message;
    private BigDecimal value;
}
