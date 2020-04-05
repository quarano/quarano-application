package de.wevsvirushackathon.coronareport.authentication;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import de.wevsvirushackathon.coronareport.client.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An account of a user. Can be connected to a {@link Client} or can be a HD employee account
 * @author Patrick Otto
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    private String firstname;
    private String lastname;
    
    @OneToOne
    private Client client;
    
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Role> roles = new ArrayList<>();

}
