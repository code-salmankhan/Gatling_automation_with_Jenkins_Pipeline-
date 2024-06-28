package simulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VideoGameDbSimulation extends Simulation {

    // 1 Http Configuration
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "5"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION","10"));
    private static final int TEST_DURATION = Integer.parseInt(System.getProperty("TEST_DURATION","20"));

    @Override
    public void before(){
        System.out.printf("Running test with %d users %n", USER_COUNT);
        System.out.printf("Ramping users over %d seconds %n", RAMP_DURATION);
        System.out.printf("Running test duration %d users %n", TEST_DURATION);
    }

    private static ChainBuilder getAllVideogames =
            exec(http("Get all video game")
                    .get("/videogame"));

    private static ChainBuilder getSpecificGame =
            exec(http("Get Specific game")
                    .get("/videogame/2"));




    // 2 Scenario Definition
    private ScenarioBuilder scn = scenario("Video game db - Section code 7")
            //forever will keep your test running
            .forever().on(
                    exec(getAllVideogames)
                            .pause(5)
                            .exec(getSpecificGame)
                            .pause(5)
                            .exec(getAllVideogames)
            );


    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                            /* started 5 user in start
                              atOnceUsers(5),
                            */

                            /* ramp up 10 users in 20sec
                             rampUsers(10).during(20)
                            */

                            /* every 5 sec it starts 5 more users
                            constantUsersPerSec(5).during(10)
                            */

                            /* same thing but in more realistic
                             constantUsersPerSec(5).during(10).randomized()
                             */

                            /* random increase 1 to 5 in 30 sec and randomized will make in more realistic
                            rampUsersPerSec(1).to(5).during(20).randomized()
                              */

                        //atOnceUsers(5),
                        rampUsers(USER_COUNT).during(RAMP_DURATION)
                ).protocols(httpProtocol)
                //maxDuration will break forever and stop it in 60 seconds
        ).maxDuration(TEST_DURATION);

    }

}