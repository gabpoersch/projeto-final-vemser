package br.com.dbc.devser.colabore.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "fundraiser")
public class FundraiserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_FUNDRAISER")
    @SequenceGenerator(name = "GEN_FUNDRAISER", sequenceName = "sequence_fundraiser", allocationSize = 1)
    @Column(name = "fundraiser_id")
    private Long fundraiserId;

    private String title;

    private String description;

    private Double goal;

    @Column(name = "current_value")
    private Double currentValue;

    @Column(name = "cover_photo")
    private String coverPhoto;

    private Boolean status;

    private Category category;

    @Column(name = "creation_date")
    private Long creationDate;

    @Column(name = "automatic_close")
    private Boolean automaticClose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundraiser_creator", referencedColumnName = "user_id")
    @Column(name = "fundraiser_creator")
    private UserEntity fundraiseCreator;
}
