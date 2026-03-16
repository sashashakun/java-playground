package com.example.fintech.day2.genericsandwildcards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class GenericRepositoryTest {

    record Asset(String id, String currency, BigDecimal value) implements GenericRepository.Identifiable {
        @Override public String getId() { return id; }
    }

    private GenericRepository<Asset> repo;

    @BeforeEach
    void setUp() { repo = new GenericRepository<>(); }

    @Test
    void saveSingleAsset() {
        var a = new Asset("a1", "USD", new BigDecimal("1000"));
        repo.save(a);
        assertThat(repo.findById("a1")).isPresent().contains(a);
    }

    @Test
    void saveOverwritesExisting() {
        repo.save(new Asset("a1", "USD", new BigDecimal("1000")));
        var updated = new Asset("a1", "USD", new BigDecimal("2000"));
        repo.save(updated);
        assertThat(repo.findById("a1")).contains(updated);
    }

    @Test
    void findByIdMissingReturnsEmpty() {
        assertThat(repo.findById("nope")).isEmpty();
    }

    @Test
    void findAllWithPredicate() {
        repo.save(new Asset("a1", "USD", new BigDecimal("500")));
        repo.save(new Asset("a2", "EUR", new BigDecimal("200")));
        repo.save(new Asset("a3", "USD", new BigDecimal("800")));
        List<Asset> usd = repo.findAll(a -> a.currency().equals("USD"));
        assertThat(usd).hasSize(2).extracting(Asset::id).containsExactlyInAnyOrder("a1", "a3");
    }

    @Test
    void deleteRemovesAsset() {
        repo.save(new Asset("a1", "USD", new BigDecimal("500")));
        boolean removed = repo.delete("a1");
        assertThat(removed).isTrue();
        assertThat(repo.findById("a1")).isEmpty();
    }

    @Test
    void deleteMissingReturnsFalse() {
        assertThat(repo.delete("ghost")).isFalse();
    }
}
