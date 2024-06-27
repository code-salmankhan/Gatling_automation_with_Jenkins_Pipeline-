package videogamedb.scriptfundamentals;

import ch.qos.logback.core.net.SyslogOutputStream;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class VideoGameDb extends Simulation {

    // 1 Http Configuration
      private HttpProtocolBuilder httpProtocol = http
              .baseUrl("https://videogamedb.uk/api")
              .acceptHeader("application/json");

      private static ChainBuilder getAllVideoGames =
              //loop
              repeat(3).on(
                              exec(http("Get all video games")
                                .get("/videogame")
                                .check(status().not(404),status().not(500)))
                             );

      private static ChainBuilder getSpecificVideoGame =
              //loop
              repeat(5, "myCounter").on(
                              exec(http("Get specific video game with id :#{myCounter}")
                              .get("/videogame/#{myCounter}")
                              .check(status().is(200)))
                             );

    // 2 Scenario Definition
      private ScenarioBuilder scn = scenario("Video Game Db - Section 5 code")

            .exec(getAllVideoGames)
            .pause(5)
            .exec(getSpecificVideoGame)
            .pause(5)
            //loop
            .repeat(2).on(
                    exec(getAllVideoGames)
            );



            /*
            .exec(http("Get all video games - 1st call")
                 .get("/videogame")
                    .check(status().is(200))
                    .check(jsonPath("$[?(@.id==1)].name").is("Resident Evil 4")))
            .pause(5)

            .exec(http("Get a specific games")
                 .get("/videogame/1")
                    .check(status().in(200, 201, 202))
                    .check(jmesPath("name").is("Resident Evil 4")))
            .pause(1,10)

            .exec(http("Get all video games - 2nd call")
                    .get("/videogame")
                    .check(status().not(404),status().not(500))
                    .check(jmesPath("[? category == 'Platform'].name").ofList().is(List.of("Super Mario 64")))
                    .check(jmesPath("[1].id").saveAs("gameId")))
            .pause(Duration.ofMillis(4000))

         //Session API

            .exec(
                   session -> {
                       System.out.println(session);
                       System.out.println("gameId set to: " +session.getString("gameId"));
                       return session;
                   }
            )


            .exec(http("Get specific game with id - #{gameId}")
                    .get("/videogame/#{gameId}")
                    .check(jmesPath("name").is("Gran Turismo 3"))
                    .check(bodyString().saveAs("responseBody")))
            .exec(
                    session -> {
                        System.out.println("Response Body : " + session.getString("responseBody"));
                        return session;
                    }
            );
             */


    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
