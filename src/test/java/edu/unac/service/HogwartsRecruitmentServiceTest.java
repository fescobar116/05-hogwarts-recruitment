package edu.unac.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.unac.domain.exception.CharacterNotFoundException;
import edu.unac.service.external.PotterApiClient;
import edu.unac.domain.Character;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class HogwartsRecruitmentServiceTest {
    @Test
    void shouldThrowExceptionWhenCharacterNotFound() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        when(potterApiClient.searchCharacter("Unknown"))
                .thenReturn(List.of());

        assertThrows(CharacterNotFoundException.class,
                () -> service.canJoinMission("Unknown"));
    }


    @Test
    void shouldThrowExceptionWhenCharacterNotFoundNull() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        when(potterApiClient.searchCharacter("Unknown"))
                .thenReturn(null);

        assertThrows(CharacterNotFoundException.class,
                () -> service.canJoinMission("Unknown"));
    }

    @Test
    void shouldReturnFalseWhenHouseNotAllowed() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Draco Malfoy")
                .hogwartsHouse("Slytherin")
                .birthdate("Jun 5, 1980")
                .children(List.of())
                .index(30)
                .build();

        when(potterApiClient.searchCharacter("Malfoy"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Malfoy");

        assertFalse(result);
    }

    @Test
    void shouldAllowSpecialAccessForGryffindorLowIndex() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Neville Longbottom")
                .hogwartsHouse("Gryffindor")
                .birthdate("Jul 30, 1980")
                .children(List.of())
                .index(10)
                .build();

        when(potterApiClient.searchCharacter("Longbottom"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Longbottom");

        assertTrue(result);
    }

    @Test
    void shouldAllowSpecialAccessForGryffindorLowIndexTeen() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Neville Longbottom")
                .hogwartsHouse("Gryffindor")
                .birthdate("Jul 30, 2012")
                .children(List.of())
                .index(10)
                .build();

        when(potterApiClient.searchCharacter("Longbottom"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Longbottom");

        assertTrue(result);
    }

    @Test
    void shouldAllowSpecialAccessForGryffindorLowIndexTeenBoundary() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Neville Longbottom")
                .hogwartsHouse("Gryffindor")
                .birthdate("Jul 30, 2012")
                .children(List.of())
                .index(20)
                .build();

        when(potterApiClient.searchCharacter("Longbottom"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Longbottom");

        assertFalse(result);
    }



    @Test
    void shouldAllowSpecialAccessForGryffindorNotLowIndex() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Neville Longbottom")
                .hogwartsHouse("Gryffindor")
                .birthdate("Jul 30, 1980")
                .children(List.of())
                .index(23)
                .build();

        when(potterApiClient.searchCharacter("Longbottom"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Longbottom");

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenAgeLessThan16() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Young Wizard")
                .hogwartsHouse("Ravenclaw")
                .birthdate("Jan 1, 2012")
                .children(List.of())
                .index(50)
                .build();

        when(potterApiClient.searchCharacter("Young"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Young");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenAgeLessThan18AndNoChildren() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Teen Wizard")
                .hogwartsHouse("Ravenclaw")
                .birthdate("Jan 1, 2009")
                .children(List.of())
                .index(50)
                .build();

        when(potterApiClient.searchCharacter("Teen"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Teen");

        assertFalse(result);
    }

    @Test
    void shouldAllowAccessWhenAgeAbove18() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Adult Wizard")
                .hogwartsHouse("Ravenclaw")
                .birthdate("Jan 1, 1990")
                .children(List.of())
                .index(50)
                .build();

        when(potterApiClient.searchCharacter("Adult"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Adult");

        assertTrue(result);
    }

    @Test
    void shouldAllowAccessWhenAgeAbove18NullChildren() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Adult Wizard")
                .hogwartsHouse("Ravenclaw")
                .birthdate("Jan 1, 1990")
                .children(null)
                .index(50)
                .build();

        when(potterApiClient.searchCharacter("Adult"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Adult");

        assertTrue(result);
    }

    @Test
    void shouldAllowAccessWhenAgeAbove18WithChildren() {
        PotterApiClient potterApiClient = mock(PotterApiClient.class);
        HogwartsRecruitmentService service = new HogwartsRecruitmentService(potterApiClient);

        Character character = Character.builder()
                .fullName("Adult Wizard")
                .hogwartsHouse("Ravenclaw")
                .birthdate("Jan 1, 1990")
                .children(List.of("First Child"))
                .index(50)
                .build();

        when(potterApiClient.searchCharacter("Adult"))
                .thenReturn(List.of(character));

        boolean result = service.canJoinMission("Adult");

        assertFalse(result);
    }
}
