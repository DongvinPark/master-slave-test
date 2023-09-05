package com.example.masterslavetest.config;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

    if(!isReadOnly){
      System.out.println("!!! MASTER !!!");
      return "master";
    } else {
      if(ThreadLocalRandom.current().nextInt(2) % 2 == 0){
        System.out.println("!!! SLAVE 1 !!!");
        return "slave1";
      } else {
        System.out.println("!!! SLAVE 2 !!!");
        return "slave2";
      }
    }
  }

}
