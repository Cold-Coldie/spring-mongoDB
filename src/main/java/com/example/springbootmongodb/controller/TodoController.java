package com.example.springbootmongodb.controller;

import com.example.springbootmongodb.exception.TodoCollectionException;
import com.example.springbootmongodb.model.TodoDTO;
import com.example.springbootmongodb.repository.TodoRepository;
import com.example.springbootmongodb.service.TodoService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @GetMapping("/todo")
    public ResponseEntity<List<TodoDTO>> getAllTodos() {
        List<TodoDTO> todos = todoService.getAllTodos();

        return new ResponseEntity<List<TodoDTO>>(todos, HttpStatus.OK);
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable("id") String id) {
        try {
            TodoDTO todo = todoService.getTodoById(id);
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/todo")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo) {
        try {
            todoService.createTodo(todo);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.CREATED);
        } catch (ConstraintViolationException | TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/todo")
    public ResponseEntity<?> updateTodoById(@RequestBody TodoDTO todo) {
        try {
            if (todo.getId() == null) {
                return new ResponseEntity<String>("Provide a valid \"id\" in the request body", HttpStatus.BAD_REQUEST);
            }

            TodoDTO updatedTodo = todoService.updateTodo(todo);
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteToDoById(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<>(todoService.deleteTodoById(id), HttpStatus.OK);
        } catch (TodoCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
