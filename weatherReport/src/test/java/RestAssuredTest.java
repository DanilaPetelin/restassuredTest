import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;

/** набор тестов для тестирования
 * с исполльзованием фреймворка RestAssured
 */
class RestAssuredTest {

    /** Поле адрес запроса к API openweathermap.org
     *  city  - город в качестве параметра
     */
    private static final String basePath =
        "https://api.openweathermap.org/data/2.5/weather?q={city}&appid=dfc1b58e7ecdfba571b36a9152292668&units=metric";

    /** тест проверяет состветствие города в запросе и ответе (London)
     *  выводит body ответа
     */
    @Test
    void CityNameTest() {
        when().get(basePath,"london")
                .then().statusCode(200)
                .and()
                .assertThat().body("name", equalTo("London"))
                .log().body();
    }

    /** тест проверяет состветствие одного из  вложеных полей ожидаемому
     *  значение "country" лежащее в "sys": {... ,"country": "GB" , ...} должно быть "GB"
     */
    @Test
    void CountryNameTest() {
        when().get(basePath,"london")
                .then().statusCode(200)
                .and()
                .assertThat().body("sys.country", equalTo("GB"));
    }

    /** тест проверяет что в ответе по запросу несуществующего города придет код "404"
     * а тажке что в поле "message" будет значение "city not found"
     */
    @Test
    void WrongCityTest() {
        when().get(basePath,"lndon")
                .then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("city not found" ));
    }

    /** тест проверяет соответсвие JSON структуры ответа структуре описанной в weather-schema.json
     */
    @Test
    void JsonValidTest() {
        when().
                get(basePath,"london")
                .then().statusCode(200)
                .and()
                .assertThat().body(matchesJsonSchemaInClasspath("weather-schema.json"));
    }

    /** тест проверяет  несоответсвие JSON структуры ответа неправильной структуре weather-wrong-schema.json
     */
    @Test
    void JsonNotValidTest() {
        when().
                get(basePath, "london")
                .then()
                .assertThat()
                .body(not(matchesJsonSchemaInClasspath("weather-wrong-schema.json")));
    }

    /** тест проверяет что запрос выполняется  быстрее чем через  за 2000мс
     */
    @Test
    void TimeoutTest() {
        when().
                get(basePath, "london").
                then().
                time(lessThan(2000L));
    }
}