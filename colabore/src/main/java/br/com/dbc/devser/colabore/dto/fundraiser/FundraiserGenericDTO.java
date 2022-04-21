package br.com.dbc.devser.colabore.dto.fundraiser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserGenericDTO {

    private Long fundraiserId;

    private String coverPhoto;

    private String title;

    private BigDecimal goal;

    private BigDecimal currentValue;

    private String fundraiserCreator;

    private List<String> categories;

    private LocalDateTime lastUpdate;

    //>>>>>>>> PLUS

    private LocalDateTime creationDate;
}
