package co.edu.iudigital.helpmeiud.repositories;

import co.edu.iudigital.helpmeiud.models.Role;
import co.edu.iudigital.helpmeiud.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByNombre(RoleEnum nombre);
}
