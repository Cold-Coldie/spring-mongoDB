package com.example.springbootmongodb.service;

import com.example.springbootmongodb.exception.TodoCollectionException;
import com.example.springbootmongodb.model.TodoDTO;
import com.example.springbootmongodb.repository.TodoRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<TodoDTO> getAllTodos() {
        List<TodoDTO> todos = todoRepository.findAll();
        if (todos.size() > 0) {
            return todos;
        } else {
           return new ArrayList<TodoDTO>();
        }
    }

    @Override
    public TodoDTO getTodoById(String id) throws TodoCollectionException {
       Optional<TodoDTO> optionalTodo = todoRepository.findById(id);

       if (!optionalTodo.isPresent()) {
           throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
       } else {
           return optionalTodo.get();
       }
    }

    @Override
    public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepository.findByTodo(todo.getTodo());

        if (optionalTodo.isPresent()) {
            throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
        } else {
            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            todo.setUpdatedAt(new Date(System.currentTimeMillis()));

            todoRepository.save(todo);
        }
    }

    @Override
    public TodoDTO updateTodo(TodoDTO todo) throws TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepository.findById(todo.getId());

        if (optionalTodo.isPresent()) {
            TodoDTO todoToSave = optionalTodo.get();

            todoToSave.setTodo(todo.getTodo() != null ? todo.getTodo() : todoToSave.getTodo());
            todoToSave.setDescription(todo.getDescription() != null ? todo.getDescription() : todoToSave.getDescription());
            todoToSave.setCompleted(todo.getCompleted() != null ? todo.getCompleted() : todoToSave.getCompleted());
            todoToSave.setUpdatedAt(new Date(System.currentTimeMillis()));

            todoRepository.save(todoToSave);

            return todoToSave;
        } else {
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(todo.getId()));
        }
    }

    @Override
    public String deleteTodoById(String id) throws TodoCollectionException {
        Optional<TodoDTO> optionalTodo = todoRepository.findById(id);

        if (optionalTodo.isPresent()) {
            todoRepository.deleteById(id);
            return "Todo with id " + id + " has been deleted";
        } else {
            throw  new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }
    }
}
