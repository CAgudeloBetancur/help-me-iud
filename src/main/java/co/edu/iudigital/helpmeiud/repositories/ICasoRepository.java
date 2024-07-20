package co.edu.iudigital.helpmeiud.repositories;

import co.edu.iudigital.helpmeiud.models.Caso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICasoRepository extends JpaRepository<Caso,Long> {

    @Query("UPDATE Caso c SET c.visible = :visible WHERE c.id = :id") //JPQL
    Boolean setVisible(Boolean visible, Long id);

    @Query("SELECT c FROM Caso c WHERE c.usuario.username = :username")
    List<Caso> findAllByUsuarioUsername(String username);

        // OTRAS DOS FORMAS DE CONSTRUIR ESTE MÉTODO

        // Usando ?#
        /*@Query("SELECT c FROM Caso c WHERE c.usuario.username = ?1")
        List<Caso> findAllByUsuarioUsername2(String username);*/

        // Usando @Param("nombreParamétro")
        // El nombre del parámetro en @Param debe ser igual al de @Query
        // Generalmente esta sintaxis se usa cuando el nombre parámetro del método difiere del de la query
        /*@Query("SELECT c FROM Caso c WHERE c.usuario.username = :username")
        List<Caso> findAllByUsuarioUsername3(@Param("username") String user);*/

    List<Caso> findAllByVisible(Boolean visible);

        // Este método busca casos por true o false, depende de lo que se pase por parámetro

        // OTRA FORMA SERÍA

        // List<Caso> findAllByVisibleTrue(); // específico para true
        // List<Caso> findAllByVisibleFalse(); // específico para false

}
