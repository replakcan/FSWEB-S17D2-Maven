package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers;
    private Taxable taxable;

    @PostConstruct
    public Map<Integer, Developer> init() {
        developers = new HashMap<>();
        return developers;
    }

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @GetMapping
    public List<Developer> findAll() {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer findById(@PathVariable("id")Integer id) {
        return developers.get(id);
    }

    @PostMapping
    public Developer create(@RequestBody Developer developer) {
        switch (developer.getExperience()) {
            case JUNIOR:
                developer.setSalary(developer.getSalary() - taxable.getSimpleTaxRate());
                break;
            case MID:
                developer.setSalary(developer.getSalary() - taxable.getMiddleTaxRate());
                break;
            case SENIOR:
                developer.setSalary(developer.getSalary() - taxable.getUpperTaxRate());
                break;
            default:
                System.out.println("Nothins else matters");
                break;
        }


        developers.put(developer.getId(), developer);
        return developer;
    }

    @PutMapping("/{id}")
    public Developer update(@PathVariable("id") Integer id, @RequestBody Developer developer) {
        developers.put(id, developer);
        return developer;
    }

    @DeleteMapping("/{id}")
    public Developer removeById(@PathVariable("id") Integer id) {
        Developer developer = developers.get(id);
        developers.remove(id);
        return developer;
    }
}
