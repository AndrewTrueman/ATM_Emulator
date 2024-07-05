# ATM Simulation Application

## Overview
This ATM Simulation Application is a simple console-based program that simulates the basic operations of an ATM (Automated Teller Machine). The application supports operations such as viewing balance, depositing funds, withdrawing funds, and card authorization with a PIN code. The application also handles card blocking after multiple incorrect PIN attempts and uses a file-based data storage mechanism.

## Features
- **Card Authorization:** Validate card number and PIN code.
- **Balance Inquiry:** Check the current balance of the card.
- **Deposit Funds:** Add money to the card.
- **Withdraw Funds:** Withdraw money from the card within ATM and card limits.
- **Card Blocking:** Block the card for 24 hours after three incorrect PIN attempts.
- **Persistent Storage:** Store card data, blocked cards, and application properties in text files.
- **Property Management:** Manage properties like max PIN attempts, max deposit amount, and ATM limit via a property file.

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven (optional, for building with Maven)

### Compilation and Execution

#### Using Command Line

1. **Compile the Java files:**
    ```sh
    javac -d out src/main/java/com/candy/atm/**/*.java
    ```

2. **Create the JAR file:**
    ```sh
    jar cfm AtmApp.jar src/main/resources/MANIFEST.MF -C out/ .
    ```

3. **Add resources to the JAR file:**
    ```sh
    jar uf AtmApp.jar -C src/main/resources/ .
    ```

4. **Run the application:**
    ```sh
    java -jar AtmApp.jar
    ```

## Configuration
The application properties are stored in the `Property.txt` file. The following properties can be configured:
- `max-attempts`: Maximum number of PIN attempts before the card is blocked (default: 3).
- `max-deposit-amount`: Maximum amount that can be deposited in a single transaction (default: 1,000,000).
- `atm-limit`: Maximum amount that can be withdrawn in a single transaction (default: 100,000).

Example `Property.txt`:

**max-attempts=3
**max-deposit-amount=1000000
**atm-limit=100000


## Exception Handling
- The application gracefully handles invalid inputs and provides appropriate error messages.
- Cards are blocked for 24 hours after 3 incorrect PIN attempts.
- If an invalid card number is entered, the user is prompted to re-enter the card number.


