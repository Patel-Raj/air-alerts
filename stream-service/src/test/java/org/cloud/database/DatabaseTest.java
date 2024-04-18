package org.cloud.database;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DatabaseTest {

    @Test
    void fetchLocations() {
//        Map<String, String> envVariables = new HashMap<>();
//        envVariables.put("DB_CONNECTION", "jdbc:mysql://localhost:3306/air-quality-alert-db");
//        envVariables.put("USER_NAME", "root");
//        envVariables.put("PASSWORD", "root1234");
//
//        List<Integer> locations = Database.fetchLocations(envVariables, new LambdaLogger() {
//            @Override
//            public void log(String message) {
//                System.out.println(message);
//            }
//
//            @Override
//            public void log(byte[] message) {
//                System.out.println(message);
//            }
//        });
//
//        Assertions.assertEquals(1, locations.size());
    }

    @Test
    void createTable() {

//        Map<String, String> envVariables = new HashMap<>();
//        envVariables.put("DB_CONNECTION", "jdbc:mysql://localhost:3306/air-quality-alert-db");
//        envVariables.put("USER_NAME", "root");
//        envVariables.put("PASSWORD", "root1234");
//
//        Database.createTable(envVariables, new LambdaLogger() {
//            @Override
//            public void log(String message) {
//                System.out.println(message);
//            }
//
//            @Override
//            public void log(byte[] message) {
//                System.out.println(message);
//            }
//        });

    }

    @Test
    void updateSubscription() throws SQLException {

//        Map<String, String> envVariables = new HashMap<>();
//        envVariables.put("DB_CONNECTION", "jdbc:mysql://localhost:3306/air-quality-alert-db");
//        envVariables.put("USER_NAME", "root");
//        envVariables.put("PASSWORD", "root1234");
//
//        SubscriberRequest subscriberRequest = new SubscriberRequest(StreamEventType.SUBSCRIBE, 456);
//
//        Database.updateSubscription(envVariables, new LambdaLogger() {
//            @Override
//            public void log(String message) {
//                System.out.println(message);
//            }
//
//            @Override
//            public void log(byte[] message) {
//                System.out.println(message);
//            }
//        }, subscriberRequest);
    }
}