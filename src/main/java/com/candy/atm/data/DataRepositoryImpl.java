package com.candy.atm.data;

import com.candy.atm.dto.CardDto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataRepositoryImpl implements DataRepository {
    private static final String USER_DATA_FILE = "src/main/resources/UserData.txt";
    private static final String BLOCKED_CARDS_FILE = "src/main/resources/BlockedCards.txt";
    private static final String PROPERTY_FILE = "src/main/resources/Property.txt";
    private Map<String, CardDto> users = new HashMap<>();
    private Set<String> blockedCards = new HashSet<>();
    public int maxPinAttempts;
    public double maxAmountOfDeposit;

    public DataRepositoryImpl(){
        getProperty();
        getUserData();
        getBlockedUserData();
    }
    private void getUserData() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    String cardNumber = parts[0];
                    int pinCode = Integer.parseInt(parts[1]);
                    double balance = Double.parseDouble(parts[2]);
                    users.put(cardNumber, new CardDto(cardNumber, pinCode, balance));
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading database file");
        }
    }

    private void getBlockedUserData(){
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(BLOCKED_CARDS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                blockedCards.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error while reading blocked cards database file");
        }
    }
    private void getProperty(){
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PROPERTY_FILE))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    int maxPinAttempts = Integer.parseInt(parts[0]);
                    double maxAmountOfDeposit = Double.parseDouble(parts[1]);
                    this.maxPinAttempts = maxPinAttempts;
                    this.maxAmountOfDeposit = maxAmountOfDeposit;
                }
            }

        }catch (IOException e){
            System.out.println("Error while reading property file");
        }


    }
    private void saveUserData() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(USER_DATA_FILE))) {
            for (CardDto user : users.values()) {
                writer.write(user.getCardNumber() + " " + user.getPinCode() + " " + user.getBalance());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBlockedCardsData() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(BLOCKED_CARDS_FILE))) {
            for (String cardNumber : blockedCards) {
                writer.write(cardNumber);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CardDto getUserByCardNumber(String cardNumber) {
        return users.get(cardNumber);
    }

    @Override
    public void updateUser(CardDto user) {
        users.put(user.getCardNumber(), user);
        saveUserData();
    }

    @Override
    public boolean isCardBlocked(String cardNumber) {
        return blockedCards.contains(cardNumber);
    }

    @Override
    public void blockCard(String cardNumber) {
        blockedCards.add(cardNumber);
        saveBlockedCardsData();
    }

    @Override
    public void unblockCard(String cardNumber) {
        blockedCards.remove(cardNumber);
        saveBlockedCardsData();
    }
}
