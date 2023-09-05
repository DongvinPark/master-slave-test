package com.example.masterslavetest.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {

  // 성능을 고려해서 Concurrent Hash Map을 써보자.
  private final ConcurrentHashMap<String, Integer> roundRobinStatusMap;

  @Value("${NUMBER_OF_DB_INSTANCE}")
  private Integer numberOfDBInstances;

  public RoutingDataSource() {
    roundRobinStatusMap = new ConcurrentHashMap<>();
    roundRobinStatusMap.put("status", 0);
  }

  @Override
  protected Object determineCurrentLookupKey() {
    boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

    if(!isReadOnly){
      System.out.println("!!! MASTER !!!");
      return "masterDataSource";
    } else {
      /*
      안정적이고 빠른 라우팅을 위해서 테스트 해본 결과들을 정리한다.
      로컬 자바 앱에서 HttpURLConnection 객체를 이용해서 100번의 테스트엔티티 읽기 요청을 했을때,
      걸린 시간이다.
      
     // 아예 라우팅 로직을 쓰지 않고 1개 슬레이브에만 일기요청 몰빵 : 1837~2716 밀리초
    // 라우팅로직에 아토믹인티저 쓰고 나머지 연산 : 24330 밀리초
    // 스레드로컬랜덤 임의 난수로 결정 : 1666 밀리초
    // ConcurrentHashMap : 1638~2372 밀리초
    // 역시 ConcurrentHashMap이 좋다.

      * */
      if(
          roundRobinStatusMap.get("status") % numberOfDBInstances == 0
      ) {
        System.out.println("!!! SLAVE 1 !!!");
        roundRobinStatusMap.put("status", 1);
        return "slave1DataSource";
      } else {
        System.out.println("!!! SLAVE 2 !!!");
        roundRobinStatusMap.put("status", 0);
        return "slave2DataSource";
      }
    }
  }

}
