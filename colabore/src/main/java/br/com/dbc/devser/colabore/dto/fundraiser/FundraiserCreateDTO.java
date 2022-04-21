package br.com.dbc.devser.colabore.dto.fundraiser;

import lombok.*;

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
