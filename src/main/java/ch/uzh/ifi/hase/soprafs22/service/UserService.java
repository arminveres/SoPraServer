package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    // member variables
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    /**
     * This function get's the User by id
     * It also checks, if the User exists with this id, otherwise throws an error
     */
    public User getUserbyID(long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            String baseErrorMessage = "The user with this id does not exist";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage));
        } else {
            return user;
        }
    }

    /**
     * This function updates the old User with the new Username or the new Birthday
     * added
     */
    public User updateUser(User currentUser, User userInput) {
        if (userInput.getUsername() == null) {
            String baseErrorMessage = "You cannot choose an empty Username!";
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
        }
        // We check first, if the userInputs username is empty
        if (userInput.getUsername() != null) {
            // If it isn't empty, we need to check, if there is already a user with this
            // username
            // We need to also check, that the User didn't just set the same Username as he
            // already had
            User databaseUser = userRepository.findByUsername(userInput.getUsername());
            if (databaseUser != null && currentUser.getUsername() != databaseUser.getUsername()) {
                String baseErrorMessage = "You cannot choose this Username. It has already been taken!";
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage));
            } else {
                currentUser.setUsername(userInput.getUsername());
            }
        }
        if (userInput.getBirthday() != null) {
            currentUser.setBirthday(userInput.getBirthday());
        }
        userRepository.save(currentUser);
        userRepository.flush();
        return currentUser;
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);

        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    /**
     * @brief checks whether user exists
     * @throws org.springframework.web.server.ResponseStatusException
     * @returns current User
     */
    public User checkLoginCredentials(User user) {
        String errorMessage;
        // check name
        if (userRepository.findByUsername(user.getUsername()) == null) {
            errorMessage = "Username doesn't exist";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(errorMessage));
        } else {
            User currUser = userRepository.findByUsername(user.getUsername());
            String testPassword = user.getPassword();
            String currPassword = currUser.getPassword();
            // check pw
            if (!currPassword.equals(testPassword)) {
                errorMessage = "Password is incorrect";
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
                // set online
            } else {
                currUser.setStatus(UserStatus.ONLINE);
                userRepository.save(currUser);
                userRepository.flush();
                return currUser;
            }
        }
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        User userByName = userRepository.findByName(userToBeCreated.getName());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null && userByName != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage, "username and the name", "are"));
        } else if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage, "username", "is"));
        } else if (userByName != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
        }
    }

    /**
     * This function is for logging out the User
     */
    public User setUserOffline(User user) {
        // Get's the id of the user
        long id = user.getId();
        User logoutUser = userRepository.findById(id);
        logoutUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(logoutUser);
        userRepository.flush();
        return logoutUser;
    }
}