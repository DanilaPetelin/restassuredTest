
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class weatherMethodsTest {

    // проверяем что в ответе по нашему запросу действительно London
    // проверяем что значение "country" лежащее в "sys": {... ,"country": "GB" , ...} "GB"
    @Test
    void TestgetData() {
        String city = "London";
        String country = "GB";
        String period = "weather";
        String data= weatherMethods.getData(city,period);
        String cityActual = JsonPath.parse(data).read("$.name");
        String countryActual = JsonPath.parse(data).read("$.sys.country");
        assertEquals(city, cityActual);
        assertEquals(country, countryActual);
    }

    // проверяем что в ответе по запросу несуществующего города придет "cod":"404"  и сообщение что город не найден.
    @Test
    void TestgetData1() {
        String city = "Lndon";
        String period = "weather";
        String data= weatherMethods.getData(city,period);
        String cod = JsonPath.parse(data).read("$.cod");
        String message = JsonPath.parse(data).read("$.message");
        assertEquals("404", cod);
        assertEquals("city not found", message);
    }

}