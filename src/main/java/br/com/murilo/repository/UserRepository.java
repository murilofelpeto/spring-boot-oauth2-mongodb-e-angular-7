package br.com.murilo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.murilo.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

	Optional<User> findByEmail(String email);
}
