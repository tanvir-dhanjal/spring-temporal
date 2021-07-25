package com.codejam.resource;

import com.codejam.service.impl.ApiServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import static com.codejam.utils.AsyncUtil.completeResponse;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * This jersey controller handles all the API calls made on the end point.
 * <p>
 * - /players: create new players and get player by id
 * - /players/{playedId}/fixture: create new fixture
 *
 * @author tdhanjal
 */
@Path("/secure")
@Produces(APPLICATION_JSON)
public class PlayersResource {

    @Autowired
    ApiServiceImpl playersService;

    @GET
    @Path("/health")
    public void getHealthCheck(@Suspended AsyncResponse asyncResponse) {
        asyncResponse.resume(ImmutableMap.of("status", "ok"));
    }

    @POST
    @Path("/players")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void createPlayer(@FormParam("FirstName") final String firstName,
                             @FormParam("LastName") final String lastName,
                             @FormParam("Level") final Float level,
                             @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        playersService.createPlayer(firstName, lastName, level),
                asyncResponse);
    }

    @GET
    @Path("/players/{playerId}")
    public void getPlayer(@PathParam("playerId") final Long playerId,
                          @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        playersService.getPlayer(playerId),
                asyncResponse);
    }

    @POST
    @Path("/players/{playerId}/fixture")
    public void createFixture(@PathParam("playerId") final Long playerId,
                              @FormParam("OpponentPlayerId") final Long opponentPlayerId,
                              @FormParam("scheduledFor") final String scheduledFor,
                              @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        playersService.createFixture(playerId, opponentPlayerId, scheduledFor),
                asyncResponse);
    }

    @GET
    @Path("/players/{playerId}/fixture/{fixtureId}")
    public void getFixture(@PathParam("playerId") final Long playerId,
                           @PathParam("fixtureId") final Long fixtureId,
                           @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        playersService.getFixture(fixtureId),
                asyncResponse);
    }

    @POST
    @Path("/players/{playerId}/fixture/{fixtureId}")
    public void createFixtureSetScore(@PathParam("playerId") final Long playerId,
                                      @PathParam("fixtureId") final Long fixtureId,
                                      @FormParam("SetNumber") final Integer setNumber,
                                      @FormParam("PlayerOneSetScore") final Integer playerOneSetScore,
                                      @FormParam("PlayerTwoSetScore") final Integer playerTwoSetScore,
                                      @Suspended final AsyncResponse asyncResponse) {
        completeResponse(() ->
                        playersService.createFixtureSetScore(fixtureId, setNumber, playerOneSetScore, playerTwoSetScore),
                asyncResponse);
    }
}
