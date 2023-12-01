package com.sa.service;

import java.util.List;

public interface IDefaultService<T> {

    List<T> getAll();

    T getById(Long id);

    List<T> getByName(String name);

    void create(T obj);

    T update(Long id, T obj);

    void deleteById(Long id);

    void deleteAll();
}
