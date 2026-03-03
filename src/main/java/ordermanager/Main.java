package ordermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ordermanager")
public class Main {

    //TODO: implement threading if and when needed.
    //TODO: Paging and sorting
    //TODO: AOP
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

  }
}
