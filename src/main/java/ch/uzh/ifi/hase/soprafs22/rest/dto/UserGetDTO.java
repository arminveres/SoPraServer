package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.sql.Timestamp;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import lombok.Getter;
import lombok.Setter;

public class UserGetDTO {

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private UserStatus status;

    @Getter @Setter
    private Timestamp creationdate;

    @Getter @Setter
    private String birthday;

    @Getter @Setter
    private String token;
}