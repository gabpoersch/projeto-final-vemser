package br.com.dbc.devser.colabore.repository;

import br.com.dbc.devser.colabore.entity.FundraiserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FundraiserRepository extends JpaRepository<FundraiserEntity, Long> {
    @Query("select f from fundraiser f where f.fundraiserCreator.userId = :userId and f.statusActive=true")
    Page<FundraiserEntity> findFundraisersOfUser(@Param("userId") Long userId, Pageable pageable);

    @Query("select f from fundraiser f where f.statusActive=true")
    Page<FundraiserEntity> findAllFundraisersActive(Pageable pageable);


    @Query("select f from fundraiser f where f.endingDate = :end")
    List<FundraiserEntity> finishedFundraisers(@Param("end") LocalDate end);

    @Query(nativeQuery = true, value =
            "SELECT * FROM fundraiser f " +
                    "INNER JOIN fundraiser_category fc ON (f.fundraiser_id = fc.fundraiser_id)" +
                    "INNER JOIN category c ON (fc.category_id = c.category_id) WHERE lower(c.\"name\") IN (?1)")
    Page<FundraiserEntity> filterByCategories(List<String> categories, Pageable pageable);

}
