package org.yagodka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yagodka.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
