package org.example;

import org.example.domain.Student;
import org.example.repository.StudentMysqlRepository;
import org.example.repository.StudentRepository;

import java.util.List;

public class Main {


    public static void main(String[] args) {
//        Створити клас студент і за допомогою джава зберігати інформацію про
//        студента в базі даних та прочитати її.

        StudentRepository studentRepository = new StudentMysqlRepository();
        Student s1 = Student.builder()
                .name("John")
                .age(22)
                .groupId(1)
                .build();
        studentRepository.save(s1);
        List<Student> students = studentRepository.findAll();
        System.out.println(students);


//        Extra task
//        Student student = studentRepository.findById(1);
//        System.out.println(student);
    }
}