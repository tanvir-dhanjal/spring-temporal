package com.codejam.dao.respository;

import com.codejam.dao.entities.Fixture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FixtureRepository extends CrudRepository<Fixture, Long> {
    /**
     * Find fixture involving player.
     *
     * @return Fixture
     */
    @Query(value = "SELECT * FROM fixture WHERE (player_one_id = :playerId OR player_two_id = :playerId) AND fixture_id = :fixtureId", nativeQuery = true)
    Fixture getPlayerFixture(@Param("playerId") Long playerId,
                             @Param("fixtureId") Long fixtureId);
}
