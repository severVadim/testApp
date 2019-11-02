package stepDefinitions;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import cucumber.api.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import utils.Credentials;
import utils.UserInfo;
import utils.Util;


import static io.restassured.RestAssured.given;


public class LoginStepDefinition {
    public static final String url = "https://www.sprinklexchange.com/api/v1/user-profile";
    public static final String loginEndPoint = "/login";
    public static final String userInfoEndPoint = "/user";


    private Response response;
    private String token;

    @Given("^host setup$")
    public void hostSetup() throws Throwable {
        response = null;
        token = null;
        RestAssured.baseURI = url;
    }

    @Given("^authorize using the URL with userName: (.*) and password: (.*)")
    public void authorizationUsingAPI(String username, String password) throws JsonProcessingException {
        response = given().header("Content-Type", "application/json").body(new Credentials(username, password)).post(loginEndPoint);
    }

    @Given("^assert that Status is: (.*) and returned JWT token has the same email: (.*)$")
    public void assertThatStatusIsStatusCodeAndReturnedJWTTokenHasTheSameEmailUserName(String status, String username) throws Throwable {
        Response responseLocal = response.then().statusCode(Integer.valueOf(status)).extract().response();
        if (status.equals("200")) {
            ObjectMapper mapper = new ObjectMapper();
            Assert.assertEquals(username, mapper.readTree(Util.decodeJwtToken(responseLocal.path("token").toString())).get("username").asText());
        }
    }

    @Given("^get user info using JWT token from authorization. UserName: (.*), Password: (.*)$")
    public void getUserInfoUsingJWTTokenFromAuthorizationUserNameUserNamePasswordPassword(String username, String password) throws Throwable {
        authorizationUsingAPI(username, password);
        token = response.then().statusCode(200).extract().response().path("token").toString();
    }

    @Given("^map response to an object and assert response model is according model$")
    public void mapResponseToAnObjectAndAssertResponseModelIsAccordingTo() throws Throwable {
        String userInfoJson = given().queryParam("jwt", token).when().get(userInfoEndPoint).then().statusCode(200).extract().response().asString();
        try {
            UserInfo userInfo = new ObjectMapper().readValue(userInfoJson, UserInfo.class);
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }
}
