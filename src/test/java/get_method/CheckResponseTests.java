package get_method;

import base_url.GiantBombBaseURL;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import utilities.ConfigReader;

import java.util.List;

import static io.restassured.RestAssured.given;

public class CheckResponseTests extends GiantBombBaseURL {

    /*
    1-) Send GET request:
        With path params:
            first  = api
            second = characters
        With query params:
            api_key = ec6f268fecd603c53475a9565a56c0b1e8e12c9a
            format  = json
            limit   = 10

    2-) THEN
        a. Validate that each id is unique in "results"
    */

    @Test
    public void givenUserSendsGetRequestThenValidateEachIdIsUniqueInResults() {

        // Set the URL
        spec.pathParams("first", "api",
                "second", "characters")
                .queryParams("api_key", ConfigReader.getProperty("api_key"),
                        "format", ConfigReader.getProperty("format"),
                        "limit", ConfigReader.getProperty("limit"))
                .headers("user-agent", ConfigReader.getProperty("userAgent"));

        // Send the request
        Response response = given().spec(spec).when().get("/{first}/{second}");
        JsonPath json = response.jsonPath();
        response.then().assertThat().statusCode(200);

        // Validate that each id is unique in "results"
        List<String> idList = json.getList("results.id");
        Boolean isAllIdsUnique = idList.stream().distinct().toList().size() == idList.size();
        if(isAllIdsUnique) System.out.println("All ids in 'results' is unique");
        Assert.assertEquals(isAllIdsUnique, true);
    }
}