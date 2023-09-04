package com.example.masterslavetest.controller;

import com.example.masterslavetest.persist.TestEntity;
import com.example.masterslavetest.persist.TestEntityRepository;
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

  private final TestEntityRepository testEntityRepository;

  @PostMapping("/create")
  public String create(){

    testEntityRepository.save(
        TestEntity.builder()
            .name("default name")
            .build()
    );

    return "saved!";
  }

  @PostMapping("/update/{id}/{newName}")
  public TestEntity update(
    @RequestParam Long id, @RequestParam String newName
  ){
    TestEntity testEntity = testEntityRepository.findById(id).orElseThrow(
        () -> new RuntimeException("엔티티 찾지 못함!!")
    );

    testEntity.updateName(newName);

    // 이렇게 리턴할 경우 실제로도 수정 완료된 엔티티가 리턴될 것인가?
    return testEntity;
  }

  @GetMapping("/read-one")
  public TestEntity read(){

    // 전체 디비 개수 중 임의로 하나 뽑아서 리턴.

    return null;
  }

  @GetMapping("/read-list/{page}")
  public List<TestEntity> readList(
      @RequestParam Integer page
  ){
    return testEntityRepository.getTestEntityList(
        PageRequest.of(page, 20)
    ).getContent();
  }

  @DeleteMapping("/delete/{id}")
  public String delete(
      @RequestParam Long id
  ){
    testEntityRepository.deleteById(id);

    return "delete success";
  }

}
