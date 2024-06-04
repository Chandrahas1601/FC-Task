package stepDefinition;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserValidationSteps {
    private List<Map<String, Object>> todos;
    private boolean isUserFromFanCode;

    @Given("User has the todo tasks")
    public void user_has_the_todo_tasks() {
        Response response = RestAssured.get("http://jsonplaceholder.typicode.com/todos");
        Assert.assertEquals(200, response.getStatusCode());
        todos = response.jsonPath().getList("");
    }

    @Given("User belongs to the city FanCode")
    public void user_belongs_to_the_city_fan_code() {
        Response usersResponse = RestAssured.get("http://jsonplaceholder.typicode.com/users");
        Assert.assertEquals(200, usersResponse.getStatusCode());

        List<Map<String, Object>> users = usersResponse.jsonPath().getList("");
        isUserFromFanCode = users.stream().anyMatch(user -> {
            Map<String, Object> address = (Map<String, Object>) user.get("address");
            Map<String, String> geo = (Map<String, String>) address.get("geo");
            double lat = Double.parseDouble(geo.get("lat"));
            double lng = Double.parseDouble(geo.get("lng"));
            return lat >= -40 && lat <= 5 && lng >= 5 && lng <= 100;
        });

        Assert.assertTrue("No user from FanCode city found", isUserFromFanCode);
    }

    @Then("User Completed task percentage should be greater than {int}%")
    public void user_completed_task_percentage_should_be_greater_than(Integer int1) {
        List<Map<String, Object>> userTodos = todos.stream()
                .filter(todo -> (Boolean) todo.get("completed"))
                .collect(Collectors.toList());

        int totalTasks = todos.size();
        int completedTasks = userTodos.size();
        double completionPercentage = (completedTasks * 100.0) / totalTasks;

        System.out.println("Total tasks: " + totalTasks);
        System.out.println("Completed tasks: " + completedTasks);
        System.out.println("Completion percentage: " + completionPercentage);

        Assert.assertTrue("Completion percentage is less than 50%", completionPercentage > int1);
    }
}
