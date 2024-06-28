package feeders;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.recorder.internal.bouncycastle.util.Arrays;
import org.apache.commons.lang3.RandomStringUtils;
import scala.collection.convert.JavaCollectionWrappers;

import javax.swing.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VideoGameCustomFeeder extends Simulation {


    // 1 Http Configuration
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");





    public static LocalDate randomDate() {
             int hundredYears = 100* 365;
             return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears));
    }

    private static Iterator<Map<String, Object>> customFeeder =
            Stream.generate((Supplier<Map<String,Object>>) ()  ->
                               {
                                Random rand = new Random();
                                int gameId = rand.nextInt(10-1+1) +1;

                                String gameName = RandomStringUtils.randomAlphanumeric(5) + "-gameName";
                                String releaseDate = randomDate().toString();
                                int reviewScore = rand.nextInt(100);
                                String category = RandomStringUtils.randomAlphanumeric(5)+ "-category";
                                String rating = RandomStringUtils.randomAlphanumeric(5)+ "-rating";

                                HashMap<String, Object> hmap = new HashMap<String, Object>();
                                hmap.put("gameId", gameId);
                                hmap.put("gameName", gameName);
                                hmap.put("releaseDate", releaseDate);
                                hmap.put("reviewScore", reviewScore);
                                hmap.put("category", category);
                                hmap.put("rating", rating);
                                return hmap;
                                }
                             ).iterator();


    private static ChainBuilder authenticate =
            exec(http("Authenticate")
                    .post("/authenticate")
                    .body(StringBody("{\n" +
                            "  \"password\": \"admin\",\n" +
                            "  \"username\": \"admin\"\n" +
                            "}"
                                    )
                          )
                    .check(jmesPath("token").saveAs("jwtToken"))
            );



    private static ChainBuilder createNewGame =
            feed(customFeeder)
                    .exec(http("Create New Game - #{gameName}")
                              .post("/videogame")
                              .header("authorization","Bearer #{jwtToken}")
                              .body(ElFileBody("bodies/newGameTemplate.json")).asJson()
                              .check(bodyString().saveAs("responseBody")))
                    .exec(session -> {
                          System.out.println(session.getString("responseBody"));
                          return session;
                            }
                          );




    // 2 Scenario Definition
    private ScenarioBuilder scn = scenario("Video Game db - Section 6 code")
            .exec(authenticate)
            .repeat(5).on(
                    exec(createNewGame)
                            .pause(1)
            );



    // 3 Load Simulation
    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
