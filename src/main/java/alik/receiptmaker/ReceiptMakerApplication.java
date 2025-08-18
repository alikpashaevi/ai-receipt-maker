package alik.receiptmaker;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReceiptMakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiptMakerApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(ChatClient.Builder builder) {
//        return (String... args) -> {
//            var client = builder.build();
//            String response = client.prompt("Tell me an interesting fact about google")
//                    .call()
//                    .content();
//
//            System.out.println(response);
//        };
//    }

}
