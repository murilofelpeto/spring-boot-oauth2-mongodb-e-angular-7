package br.com.murilo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.murilo.domain.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String>{

	Optional<Role> findByRoleName(String name);
}
