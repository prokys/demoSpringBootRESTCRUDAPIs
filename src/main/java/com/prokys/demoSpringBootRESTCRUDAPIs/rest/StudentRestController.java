package com.prokys.demoSpringBootRESTCRUDAPIs.rest;

import com.prokys.demoSpringBootRESTCRUDAPIs.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class StudentRestController {

    List<Student> theStudents = new ArrayList<>();

    // define @PostConstruct
    @PostConstruct
    public void loadData(){
        theStudents.add(new Student("Poornima", "Patell"));
        theStudents.add(new Student("Mario", "Rosii"));
        theStudents.add(new Student("Mary", "Smith"));
    }


    //define endpoint for "/students" - return list of students
    @GetMapping("/students")
    public List<Student> getStudents(){

        return theStudents;
    }

    // define endpoint for "/students/{studentId}" - return student at id
    @GetMapping("/students/{studentID}")
    public Student getStudentByID(@PathVariable int studentID){

        //check the student id against List size
        if (studentID>= theStudents.size() || studentID<0){
            throw new StudentNotFoundException("Student id not found - " + studentID);
        }

        return theStudents.get(studentID);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc){

        //create a StudentErrorResponse

        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        //return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // add another exception handler for any exception
    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(Exception exc){
        //create a StudentErrorResponse

        StudentErrorResponse error = new StudentErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        //return ResponseEntity

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
