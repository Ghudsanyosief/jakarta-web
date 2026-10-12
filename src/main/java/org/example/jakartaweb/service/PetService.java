package org.example.jakartaweb.service;

import org.example.jakartaweb.dto.PetDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@ApplicationScoped
public class PetService {

    private final ConcurrentHashMap<Long, PetDTO> petStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ReentrantLock lock = new ReentrantLock();

    public PetDTO adoptPet(PetDTO pet) {
        long id = idGenerator.getAndIncrement();
        pet.setId(id);
        petStorage.put(id, pet);
        return pet;
    }

    public List<PetDTO> getAllPets() {
        return new ArrayList<>(petStorage.values());
    }

    public PetDTO getPetById(Long id) {
        PetDTO pet = petStorage.get(id);
        if (pet == null) {
            throw new NotFoundException("Could not find any pet with ID: " + id); // <-- PÅ ENGELSKA
        }
        return pet;
    }

    public PetDTO feedPet(Long id) {
        lock.lock();
        try {
            PetDTO pet = getPetById(id);
            int newHunger = Math.max(0, pet.getHungerLevel() - 15);
            pet.setHungerLevel(newHunger);
            return pet;
        } finally {
            lock.unlock();
        }
    }

    public PetDTO playWithPet(Long id) {
        lock.lock();
        try {
            PetDTO pet = getPetById(id);
            int newHappiness = Math.min(100, pet.getHappiness() + 15);
            pet.setHappiness(newHappiness);
            return pet;
        } finally {
            lock.unlock();
        }
    }

    public void releasePet(Long id) {
        if (!petStorage.containsKey(id)) {
            throw new NotFoundException("Could not release pet. No pet found with ID: " + id); // <-- PÅ ENGELSKA
        }
        petStorage.remove(id);
    }
}
