import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Data {
    public String dataset() {
        try {
            // Build URL with query parameters
            String url = "https://opentdb.com/api.php?amount=10&type=boolean&category=18";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print JSON response
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();// Prints relative exception, Stack Trace means that it'll give the number of line where error occurred
            return "Something went wrong";
        }
    }
}


