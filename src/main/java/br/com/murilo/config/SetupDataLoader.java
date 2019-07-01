package br.com.murilo.config;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.murilo.domain.Role;
import br.com.murilo.domain.User;
import br.com.murilo.repository.RoleRepository;
import br.com.murilo.repository.UserRepository;

@Configuration
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Role admin = new Role("ROLE_ADMIN");
		Role user = new Role("ROLE_USER");

		User joao = new User("Joao", "Silva", "joao@gmail.com", Arrays.asList(admin), encoder.encode("123admin"), true);
		User maria = new User("Maria", "Teixeira", "maria@gmail.com", Arrays.asList(user), encoder.encode("456user"),
				true);
		User murilo = new User("Murilo", "Felpeto", "murilofelpeto@hotmail.com", Arrays.asList(admin),
				encoder.encode("Murilo!8"), true);

		createRoleIfNotFound(admin);
		createRoleIfNotFound(user);

		createUserIfNotFound(joao);
		createUserIfNotFound(maria);
		createUserIfNotFound(murilo);
	}

	private User createUserIfNotFound(final User user) {
		Optional<User> obj = repository.findByEmail(user.getEmail());
		if (obj.isPresent()) {
			return obj.get();
		}
		return repository.save(user);
	}

	private Role createRoleIfNotFound(Role role) {
		Optional<Role> obj = roleRepository.findByRoleName(role.getRoleName());
		if (obj.isPresent()) {
			return obj.get();
		}
		return roleRepository.save(role);
	}
}
