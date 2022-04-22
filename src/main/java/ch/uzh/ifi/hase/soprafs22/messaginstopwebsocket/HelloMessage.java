package ch.uzh.ifi.hase.soprafs22.messaginstopwebsocket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class HelloMessage {
    @Getter
    @Setter
    private String name;

    public HelloMessage(String name) {
        this.name = name;
    }
}