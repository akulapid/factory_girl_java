package factory;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

class Bicycle {
}

class MountainBike extends Bicycle {
}

@Factory(MountainBike.class)
class MountainBikeSetup {
}

public class MissingSetupTest {

    @Test
    public void shouldNotThrowExceptionIfSuperClassDoesNotHaveSetup() {
        Instantiator.create(MountainBike.class);
    }
}

