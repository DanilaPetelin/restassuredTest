import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class JUnit5Test {
    final static private String city = "London";
    final static private String country = "GB";
    final static private String period = "weather";;

    // проверяем что в ответе по нашему запросу действительно London
    @Test
    void TestCityFromData() {
        String data= WeatherMethods.getData(city,period);
        String cityActual = JsonPath.parse(data).read("$.name");
        assertEquals(city, cityActual);
    }

    // проверяем что значение "country" лежащее в "sys": {... ,"country": "GB" , ...} "GB"
    @Test
    void TestCountryFromData() {
        String data= WeatherMethods.getData(city,period);
        String countryActual = JsonPath.parse(data).read("$.sys.country");
        assertEquals(country, countryActual);
    }

    //проверка неправильно заданный город приводит к исключению в работе метода getData
    @Test
    void testExpectedException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            String city = "Lndon";
            String data= WeatherMethods.getData(city,period);
        });
    }

    //проверяем что ответ мы получаем быстрее чем за 1,5 сек
    @Test
    void timeoutExceeded() {
        assertTimeout(Duration.ofMillis(1500), () -> {
            String data= WeatherMethods.getData(city,period);
            System.out.println(data);
        });
    }
}