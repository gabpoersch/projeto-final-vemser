package br.com.dbc.devser.colabore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity (name = "user_colabore")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_user")
    @SequenceGenerator(name = "sequence_user", sequenceName = "sequence_user", allocationSize = 1)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "full_name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @JsonIgnore
    @OneToMany(mappedBy = "donator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DonationEntity> donations;

    @JsonIgnore
    @OneToMany(mappedBy = "fundraiserCreator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FundraiserEntity> fundraisers;

    @JsonIgnore
    @ManyToMany
    @JoinColumn(name = "role_id", referencedColumnName = "user_id")
    private Set<RoleEntity> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
