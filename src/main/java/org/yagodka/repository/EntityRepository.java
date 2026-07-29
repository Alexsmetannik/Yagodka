package org.yagodka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yagodka.model.MyEntity;

import java.util.List;

@Repository
public interface EntityRepository extends JpaRepository<MyEntity, Long> {

    @Query("SELECT e FROM MyEntity e WHERE " +
            "LOWER(e.type) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(e.displayName) LIKE LOWER(CONCAT('%', :filter, '%')) " +
            "ORDER BY " +
            "CASE WHEN :order = 'DESC' THEN e.id END DESC, " +
            "CASE WHEN :order = 'ASC' OR :order IS NULL THEN e.id END ASC")
    List<MyEntity> findByFilterWithOrder(@Param("filter") String filter,
                                         @Param("order") String order);

    @Query("SELECT e FROM MyEntity e ORDER BY " +
            "CASE WHEN :order = 'DESC' THEN e.id END DESC, " +
            "CASE WHEN :order = 'ASC' OR :order IS NULL THEN e.id END ASC")
    List<MyEntity> findAllWithOrder(@Param("order") String order);
}
