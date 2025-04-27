package org.yagodka.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yagodka.product.entity.Product;

@Repository
public interface CommentRepository extends JpaRepository<Product, Long> {
}
