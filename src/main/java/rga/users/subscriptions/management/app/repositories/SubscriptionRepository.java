package rga.users.subscriptions.management.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rga.users.subscriptions.management.app.entities.Subscription;
import rga.users.subscriptions.management.app.entities.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Page<Subscription> findAllByUserOrderByTimestampAsc(User user, Pageable pageable);

    Page<Subscription> findAllByUser(User user, Pageable pageable);

}
