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

    @Query("select f from fundraiser f where f.statusActive=true and f.currentValue < f.goal")
    Page<FundraiserEntity> findFundraisersActiveNotAcchieved(Pageable pageable);

    @Query("select f from fundraiser f where f.statusActive=true and f.currentValue >= f.goal")
    Page<FundraiserEntity> findFundraisersActiveAcchieved(Pageable pageable);

    @Query("select f from fundraiser f where f.statusActive=true")
    Page<FundraiserEntity> findAllFundraisersActive(Pageable pageable);

    @Query("select f from fundraiser f where f.endingDate = :end")
    List<FundraiserEntity> finishedFundraisers(@Param("end") LocalDate end);

//    @Query(nativeQuery = true, value =
//            "    SELECT f from fundraiser f \n" +
//                    "    INNER JOIN\n" +
//                    "        fundraiser_category fc ON (f.fundraiser_id = fc.fundraiser_id)\n" +
//                    "    INNER JOIN\n" +
//                    "        category c ON (fc.category_id = c.category_id) \n" +
//                    "    WHERE\n" +
//                    "        lower(c.name) IN (?1) AND f.status = true " +
//                    "GROUP BY f.fundraiser_id, fc.fundraiser_id" +
//                    ", fc.category_id, c.category_id")
//    Page<FundraiserEntity> filterByCategories(List<String> categories, Pageable pageable);
//
//    @Query("select f from fundraiser f join ")
}
