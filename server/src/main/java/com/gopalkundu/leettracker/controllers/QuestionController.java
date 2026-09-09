package com.gopalkundu.leettracker.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gopalkundu.leettracker.entity.Question;
import com.gopalkundu.leettracker.services.QuestionService;

@RestController
@RequestMapping("api/questions/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/bulk")
    public ResponseEntity<HashMap<String, Object>> addQuestions(@RequestBody List<Question> question) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            if (question == null || question.isEmpty()) {
                response.put("success", false);
                response.put("message", "Question list cannot be empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            List<Question> list = questionService.addQuestionsInBulk(question);

            response.put("success", true);
            response.put("Data", list);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
