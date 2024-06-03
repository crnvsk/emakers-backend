package com.emakers.biblioteca.domain.person;

import jakarta.persistence.*;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person extends RepresentationModel<Person> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personId;

    @Column(length = 80, nullable = false)
    private String name;

    @Column(length = 9, nullable = false)
    private String cep;

    @Column(length = 45, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Boolean isBorrowing;

    private Boolean isAdmin;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "person_roles", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsBorrowing() {
        return isBorrowing;
    }

    public void setIsBorrowing(Boolean borrowing) {
        isBorrowing = borrowing;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
