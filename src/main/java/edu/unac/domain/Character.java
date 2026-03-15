package edu.unac.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Character {
    private String fullName;
    private String nickname;
    private String hogwartsHouse;
    private String interpretedBy;
    private List<String> children;
    private String birthdate;
    private int index;
}
