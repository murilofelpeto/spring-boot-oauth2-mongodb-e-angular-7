package br.com.murilo.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.murilo.domain.User;
import br.com.murilo.domain.VerificationToken;
import br.com.murilo.dto.UserDTO;
import br.com.murilo.repository.RoleRepository;
import br.com.murilo.repository.UserRepository;
import br.com.murilo.repository.VerificationTokenRepository;
import br.com.murilo.service.exception.ObjectAlreadyExistException;
import br.com.murilo.service.exception.ObjectNotFoundException;
import br.com.murilo.services.email.EmailService;

@Service
public class UserService {

	@Autowired
	private UserRepository repositorio;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private EmailService emailService;

	public List<User> findAll() {
		return repositorio.findAll();
	}

	public User findById(String id) {
		Optional<User> user = repositorio.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}

	public User create(UserDTO user) {
		user.setPassword(encoder.encode(user.getPassword()));
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

	public User registerUser(UserDTO user) {
		if (userExits(user.getEmail())) {
			throw new ObjectAlreadyExistException("Usuário já cadastrado");
		}

		user.setRoles(Arrays.asList(roleRepository.findByRoleName("ROLE_USER").get()));
		User rUser = create(user);
		emailService.sendConfirmationHtmlEmail(rUser, null);
		return rUser;
	}

	public void createVerificationTokenForUser(User user, String token) {
		VerificationToken verificationToken = new VerificationToken(token, user);
		tokenRepository.save(verificationToken);
	}

	public String validateVerificationToken(String token) {
		Optional<VerificationToken> vToken = tokenRepository.findByToken(token);
		if (!vToken.isPresent()) {
			return "Invalid token";
		}
		User user = vToken.get().getUser();
		Calendar calendar = Calendar.getInstance();
		if ((vToken.get().getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
			return "Token expired";
		}
		user.setEnable(true);
		repositorio.save(user);
		return null;
	}
	
	public User findByEmail(String email) {
		Optional<User> user = repositorio.findByEmail(email);
		return user.orElseThrow(() -> new ObjectNotFoundException("Usuário " + email + " não encontrado!"));
	}

	private Boolean userExits(String email) {
		if (repositorio.findByEmail(email).isPresent()) {
			return true;
		}
		return false;
	}

	private User fromDTO(UserDTO dto) {
		return new User(dto);
	}
}
