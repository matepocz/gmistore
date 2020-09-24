package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.dto.user.UserListDetailDto;
import hu.progmasters.gmistore.dto.user.UserRegistrationDTO;
import hu.progmasters.gmistore.enums.Role;
import hu.progmasters.gmistore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    @Query("select u from User u where u.id = :id")
    Optional<User> findUserById(@Param("id") Long id);

    Optional<User> findUserByUsername(String username);

    @Query("select new hu.progmasters.gmistore.dto.user.UserRegistrationDTO( u.id,u.registered ) " +
            "from User u join u.roles as r where r in :role ")
    List<UserRegistrationDTO> findByRolesIn(@Param("role") Role role);

    @Query("select u from User u")
    List<User> findAllUsersWithListDetails2();

    @Query("select new hu.progmasters.gmistore.dto.user.UserListDetailDto(u) from User u")
    List<UserListDetailDto> findAllUsersWithListDetails();

    @Query("select new hu.progmasters.gmistore.dto.user.UserRegistrationDTO( u.id,u.registered ) " +
            "from User u where u.registered <= :end AND u.registered>=:start")
    List<UserRegistrationDTO> findUserRegistrationsByDateInterval(@Param("start") LocalDateTime dateTimeStart,
                                             @Param("end") LocalDateTime dateTimeEnd);
}
