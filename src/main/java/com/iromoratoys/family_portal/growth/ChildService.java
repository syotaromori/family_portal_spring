package com.iromoratoys.family_portal.growth;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChildService {

    private final ChildRepository repo;

    public ChildService(ChildRepository repo) {
        this.repo = repo;
    }

    public List<Child> findAll() {
        return repo.findAll();
    }

    public Child create(ChildRequest req) {
        Child child = new Child();
        child.setName(req.getName());
        child.setBirthDate(req.getBirthDate());

        return repo.save(child);
    }

    public Child update(Long id, ChildUpdateRequest req) {
        Child child = repo.findById(id).orElseThrow();

        child.setName(req.getName());
        child.setBirthDate(req.getBirthDate());

        return repo.save(child);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Child getById(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
