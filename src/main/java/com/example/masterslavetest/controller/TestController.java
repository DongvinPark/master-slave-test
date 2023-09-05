package com.example.masterslavetest.controller;

import com.example.masterslavetest.persist.TestEntity;
import com.example.masterslavetest.persist.TestEntityRepository;
import com.example.masterslavetest.service.TestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

  private final TestService testService;

  @PostMapping("/create")
  public String create(){
    return testService.create();
  }

  @PostMapping("/update/{id}/{newName}")
  public TestEntity update(
    @RequestParam Long id, @RequestParam String newName
  ){
    return testService.update(id, newName);
  }

  @GetMapping("/read-one")
  public TestEntity read(){
    return testService.readOne();
  }

  @GetMapping("/read-list/{page}")
  public List<TestEntity> readList(
      @RequestParam Integer page
  ){
    return testService.readList(page);
  }

  @DeleteMapping("/delete/{id}")
  public String delete(
      @RequestParam Long id
  ){
    return testService.deleteOne(id);
  }

}
