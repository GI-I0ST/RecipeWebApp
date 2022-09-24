package com.ghost.recipewebapp.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "authorities")
    private String authorities;

    // For future improvement
    /*
    public void setAuthorities(String authorities) {
        this.authorities = authorities;
        this.authoritiesSet = new HashSet<>(AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
    }

    @Transient
    private Set<GrantedAuthority> authoritiesSet = new HashSet<>();

    public void setAuthoritiesSet(Collection<? extends GrantedAuthority> authoritiesCollection) {
        this.authoritiesSet = new HashSet<>(authoritiesSet);
        this.authorities = authoritiesCollectionToString(authoritiesSet);
    }

    public void addAuthority(GrantedAuthority authority) {
        if (this.authoritiesSet.add(authority)) {
            this.authorities = authoritiesCollectionToString(authoritiesSet);
        }
    }

    public void removeAuthority(GrantedAuthority authority) {
        if (this.authoritiesSet.remove(authority)) {
            this.authorities = authoritiesCollectionToString(authoritiesSet);
        }
    }

    private String authoritiesCollectionToString(Collection<? extends GrantedAuthority> authoritiesCollection) {
        return authoritiesCollection.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
    */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User that = (User) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
