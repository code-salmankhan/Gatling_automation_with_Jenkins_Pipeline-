package feeders;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import javax.swing.*;

import java.util.Objects;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VideoGameJsonFeeders extends Simulation {


        // 1 Http Configuration
        private HttpProtocolBuilder httpProtocol = http
                .baseUrl("https://videogamedb.uk/api")
                .acceptHeader("application/json");

        private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/gameJsonFile.json").circular();



        private static ChainBuilder getSpecificGame =
                feed(jsonFeeder)
                        .exec(http("Get video game with name - #{name}")
                                .get("/videogame/#{id}")
                                .check(jmesPath("name").isEL("#{name}")));




        // 2 Scenario Definition
        private ScenarioBuilder scn = scenario("My First Test")
                .repeat(10).on(
                        exec(getSpecificGame)
                                .pause(1)
                );



        // 3 Load Simulation
        {
            setUp(
                    scn.injectOpen(atOnceUsers(1))
            ).protocols(httpProtocol);
        }
    }


