package br.com.dbc.devser.colabore.dto.fundraiser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserCreateDTO {

    private String title;

    private BigDecimal goal;

    private Boolean automaticClose;

    private String description;

    private String coverPhoto;

    //>>>>TODO: Lista de tags
}
