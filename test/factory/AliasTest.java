package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

class Subscription {

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

@FactorySetup(value = Subscription.class, alias = "ActiveSubscription")
class ActiveSubscriptionSetup {
    public String state() {
        return "active";
    }
}

@FactorySetup(value = Subscription.class, alias = "InactiveSubscription")
class InactiveSubscriptionSetup {
    public String state() {
        return "inactive";
    }
}

public class AliasTest {

    @Test
    public void shouldInstantiateUsingAliases() {
        assertEquals("active", Instantiator.create(Subscription.class, "ActiveSubscription").getState());
        assertEquals("inactive", Instantiator.create(Subscription.class, "InactiveSubscription").getState());
    }
}
