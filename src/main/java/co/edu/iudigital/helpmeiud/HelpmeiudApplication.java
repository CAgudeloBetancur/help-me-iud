package co.edu.iudigital.helpmeiud;

import co.edu.iudigital.helpmeiud.models.Role;
import co.edu.iudigital.helpmeiud.models.RoleEnum;
import co.edu.iudigital.helpmeiud.models.Usuario;
import co.edu.iudigital.helpmeiud.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class HelpmeiudApplication implements CommandLineRunner {

	@Autowired
	private IUsuarioRepository usuarioRepositorio;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HelpmeiudApplication.class, args);
	}

	@Override
	public void run (String ... args) throws Exception {

		Role rolAdmin = Role
			.builder()
			.nombre(RoleEnum.ADMIN)
			.descripcion(RoleEnum.ADMIN.name())
			.build();
		Role rolUsuario = Role
			.builder()
			.nombre(RoleEnum.USER)
			.descripcion(RoleEnum.USER.name())
			.build();
		HashSet<Role> adminRoles = new HashSet<>();
		adminRoles.add(rolAdmin);
		adminRoles.add(rolUsuario);
		Usuario usuarioAdmin = Usuario
			.builder()
			.username("camiloab97@gmail.com")
			.nombre("Camilo")
			.apellido("Agudelo")
			.password(passwordEncoder.encode("Camilo2024_"))
			.fechaNacimiento(LocalDate.parse("1997-08-01"))
			.enabled(true)
			.redSocial(true)
			.image("unaImagenPorDefecto")
			.roles(adminRoles)
			.build();

		Usuario usuario = Usuario
			.builder()
			.username("agubetcamilo97@gmail.com")
			.nombre("Roberto")
			.apellido("Perez")
			.password(passwordEncoder.encode("Roberto2024_"))
			.fechaNacimiento(LocalDate.parse("1981-09-22"))
			.enabled(true)
			.redSocial(true)
			.image("unaImagenPorDefecto")
			.roles(Collections.singleton(rolUsuario))
			.build();
		usuarioRepositorio.saveAll(Arrays.asList(usuarioAdmin, usuario));
	}
}
