package br.com.dbc.devser.colabore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_CATEGORY")
    @SequenceGenerator(name = "GEN_CATEGORY", sequenceName = "sequence_category", allocationSize = 1)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "fundraiser_category",
            joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "fundraiser_id", referencedColumnName = "fundraiser_id")
    )
    private Set<FundraiserEntity> fundraisers;

}
