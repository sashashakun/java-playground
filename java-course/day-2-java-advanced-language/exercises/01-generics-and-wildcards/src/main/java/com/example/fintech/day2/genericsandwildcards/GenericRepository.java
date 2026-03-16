package com.example.fintech.day2.genericsandwildcards;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Exercise 01 — Generics & Wildcards (Part 1 of 2)
 *
 * A generic in-memory repository that can store any type T.
 * T must be Identifiable — it has a String id() method.
 *
 * Implement the 4 TODO methods.
 */
public class GenericRepository<T extends Identifiable> {

    /** Every item stored here must have a String id. */
    public interface Identifiable {
        String id();
    }

    private final List<T> items = new ArrayList<>();

    /**
     * TODO 1 — Save an item. Replace if same id exists, otherwise add.
     *
     * Steps:
     *   1. Remove any existing item with the same id
     *   2. Add the new item
     */
    public void save(T item) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Find by id, returning Optional.empty() if not found.
     */
    public Optional<T> findById(String id) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Find all items matching a predicate.
     *
     * Example:
     *   repo.findAll(asset -> asset.currency().equals("USD"))
     */
    public List<T> findAll(Predicate<T> predicate) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Delete by id. Returns true if an item was removed, false if not found.
     */
    public boolean delete(String id) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int size() { return items.size(); }
    public List<T> all() { return List.copyOf(items); }
}
