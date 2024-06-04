package api;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private static final String BASE_URL = "http://jsonplaceholder.typicode.com";

    public static Response getAllUsers() {
        return given()
                .get(BASE_URL + "/users");
    }

    public static Response getUserTodos(int userId) {
        return given()
                .pathParam("userId", userId)
                .get(BASE_URL + "/todos?userId={userId}");
    }

    public static class UserValidation {
        public static boolean isUserFromFanCodeCity(Response userResponse) {
            double latitude = userResponse.jsonPath().getDouble("address.geo.lat");
            double longitude = userResponse.jsonPath().getDouble("address.geo.lng");
            return latitude >= -40 && latitude <= 5 && longitude >= 5 && longitude <= 100;
        }

        public static boolean isTaskCompletionGreaterThan50Percent(Response todosResponse) {
            List<Boolean> completedList = todosResponse.jsonPath().getList("completed");
            long completedTasks = completedList.stream().filter(Boolean::booleanValue).count();
            return (completedTasks * 100) / completedList.size() > 50;
        }
    }
}
