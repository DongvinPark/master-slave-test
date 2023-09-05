package com.example.masterslavetest.service;

import com.example.masterslavetest.persist.TestEntity;
import com.example.masterslavetest.persist.TestEntityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

  private final TestEntityRepository testEntityRepository;

  public String create(){
    testEntityRepository.save(
        TestEntity.builder()
            .name("default name")
            .build()
    );

    return "saved!";
  }

  public TestEntity update(Long id, String newName){
    TestEntity testEntity = testEntityRepository.findById(id).orElseThrow(
        () -> new RuntimeException("엔티티 찾지 못함!!")
    );

    testEntity.updateName(newName);

    // 이렇게 리턴할 경우 실제로도 수정 완료된 엔티티가 리턴될 것인가?
    return testEntity;
  }

  @Transactional(readOnly = true)
  public TestEntity readOne(){
    return testEntityRepository.findById(1L).orElseThrow(
        () -> new RuntimeException("첫 번째 엔티티 찾지 못함!!")
    );
  }

  @Transactional(readOnly = true)
  public List<TestEntity> readList(Integer page){
    return testEntityRepository.getTestEntityList(
        PageRequest.of(page, 20)
    ).getContent();
  }

  public String deleteOne(Long id){
    testEntityRepository.deleteById(id);
    return "delete success";
  }

}
