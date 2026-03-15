package edu.unac.service.external;

import org.springframework.web.client.RestTemplate;
import edu.unac.domain.Character;
import java.util.Arrays;
import java.util.List;

public class PotterApiClient {
    private final RestTemplate restTemplate;
    private static final String URL = "https://potterapi-fedeperin.vercel.app/es/characters?search=";

    public PotterApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Character> searchCharacter(String criteria) {

        Character[] response =
                restTemplate.getForObject(URL + criteria, Character[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.asList(response);
    }
}
