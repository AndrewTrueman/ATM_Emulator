package com.candy.atm.data;

import com.candy.atm.dto.CardDto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DataRepositoryImpl implements DataRepository {
    private static final String USER_DATA_FILE = "src/main/resources/UserData.txt";
    private static final String BLOCKED_CARDS_FILE = "src/main/resources/BlockedCards.txt";
    private static final String PROPERTY_FILE = "src/main/resources/Property.txt";
    private static final String PROPERTY_MAX_ATTEMPTS = "max-attempts";
    private static final String PROPERTY_MAX_DEPOSIT_AMOUNT = "max-deposit-amount";
    private static final String PROPERTY_ATM_LIMIT = "atm-limit";

    private final Map<String, CardDto> cards = new HashMap<>();
    private final Map<String, LocalDateTime> blockedCards = new HashMap<>();
    private final Map<String, String> properties = new HashMap<>();

    public DataRepositoryImpl() {
        loadProperties();
        loadUserCards();
        loadBlockedCardsData();
    }

    @Override
    public CardDto getCardByNumber(String cardNumber) {
        CardDto card = cards.get(cardNumber);
        if (card == null) {
            throw new RuntimeException("Карта не найдена.");
        }
        return card;
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

    @Override
    public int getMaxAttempts() {
        String property = properties.putIfAbsent(PROPERTY_MAX_ATTEMPTS, "3");
        return Integer.parseInt(property);
    }

    @Override
    public double getMaxDepositAmount() {
        String property = properties.putIfAbsent(PROPERTY_MAX_DEPOSIT_AMOUNT, "1000000");
        return Double.parseDouble(property);
    }

    @Override
    public double getAtmLimit() {
        String property = properties.putIfAbsent(PROPERTY_ATM_LIMIT, "100000");
        return Double.parseDouble(property);
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

    private void loadProperties() {
        loadFileData(PROPERTY_FILE, line -> {
            String[] parts = line.split("=");
            if (parts.length == 2) {
                properties.put(parts[0].trim(), parts[1].trim());
            }
        });

    }

    private void loadUserCards() {
        loadFileData(USER_DATA_FILE, line -> {
            String[] parts = line.split(" ");
            if (parts.length == 3) {
                String cardNumber = parts[0];
                int pinCode = Integer.parseInt(parts[1]);
                double balance = Double.parseDouble(parts[2]);
                cards.put(cardNumber, new CardDto(cardNumber, pinCode, balance));
            }
        });
    }

    private void loadBlockedCardsData() {
        loadFileData(BLOCKED_CARDS_FILE, line -> {
            String[] parts = line.split(" ");
            if (parts.length == 2) {
                String cardNumber = parts[0];
                LocalDateTime unblockTime = LocalDateTime.parse(parts[1]);
                blockedCards.put(cardNumber, unblockTime);
            }
        });
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


}
