//package com.ecommerce.project.scripts;
//
//import com.ecommerce.project.model.Category;
//import com.ecommerce.project.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.IntStream;
//
//@Component
//@RequiredArgsConstructor
//public class CategoryDataLoader implements CommandLineRunner {
//
//    private final CategoryRepository categoryRepository;
//
//    @Override
//    public void run(String... args)  {
//
//        if(categoryRepository.count() > 0) {
//            return; // should prevent duplicate inserts at every restart of application.
//        }
//
//        List<Category> categories = IntStream.rangeClosed(1, 100)
//                .mapToObj(i -> new Category("Categories " + i))
//                .toList();
//
//        categoryRepository.saveAll(categories);
//    }
//}
