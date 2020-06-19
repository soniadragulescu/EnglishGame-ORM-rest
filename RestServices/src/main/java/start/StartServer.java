package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("games")
@ComponentScan("repos")
@SpringBootApplication
public class StartServer {
    public static void main(String[] args){
        SpringApplication.run(StartServer.class,args);
    }
}
