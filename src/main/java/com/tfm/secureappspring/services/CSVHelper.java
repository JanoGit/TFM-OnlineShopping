/*package com.tfm.secureappspring.services;

import com.tfm.secureappspring.data.models.Role;
import com.tfm.secureappspring.data.models.User;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Id", "Mail", "Password", "Role", "FirstName", "LastName", "Address", "RegistrationDate" };

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType())
                || Objects.equals(file.getContentType(), "application/vnd.ms-excel");
    }

    public static List<User> csvToUsers(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<User> userList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                User user = new User(
                        Integer.getInteger(csvRecord.get("Id")),
                        csvRecord.get("Mail"),
                        csvRecord.get("Password"),
                        Role.valueOf(csvRecord.get("Role")),
                        csvRecord.get("FirstName"),
                        csvRecord.get("LastName"),
                        csvRecord.get("Address"),
                        LocalDateTime.parse(csvRecord.get("RegistrationDate")),
                        Set.of(csvRecord.get("Order"))
                );

                userList.add(user);
            }

            return userList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream usersToCSV(List<User> userList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (User users : userList) {
                List<String> data = Arrays.asList(
                        String.valueOf(users.getId()),
                        users.getMail(),
                        users.getPassword(),
                        users.getFirstName(),
                        users.getLastName(),
                        users.getRole().toString(),
                        users.getRegistrationDate().toString()
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}*/
