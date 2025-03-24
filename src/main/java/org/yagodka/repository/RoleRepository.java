package org.yagodka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yagodka.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long>  {
}
