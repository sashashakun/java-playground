package com.example.fintech.day2.genericsandwildcards;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class GenericRepository<T extends GenericRepository.Identifiable> {

    public interface Identifiable {
        String getId();
    }

    private final ConcurrentHashMap<String, T> store = new ConcurrentHashMap<>();

    public void save(T entity) {
        store.put(entity.getId(), entity);
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<T> findAll(Predicate<T> predicate) {
        return store.values().stream().filter(predicate).toList();
    }

    public boolean delete(String id) {
        return store.remove(id) != null;
    }
}
