package br.com.murilo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.murilo.domain.User;
import br.com.murilo.dto.UserDTO;
import br.com.murilo.repository.UserRepository;
import br.com.murilo.service.exception.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repositorio;

	public List<User> findAll() {
		return repositorio.findAll();
	}

	public User findById(String id) {
		Optional<User> user = repositorio.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}

	public User create(UserDTO user) {
		return repositorio.save(fromDTO(user));
	}

	public User update(UserDTO dto) {
		Optional<User> update = repositorio.findById(dto.getId());
		return update.map(u -> repositorio.save(new User(dto)))
				.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}
	
	public void delete(String id) {
		repositorio.deleteById(id);
	}

	private User fromDTO(UserDTO dto) {
		return new User(dto);
	}
}
