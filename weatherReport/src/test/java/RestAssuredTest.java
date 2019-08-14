import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;

public class RestAssuredTest {
    // POST запрос к API openweathermap.org город в качестве параметра
    private static final String basePath = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid=dfc1b58e7ecdfba571b36a9152292668&units=metric";

    @Test
    // проверяем что в ответе по нашему запросу действительно London
    //выводим body ответа
    public void CityNameTest() {
        when().get(basePath,"london")
                .then().statusCode(200)
                .and()
                .assertThat().body("name", equalTo("London"))
                .log().body();
    }

    @Test
    //Gроверяем значение "country" лежащее в "sys": {... ,"country": "GB" , ...} должно быть "GB"
    public void CountryNameTest() {
        when().get(basePath,"london")
                .then().statusCode(200)
                .and()
                .assertThat().body("sys.country", equalTo("GB"));
    }

    @Test
    // проверяем что в ответе по запросу несуществующего города придет "404"
    public void WrongCityTest() {
        when().get(basePath,"lndon")
                .then().statusCode(404);
    }

    @Test
    // проверка ответа на соответсвие json структуре описанной в weather-schema.json
    public void JsonValidTest() {
        when().
                get(basePath,"london")
                .then().statusCode(200)
                .and()
                .assertThat().body(matchesJsonSchemaInClasspath("weather-schema.json"));
    }

    @Test
    // проверка ответа на соответсвие НЕПРАВИЛЬНОЙ структуре weather-wrong-schema.json
    // должно быть "name": {"type": "string"}, а у нас "name": {"type": "number"}
    //тест закончится неудачей
    public void JsonNotValidTest() {
        when().
                get(basePath, "london")
                .then()
                .assertThat()
                .body(not(matchesJsonSchemaInClasspath("weather-wrong-schema.json")));
    }

    @Test
    //проверяем что запрос приходит быстрее чем через 2000мс
    public void TimeoutTest() {
        when().
                get(basePath, "london").
                then().
                time(lessThan(2000L));
    }
}