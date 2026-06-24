// SearchController is no longer needed — search is handled client-side via /api/dogs/search
// package com.dogstore.dogsellingapp.controller;
//
// import com.dogstore.dogsellingapp.service.DogService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
//
// @Controller
// @RequiredArgsConstructor
// public class SearchController {
//
//     private final DogService dogService;
//
//     @GetMapping("/search")
//     public String search(@RequestParam(name = "q", required = false) String query, Model model) {
//         model.addAttribute("query", query);
//         model.addAttribute("results", dogService.searchDogs(query));
//         return "search-results";
//     }
// }
