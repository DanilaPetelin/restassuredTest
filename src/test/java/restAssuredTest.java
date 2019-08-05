
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;

public class restAssuredTest {
    // POST запрос к API openweathermap.org
    private static final String basePath = "https://api.openweathermap.org/data/2.5/weather?q=london&appid=dfc1b58e7ecdfba571b36a9152292668&units=metric";

    // POST запрос к API openweathermap.org усли хотим передавать город в качестве параметра
    private static final String basePathCity = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid=dfc1b58e7ecdfba571b36a9152292668&units=metric";

    @Test
    // проверяем что в ответе по нашему запросу действительно London
    // и еще проверяем значение "country" лежащее в "sys": {... ,"country": "GB" , ...} должно быть "GB"
    //выводим body ответа
    public void firstTest() {
        when().get(basePathCity,"london")
                .then().statusCode(200)
                .and()
                .assertThat().body("name", equalTo("London"))
                .and()
                .assertThat().body("sys.country", equalTo("GB"))
                .log().body();
    }

    @Test
    // проверяем что в ответе по запросу несуществующего города придет "404"
    public void secondTest() {
        when().get(basePathCity,"lndon")
              .then().statusCode(404);
    }

    @Test
    // проверка ответа на соответсвие json структуре описанной в weather-schema.json
    public void thirdTest() {
        when().
        get(basePath)
                .then().statusCode(200)
                .and()
                .assertThat().body(matchesJsonSchemaInClasspath("weather-schema.json"));
    }

    @Test
    // проверка ответа на соответсвие НЕПРАВИЛЬНОЙ структуре weather-wrong-schema.json
    // должно быть "name": {"type": "string"}, а у нас "name": {"type": "number"}
    //тест закончится неудачей
    public void fourthTest() {
        when().
                get(basePath)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("weather-wrong-schema.json"));
    }

    @Test
    //проверяем что запрос приходит быстрее чем через 2000мс
    public void fifthTest() {
        when().
                get(basePath).
                then().
                time(lessThan(2000L));
    }
}