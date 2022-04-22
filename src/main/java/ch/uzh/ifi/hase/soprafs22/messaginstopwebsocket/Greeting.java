package ch.uzh.ifi.hase.soprafs22.messaginstopwebsocket;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Greeting
 */
@NoArgsConstructor
public class Greeting {

    @Getter
    private String content;

    public Greeting(String content) {
        this.content = content;
    }
}