package com.bhavani.cicd.dockerize.service;

import com.bhavani.cicd.dockerize.domain.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackendService {
    private final List<Student> students;

    public BackendService(final List<Student> students) {
        this.students=new ArrayList<>();
    }

    public Student getStudentByName(final String name) {
        return getStudents().stream().filter(student -> student.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Student> getStudents() {
        return students;
    }
}
