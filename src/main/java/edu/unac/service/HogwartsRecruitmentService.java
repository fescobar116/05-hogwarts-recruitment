package edu.unac.service;

import edu.unac.domain.exception.CharacterNotFoundException;
import edu.unac.service.external.PotterApiClient;
import edu.unac.domain.Character;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HogwartsRecruitmentService {
    private final PotterApiClient potterApiClient;

    public HogwartsRecruitmentService(PotterApiClient potterApiClient) {
        this.potterApiClient = potterApiClient;
    }

    public boolean canJoinMission(String searchName) {

        List<Character> characters = potterApiClient.searchCharacter(searchName);

        if (characters == null || characters.isEmpty()) {
            throw new CharacterNotFoundException("Character not found");
        }

        Character selected = characters.stream()
                .min(Comparator.comparingInt(Character::getIndex))
                .orElseThrow(() -> new CharacterNotFoundException("Character not found"));

        if (!isAllowedHouse(selected)) {
            return false;
        }

        int age = calculateAge(selected.getBirthdate());

        if (selected.getHogwartsHouse().equalsIgnoreCase("Gryffindor")
                && selected.getIndex() < 20) {
            return true;
        }

        if (age < 16) {
            return false;
        }

        if (age < 18 || (selected.getChildren() != null && !selected.getChildren().isEmpty())) {
            return false;
        }

        return true;
    }

    private boolean isAllowedHouse(Character character) {

        String house = character.getHogwartsHouse();

        return "Gryffindor".equalsIgnoreCase(house)
                || "Ravenclaw".equalsIgnoreCase(house);
    }

    private int calculateAge(String birthdate) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

        LocalDate birth =
                LocalDate.parse(birthdate, formatter);

        return Period.between(birth, LocalDate.now()).getYears();
    }
}
