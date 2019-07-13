package br.com.murilo.resources;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.murilo.domain.Role;
import br.com.murilo.domain.User;
import br.com.murilo.dto.UserDTO;
import br.com.murilo.services.UserService;

@RestController
@RequestMapping("/api")
public class UserResource {

	@Autowired
	private UserService service;
	
	@Autowired
	private DefaultTokenServices tokenServices = new DefaultTokenServices();
	
	private TokenStore tokenStore = new InMemoryTokenStore();
	

	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> findAll() {
		List<User> users = service.findAll();
		List<UserDTO> listDTO = users.stream().map(u -> new UserDTO(u)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable String id) {
		User user = service.findById(id);
		return ResponseEntity.ok().body(new UserDTO(user));
	}

	@PostMapping("/users")
	public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
		User user = service.create(userDTO);
		return new ResponseEntity<>(new UserDTO(user), HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UserDTO userDTO) {
		userDTO.setId(id);
		User user = service.update(userDTO);
		return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/users/{id}/roles")
	public ResponseEntity<List<Role>> findRoles(@PathVariable String id) {
		User user = service.findById(id);

		return ResponseEntity.ok().body(user.getRoles());
	}

	@GetMapping("/users/main")
	public ResponseEntity<UserDTO> getUserMain(Principal principal) {
		UserDTO user = new UserDTO(service.findByEmail(principal.getName()));
		user.setPassword("");
		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if(authHeader != null) {
			String tokenValue = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenServices.readAccessToken(tokenValue);
			tokenStore.removeAccessToken(accessToken);
			tokenServices.revokeToken(String.valueOf(accessToken));
		}
		return ResponseEntity.noContent().build();
	}
}
