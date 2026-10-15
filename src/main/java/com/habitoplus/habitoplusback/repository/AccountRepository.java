package com.habitoplus.habitoplusback.repository;

    import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import com.habitoplus.habitoplusback.model.Account;
import com.habitoplus.habitoplusback.model.Profile;
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByResetToken(String token);
    Optional<Account> findByProfile(Profile profile);
}