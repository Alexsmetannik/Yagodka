package org.yagodka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yagodka.entity.TypeProduct;

@Repository
public interface TypeProductRepository  extends JpaRepository<TypeProduct, Long> {
    TypeProduct findByName(String name);
}
