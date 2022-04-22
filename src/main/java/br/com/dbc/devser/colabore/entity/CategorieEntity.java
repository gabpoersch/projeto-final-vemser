package br.com.dbc.devser.colabore.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "categorie")
public class CategorieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_CATEGORIE")
    @SequenceGenerator(name = "GEN_CATEGORIE", sequenceName = "sequence_categorie", allocationSize = 1)
    @Column(name = "categorie_id")
    private Long categorieId;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundraiser_id", referencedColumnName = "fundraiser_id")
    private Set<FundraiserEntity> fundraisers;
}
