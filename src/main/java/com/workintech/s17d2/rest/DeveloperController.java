package com.workintech.s17d2.rest;

import com.workintech.s17d2.dto.DeveloperResponse;
import com.workintech.s17d2.model.DeveloperFactory;
import com.workintech.s17d2.model.Experience;
import com.workintech.s17d2.tax.Taxable;
import com.workintech.s17d2.model.Developer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/developers") // localhost:8585/workintech/developers
public class DeveloperController {

    //normalde private yapsak encapsulation için daha iyi olur ancak test dosyası için public yapıyoruz örneğe özel
    public Map<Integer, Developer> developers;
    private Taxable taxable;

    @Autowired
    public DeveloperController(@Qualifier("developerTax") Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        this.developers = new HashMap<>();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperResponse save(@RequestBody Developer developer) {
        Developer createdDeveloper = DeveloperFactory.createDeveloper(developer, taxable);
        if (Objects.nonNull(createdDeveloper)) {
            developers.put(createdDeveloper.getId(), createdDeveloper);
        }
        return new DeveloperResponse(createdDeveloper, HttpStatus.CREATED.value(), "create islemi basarili");
    }

    @GetMapping
    public List<Developer> getAll() {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public DeveloperResponse getById(@PathVariable("id") int id) {
        Developer foundDeveloper = this.developers.get(id);

        if(foundDeveloper == null) {
            return new DeveloperResponse(null, HttpStatus.NOT_FOUND.value(), id + " ile search yapildiginda kayit bulunamadi");
        }
        return new DeveloperResponse(foundDeveloper, HttpStatus.OK.value(), "id ile search basarili");
    }

    @PutMapping("/{id}")
    public DeveloperResponse update(@PathVariable("id") int id, @RequestBody Developer developer) {
        developer.setId(id);
        Developer updatedDeveloper = DeveloperFactory.createDeveloper(developer, taxable);
        this.developers.put(id, updatedDeveloper);

        return new DeveloperResponse(updatedDeveloper, HttpStatus.OK.value(), "update basarili");
    }

    @DeleteMapping("/{id}")
    public DeveloperResponse delete(@PathVariable("id") int id) {
        Developer devToBeDeleted = this.developers.get(id);
        this.developers.remove(id);

        return new DeveloperResponse(devToBeDeleted, HttpStatus.NO_CONTENT.value(), "silme basarili");
    }

}
