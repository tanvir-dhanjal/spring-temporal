package com.codejam.dao.respository;

import com.codejam.dao.entities.PlayerStat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerStatRepository extends CrudRepository<PlayerStat, Long> {

}
