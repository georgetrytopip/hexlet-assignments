package exercise;

import java.sql.Array;
import java.util.Random;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javafaker.Faker;

public class Data {
    private static final int COMPANIES_COUNT = 100;

    public static List<String> getCompanies() {
        Faker faker = new Faker(new Locale("en"), new Random(123));
        List<String> companies = new ArrayList<>();

        for (int i = 0; i < COMPANIES_COUNT; i++) {
            String company = faker.company().name() + " " + faker.company().suffix();
            companies.add(company);
        }

        return companies;
    }

    public static List<String> getCompany(String letters){

        if(letters == null) {
            return getCompanies();
        }

        List<String> filteredCompanies = new ArrayList<>();
        Pattern pattern = Pattern.compile(letters);
        List<String> companiesForUse = getCompanies();

        for(String company : companiesForUse) {
            Matcher matcher = pattern.matcher(company);
            if (matcher.find()) {
                filteredCompanies.add(company);
            }
        }
        return filteredCompanies;
    }
}
