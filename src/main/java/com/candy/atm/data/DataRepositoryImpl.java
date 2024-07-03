package com.candy.atm.data;

import com.candy.atm.dto.CardDto;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

public class DataRepositoryImpl implements DataRepository {
    private static final String USER_DATA_FILE = "src/main/resources/UserData.txt";
    private static final String BLOCKED_CARDS_FILE = "src/main/resources/BlockedCards.txt";
    private static final String PROPERTY_FILE = "src/main/resources/Property.txt";

    private final Map<String, CardDto> cards = new HashMap<>();
    private final Map<String, LocalDateTime> blockedCards = new HashMap<>();
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
            System.out.println("Ошибка во время чтения Property.");
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
            System.out.println("Ошибка во время чтения UserData файла.");
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
            System.out.println("Ошибка во время чтения BlockedCards файла.");
        }
    }

    private void saveUserData() {
        List<String> lines = new ArrayList<>();
        for (CardDto card : cards.values()) {
            lines.add(card.getCardNumber() + " " + card.getPinCode() + " " + card.getBalance());
        }
        saveFileData(USER_DATA_FILE, lines);
    }

    private void saveBlockedCardsData() {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, LocalDateTime> entry : blockedCards.entrySet()) {
            lines.add(entry.getKey() + " " + entry.getValue().toString());
        }
        saveFileData(BLOCKED_CARDS_FILE, lines);
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
    private void loadFileData(String filePath, Consumer<String> lineProcessor) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineProcessor.accept(line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время чтения файла " + filePath);
        }
    }
    private void saveFileData(String filePath, List<String> lines) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время записи в файл " + filePath);
        }
    }
}
