package br.com.dbc.devser.colabore.dto.fundraiser;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundraiserCreateDTO {

    @ApiModelProperty(value = "Título da campanha")
    private String title;

    @ApiModelProperty(value = "Meta da campanha")
    private BigDecimal goal;

    @ApiModelProperty(value = "Fechar automaticamente quando atigir a meta")
    private Boolean automaticClose;

    @ApiModelProperty(value = "Descrição")
    private String description;

    @ApiModelProperty(value = "Foto de capa")
    private File coverPhoto;

    @ApiModelProperty(value = "Lista de categorias")
    private Set<String> categories;
}
