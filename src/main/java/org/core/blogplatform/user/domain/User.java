package org.core.blogplatform.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.core.blogplatform.post.domain.Post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate createAt;

    @Column(nullable = false)
    private LocalDate updateAt;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(createAt, user.createAt) && Objects.equals(updateAt, user.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, createAt, updateAt);
    }

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDate.now();
    }

}
