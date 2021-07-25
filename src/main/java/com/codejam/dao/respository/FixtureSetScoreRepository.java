package com.codejam.dao.respository;

import com.codejam.dao.entities.FixtureSetScore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FixtureSetScoreRepository extends CrudRepository<FixtureSetScore, Long> {

}
