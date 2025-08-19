package OrderManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "OrderManager")
public class Main {

    //TODO: Refactor Services and use vavr.io when needed
    //TODO: Implement jwt spring security
    //TODO: implement threading if and when needed.
    //TODO: Paging and sorting
    //TODO: AOP
    //TODO: change create endpoints so that if its successful its 201 not 200 and make sure that create methods return nothing.
    //TODO: Update the services so that in update theres two parameters required one is id and the other is the updatedentity.
    //TODO: test The dtos validators and mappers.

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

  }
}
