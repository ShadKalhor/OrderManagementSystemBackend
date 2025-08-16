package OrderManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "OrderManager")
public class Main {

    //TODO: Implement dtos in requests and responses
    //TODO: Refactor Services and use vavr.io when needed
    //TODO: Implement jwt spring security
    //TODO: implement threading if and when needed.
    //TODO: Paging and sorting
    //TODO: AOP
    //TODO: change create endpoints so that if its successful its 201 not 200 and make sure that create methods return nothing.

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

  }
}
