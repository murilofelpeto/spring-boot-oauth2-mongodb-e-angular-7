package br.com.murilo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.murilo.domain.User;
import br.com.murilo.domain.VerificationToken;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {

	Optional<VerificationToken> findByToken(String token);
	VerificationToken findByUser(User user);
}
