package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.security.Principal;
import java.sql.Timestamp;


/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
@NoArgsConstructor
public class User implements Serializable, Principal {

    // ! since the entity implements Principal, a java security interface,
    // ! the name has to be an identification, but in our case it's just
    // ! a combination of last and first name

    private static final long serialVersionUID = 1L;

    public User(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String username;

    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String token;

    @Column(nullable = false)
    @Getter @Setter
    private UserStatus status;

    @Column(nullable = false)
    @Getter @Setter
    private String password;

    @Column()
    @Getter @Setter
    private String birthday;

    @CreationTimestamp
    @Getter @Setter
    private Timestamp creationdate;
}