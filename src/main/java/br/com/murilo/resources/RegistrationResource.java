package br.com.murilo.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.murilo.dto.UserDTO;
import br.com.murilo.resources.util.GenericResponse;
import br.com.murilo.services.UserService;

@RestController
@RequestMapping("/api/public")
public class RegistrationResource {

	@Autowired
	private UserService service;

	@GetMapping("/registrationConfirm/users")
	public ResponseEntity<GenericResponse> confirmRegistrationUser(@RequestParam("token") String token) {
		Object result = service.validateVerificationToken(token);
		if (result == null) {
			return ResponseEntity.ok().body(new GenericResponse("Success"));
		}
		return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse("ERROR", result.toString()));
	}

	@GetMapping("/resendRegistrationToken/users")
	public ResponseEntity<Void> resendRegistrationToken(@RequestParam("email") String email) {
		service.generateNewVerificationToken(email);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/registration/users")
	public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO) {
		service.registerUser(userDTO);
		return ResponseEntity.noContent().build();
	}
}
