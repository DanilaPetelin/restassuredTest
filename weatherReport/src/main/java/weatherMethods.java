import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class WeatherMethods {
    /**ключ для доступа к API*/
    private static final String API_KEY="dfc1b58e7ecdfba571b36a9152292668";

    /** метод возвращающий данные о погоде в указанном городе на сегодня или на 5 дней вперед
     * @param city город в котором хотм узнать погоду
     * @param period принимает два значения:
     *    "weather" погода сегодня
     *    "forecast" прогноз за 5 дней
     */
    static String getData(String city, String period) {

        URL basePath = null;
        HttpURLConnection connection = null;
        StringBuilder data = new StringBuilder();

        try {
            basePath = new URL("https://api.openweathermap.org/data/2.5/"+period+"?q="+city+"&appid="+API_KEY+"&units=metric");
            connection = (HttpURLConnection) basePath.openConnection();
            connection.connect();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine())!= null){
                    data.append(line);
                }
            }else{
                System.out.println("fail:"+connection.getResponseMessage()+"  ResponseCode: "+connection.getResponseCode());
                data = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}