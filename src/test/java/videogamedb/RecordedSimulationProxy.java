package videogamedb;

import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class RecordedSimulationProxy extends Simulation {

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("https://videogamedb.uk")
    .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*"))
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("PostmanRuntime/7.39.0");
  
  private Map<CharSequence, String> headers_0 = Map.of("Postman-Token", "faf039b1-adaa-4eac-8deb-8aef34431aee");
  
  private Map<CharSequence, String> headers_1 = Map.of("Postman-Token", "5b0a6308-1577-4a23-8983-cc1298b74ee2");
  
  private Map<CharSequence, String> headers_2 = Map.ofEntries(
    Map.entry("Content-Type", "application/json"),
    Map.entry("Postman-Token", "7e33fc8e-e0a7-4361-bc28-6085c5ce7766")
  );
  
  private Map<CharSequence, String> headers_3 = Map.ofEntries(
    Map.entry("Content-Type", "application/json"),
    Map.entry("Postman-Token", "1a1dfdce-d193-4cf2-8bfe-e9af03a538ae"),
    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxOTI0NTU0MywiZXhwIjoxNzE5MjQ5MTQzfQ.86zfltMwK3G1IH9PjhceKVoxpQgwsxaVdB3kqnNow0w")
  );
  
  private Map<CharSequence, String> headers_4 = Map.ofEntries(
    Map.entry("Content-Type", "application/json"),
    Map.entry("Postman-Token", "50f78f73-e51b-424c-b7a6-676753ea2a76"),
    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxOTI0NTU0MywiZXhwIjoxNzE5MjQ5MTQzfQ.86zfltMwK3G1IH9PjhceKVoxpQgwsxaVdB3kqnNow0w")
  );
  
  private Map<CharSequence, String> headers_5 = Map.ofEntries(
    Map.entry("Postman-Token", "c1d26281-a8c2-41b3-b230-20e0857d5ddb"),
    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxOTI0NTU0MywiZXhwIjoxNzE5MjQ5MTQzfQ.86zfltMwK3G1IH9PjhceKVoxpQgwsxaVdB3kqnNow0w")
  );


  private ScenarioBuilder scn = scenario("RecordedSimulationProxy")
    .exec(
      http("request_0")
        .get("/api/videogame")
        .headers(headers_0)
    )
    .pause(12)
    .exec(
      http("request_1")
        .get("/api/videogame/2")
        .headers(headers_1)
    )
    .pause(5)
    .exec(
      http("request_2")
        .post("/api/authenticate")
        .headers(headers_2)
        .body(RawFileBody("videogamedb/recordedsimulationproxy/0002_request.json"))
    )
    .pause(53)
    .exec(
      http("request_3")
        .post("/api/videogame")
        .headers(headers_3)
        .body(RawFileBody("videogamedb/recordedsimulationproxy/0003_request.json"))
    )
    .pause(15)
    .exec(
      http("request_4")
        .put("/api/videogame/3")
        .headers(headers_4)
        .body(RawFileBody("videogamedb/recordedsimulationproxy/0004_request.json"))
    )
    .pause(6)
    .exec(
      http("request_5")
        .delete("/api/videogame/2")
        .headers(headers_5)
    );

  {
	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}
