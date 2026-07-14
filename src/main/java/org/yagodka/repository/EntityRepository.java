package org.yagodka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yagodka.model.MyEntity;

import java.util.List;

@Repository
public interface EntityRepository extends JpaRepository<MyEntity, Long> {

    @Query("SELECT e FROM MyEntity e WHERE LOWER(e.type) LIKE LOWER(CONCAT('%', :filter, '%')) OR LOWER(e.displayName) " +
            "LIKE LOWER(CONCAT('%', :filter, '%')) ORDER BY e.id ASC")
    List<MyEntity> findByFilterWithDefaultOrder(@Param("filter") String filter);

    @Query("SELECT e FROM MyEntity e WHERE LOWER(e.type) LIKE LOWER(CONCAT('%', :filter, '%')) OR LOWER(e.displayName) " +
            "LIKE LOWER(CONCAT('%', :filter, '%')) ORDER BY e.id DESC")
    List<MyEntity> findByFilterWithDescOrder(@Param("filter") String filter);

    @Query("SELECT e FROM MyEntity e ORDER BY e.id ASC")
    List<MyEntity> findAllWithDefaultOrder();

    @Query("SELECT e FROM MyEntity e ORDER BY e.id DESC")
    List<MyEntity> findAllWithDescOrder();
}
