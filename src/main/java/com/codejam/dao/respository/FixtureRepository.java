package com.codejam.dao.respository;

import com.codejam.dao.entities.Fixture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FixtureRepository extends CrudRepository<Fixture, Long> {

}
