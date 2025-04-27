package org.yagodka.product.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yagodka.product.entity.TypeProduct;
import org.yagodka.product.repository.TypeProductRepository;

import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initTypes(TypeProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<TypeProduct> types = List.of(
                        new TypeProduct("BEER", "ПИВО"),
                        new TypeProduct("SNACK", "ЗАКУСКИ"),
                        new TypeProduct("STRONG_ALCOHOL", "КРЕПКИЙ АЛКОГОЛЬ")
                );
                repository.saveAll(types);
            }
        };
    }
}
