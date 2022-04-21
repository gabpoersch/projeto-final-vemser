package br.com.dbc.devser.colabore.entity;

import java.time.LocalDateTime;

public class FundraiserEntity {
    private Integer fundraiserId;
    private String title;
    private String description;
    private String goal;
    private String currentValue;
    private Boolean status;
    private Category category;
    private LocalDateTime creationDate;
    //PEGAR DE USER ENTITY
    private Integer userId;


}
