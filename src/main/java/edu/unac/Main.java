package edu.unac;

import edu.unac.service.HogwartsRecruitmentService;
import edu.unac.service.external.PotterApiClient;
import org.springframework.web.client.RestTemplate;

public class Main {
    static void main() {
        RestTemplate restTemplate = new RestTemplate();

        PotterApiClient client =
                new PotterApiClient(restTemplate);

        HogwartsRecruitmentService service =
                new HogwartsRecruitmentService(client);

        boolean result =
                service.canJoinMission("Longbottom");

        System.out.println("Can join mission: " + result);
    }
}
