package com.example.springbootmongodb.service;

import com.example.springbootmongodb.exception.TodoCollectionException;
import com.example.springbootmongodb.model.TodoDTO;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface TodoService {

    List<TodoDTO> getAllTodos();

    TodoDTO getTodoById(String id) throws TodoCollectionException;

    void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException;

    TodoDTO updateTodo(TodoDTO todo) throws TodoCollectionException;

    String deleteTodoById(String id) throws TodoCollectionException;
}
