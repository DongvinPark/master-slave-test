package com.example.masterslavetest.persist;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {

  Slice<TestEntity> getTestEntityList(PageRequest pageRequest);

}
