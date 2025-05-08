package rga.users.subscriptions.management.app.services.access;

import rga.users.subscriptions.management.app.entities.Subscription;

public interface UserAccessService {

    boolean isAdmin();

    boolean isSubscriber(Subscription subscription);

    String getCurrentEmail();

}
