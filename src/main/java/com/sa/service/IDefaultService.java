package com.sa.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDefaultService<T> {

    ResponseEntity<List<T>>  getAll();

    ResponseEntity<T> getById(Long id);

    ResponseEntity<List<T>> getByName(String name);

    void create(T obj);

    ResponseEntity<T> update(Long id, T obj);

    void deleteById(Long id);

    void deleteAll();
}
