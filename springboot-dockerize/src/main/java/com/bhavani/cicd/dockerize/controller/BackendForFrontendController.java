package com.bhavani.cicd.dockerize.controller;


import com.bhavani.cicd.dockerize.domain.Student;
import com.bhavani.cicd.dockerize.service.BackendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class BackendForFrontendController {

    private final BackendService backendService;

    public BackendForFrontendController(BackendService backendService) {
        this.backendService = backendService;
    }

    @GetMapping("/api/students")
    public List<Student> getStudents(){
        return backendService.getStudents();
    }

    @GetMapping("/api/students/{name}")
    public Student getStudent(@PathVariable String name){
        return backendService.getStudentByName(name);
    }
}
