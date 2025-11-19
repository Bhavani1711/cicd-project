package com.bhavani.cicd.dockerize;

import com.bhavani.cicd.dockerize.domain.Student;
import com.bhavani.cicd.dockerize.service.BackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootDockerizeApplication implements CommandLineRunner {

    @Autowired
    BackendService backendService;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDockerizeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Student student1 = new Student();
        student1.setId("Id1");
        student1.setName("Name1");
        student1.setGender("Female");
        student1.setCourseName("IOT");
        Student student2 = new Student();
        student2.setId("Id2");
        student2.setName("Name2");
        student2.setGender("Male");
        student2.setCourseName("IOT");
        Student student3 = new Student();
        student3.setId("Id3");
        student3.setName("Name3");
        student3.setGender("Male");
        student3.setCourseName("IOT");
        Student student4 = new Student();
        student4.setId("Id4");
        student4.setName("Name4");
        student4.setGender("Female");
        student4.setCourseName("IOT");
        Student student5 = new Student();
        student5.setId("Id5");
        student5.setName("Name5");
        student5.setGender("Male");
        student5.setCourseName("IOT");
        backendService.getStudents().add(student1);
        backendService.getStudents().add(student2);
        backendService.getStudents().add(student3);
        backendService.getStudents().add(student4);
        backendService.getStudents().add(student5);
    }

}
