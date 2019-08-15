import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/** набор тестов для тестирования
 * @see WeatherMethods#getData с использованием фреймворка JUnit5
 */
class JUnit5Test {

    final static private String city = "London";
    final static private String country = "GB";
    final static private String period = "weather";;

    /** тест проверяет соответствие города в запросе и ответе (London)
     */
    @Test
    void TestCityFromData() {
        String data= WeatherMethods.getData(city,period);
        String cityActual = JsonPath.parse(data).read("$.name");
        assertEquals(city, cityActual);
    }

    /** тест проверяет соответствие одного из  вложеных полей ожидаемому,
     *  значение "country" лежащее в "sys": {... ,"country": "GB" , ...} должно быть "GB"
     */
    @Test
    void TestCountryFromData() {
        String data= WeatherMethods.getData(city,period);
        String countryActual = JsonPath.parse(data).read("$.sys.country");
        assertEquals(country, countryActual);
    }

    /**тест проверяет появление  NullPointerException при неправильном запросе
     */
    @Test
    void testExpectedException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            String city = "Lndon";
            String data= WeatherMethods.getData(city,period);
        });
    }

    /** тест проверяет что запрос выполняется  быстрее чем через  за 1,5 сек
     */
    @Test
    void timeoutExceeded() {
        assertTimeout(Duration.ofMillis(1500), () -> {
            String data= WeatherMethods.getData(city,period);
            System.out.println(data);
        });
    }
}