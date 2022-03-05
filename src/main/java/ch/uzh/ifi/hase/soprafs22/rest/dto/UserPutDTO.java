package ch.uzh.ifi.hase.soprafs22.rest.dto;

import lombok.Getter;
import lombok.Setter;

// This class is for updating the user
public class UserPutDTO {

    // The id is used to identify the user

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String birthday;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;
}