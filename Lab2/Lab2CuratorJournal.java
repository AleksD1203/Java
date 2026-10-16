import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Lab2CuratorJournal {

    static class Address {
        private String street;
        private String building;
        private int apartment;

        public Address(String street, String building, int apartment) {
            this.street = street;
            this.building = building;
            this.apartment = apartment;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Address address = (Address) o;
            return apartment == address.apartment
                    && Objects.equals(street, address.street)
                    && Objects.equals(building, address.building);
        }

        @Override
        public int hashCode() {
            return Objects.hash(street, building, apartment);
        }

        @Override
        public String toString() {
            return "вул. " + street + ", буд. " + building + ", кв. " + apartment;
        }
    }

    static class JournalEntry {
        private String lastName;
        private String firstName;
        private LocalDate birthDate;
        private String phone;
        private Address address;

        public JournalEntry(String lastName, String firstName, LocalDate birthDate, String phone, Address address) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.birthDate = birthDate;
            this.phone = phone;
            this.address = address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JournalEntry that = (JournalEntry) o;
            return Objects.equals(lastName, that.lastName)
                    && Objects.equals(firstName, that.firstName)
                    && Objects.equals(birthDate, that.birthDate)
                    && Objects.equals(phone, that.phone)
                    && Objects.equals(address, that.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lastName, firstName, birthDate, phone, address);
        }

        @Override
        public String toString() {
            return lastName + " " + firstName
                    + " | Дата нар.: " + birthDate.format(DATE_FORMATTER)
                    + " | Тел.: " + phone
                    + " | Адреса: " + address;
        }
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        List<JournalEntry> journal = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            boolean running = true;
            while (running) {
                System.out.println("\n--- Журнал куратора ---");
                System.out.println("1. Додати запис");
                System.out.println("2. Показати всі записи");
                System.out.println("0. Вихід");
                System.out.print("Оберіть дію: ");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        JournalEntry entry = readEntry(scanner);
                        journal.add(entry);
                        System.out.println("Запис успішно додано до журналу!");
                        break;
                    case "2":
                        printJournal(journal);
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Невірна команда, спробуйте ще раз.");
                }
            }
        }
    }

    private static JournalEntry readEntry(Scanner scanner) {
        String lastName = readValidString(scanner, "Введіть прізвище: ", "^[A-Za-zА-Яа-яІіЇїЄєҐґ'\\-]{2,30}$",
                "Помилка: прізвище має містити тільки літери (від 2 символів).");

        String firstName = readValidString(scanner, "Введіть ім'я: ", "^[A-Za-zА-Яа-яІіЇїЄєҐґ'\\-]{2,30}$",
                "Помилка: ім'я має містити тільки літери (від 2 символів).");

        LocalDate birthDate = readValidDate(scanner);

        String phone = readValidString(scanner, "Введіть телефон (+380XXXXXXXXX або 0XXXXXXXXX): ", "^(\\+380|0)\\d{9}$",
                "Помилка: невірний формат номера телефону.");

        String street = readValidString(scanner, "Введіть вулицю: ", "^[A-Za-zА-Яа-яІіЇїЄєҐґ0-9\\s.,'\\-]{2,40}$",
                "Помилка: введіть коректну назву вулиці.");

        String building = readValidString(scanner, "Введіть номер будинку (напр. 12 або 15А): ", "^\\d{1,4}[A-Za-zА-Яа-яІіЇїЄєҐґ]?(/\\d{1,3})?$",
                "Помилка: невірний формат номера будинку.");

        int apartment = readValidApartment(scanner);

        Address address = new Address(street, building, apartment);
        return new JournalEntry(lastName, firstName, birthDate, phone, address);
    }

    private static String readValidString(Scanner scanner, String prompt, String regex, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches(regex)) {
                return input;
            }
            System.out.println(errorMsg + " Спробуйте ще раз.");
        }
    }

    private static LocalDate readValidDate(Scanner scanner) {
        while (true) {
            System.out.print("Введіть дату народження (ДД.ММ.РРРР): ");
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMATTER);
                if (date.getYear() >= 1970 && date.getYear() <= 2012) {
                    return date;
                }
                System.out.println("Помилка: рік народження має бути в межах 1970–2012. Спробуйте ще раз.");
            } catch (DateTimeParseException e) {
                System.out.println("Помилка: невірний формат дати (очікується ДД.ММ.РРРР). Спробуйте ще раз.");
            }
        }
    }

    private static int readValidApartment(Scanner scanner) {
        while (true) {
            System.out.print("Введіть номер квартири: ");
            String input = scanner.nextLine().trim();
            try {
                int apt = Integer.parseInt(input);
                if (apt > 0 && apt <= 2000) {
                    return apt;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Помилка: номер квартири має бути додатним цілим числом. Спробуйте ще раз.");
        }
    }

    private static void printJournal(List<JournalEntry> journal) {
        System.out.println("\n--- Список записів у журналі ---");
        if (journal.isEmpty()) {
            System.out.println("Журнал порожній.");
            return;
        }
        for (int i = 0; i < journal.size(); i++) {
            System.out.println((i + 1) + ". " + journal.get(i));
        }
    }
}