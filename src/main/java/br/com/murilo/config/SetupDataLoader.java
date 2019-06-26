package br.com.murilo.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import br.com.murilo.domain.User;
import br.com.murilo.repository.UserRepository;

@Configuration
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UserRepository repository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		User joao = new User("Joao", "Silva", "joao@gmail.com");
		User maria = new User("Maria", "Teixeira", "maria@gmail.com");
		
		createUserIfNotFound(joao);
		createUserIfNotFound(maria);
	}

	private User createUserIfNotFound(final User user) {
		Optional<User> obj = repository.findByEmail(user.getEmail());
		if (obj.isPresent()) {
			return obj.get();
		}
		return repository.save(user);
	}
}
