import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class weatherMethods {

    static String getData(String city, String period) {
        final String API_KEY="dfc1b58e7ecdfba571b36a9152292668";
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
                System.out.println("fail:"+connection.getResponseMessage()+connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}