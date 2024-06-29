package com.candy.atm.data;

import com.candy.atm.dto.CardDto;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class DataRepositoryImpl implements DataRepository {
    private static final String USER_DATA_FILE = "src/main/resources/UserData.txt";
    private static final String BLOCKED_CARDS_FILE = "src/main/resources/BlockedCards.txt";
    private static final String PROPERTY_FILE = "src/main/resources/Property.txt";

    private Map<String, CardDto> cards = new HashMap<>();
    private Map<String, LocalDateTime> blockedCards = new HashMap<>();
    private int maxAttempts;
    private double maxDepositAmount;

    public DataRepositoryImpl() {
        loadProperties();
        loadUserData();
        loadBlockedCardsData();
    }

    private void loadProperties() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PROPERTY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "maxAttempts":
                            maxAttempts = Integer.parseInt(value);
                            break;
                        case "maxDepositAmount":
                            maxDepositAmount = Double.parseDouble(value);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Устанавливаем значения по умолчанию в случае ошибки чтения файла
            maxAttempts = 3;
            maxDepositAmount = 1000000;
        }
    }

    private void loadUserData() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    String cardNumber = parts[0];
                    int pinCode = Integer.parseInt(parts[1]);
                    double balance = Double.parseDouble(parts[2]);
                    cards.put(cardNumber, new CardDto(cardNumber, pinCode, balance));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlockedCardsData() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(BLOCKED_CARDS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String cardNumber = parts[0];
                    LocalDateTime unblockTime = LocalDateTime.parse(parts[1]);
                    blockedCards.put(cardNumber, unblockTime);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(USER_DATA_FILE))) {
            for (CardDto card : cards.values()) {
                writer.write(card.getCardNumber() + " " + card.getPinCode() + " " + card.getBalance());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBlockedCardsData() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(BLOCKED_CARDS_FILE))) {
            for (Map.Entry<String, LocalDateTime> entry : blockedCards.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CardDto getCardByNumber(String cardNumber) {
        return cards.get(cardNumber);
    }

    @Override
    public void updateCard(CardDto card) {
        cards.put(card.getCardNumber(), card);
        saveUserData();
    }

    @Override
    public boolean isCardBlocked(String cardNumber) {
        LocalDateTime unblockTime = blockedCards.get(cardNumber);
        if (unblockTime != null) {
            if (LocalDateTime.now().isAfter(unblockTime)) {
                unblockCard(cardNumber);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void blockCard(String cardNumber) {
        blockedCards.put(cardNumber, LocalDateTime.now().plusHours(24));
        saveBlockedCardsData();
    }

    @Override
    public void unblockCard(String cardNumber) {
        blockedCards.remove(cardNumber);
        saveBlockedCardsData();
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public double getMaxDepositAmount() {
        return maxDepositAmount;
    }
}
