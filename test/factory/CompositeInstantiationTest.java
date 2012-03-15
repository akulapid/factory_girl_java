package factory;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

class Country {

    private State state;

    public State getState() {
        return this.state;
    }
}

class State {

    private District district;

    public District getDistrict() {
        return this.district;
    }
}

class District {
}

@Setup(Country.class)
class CountrySetup {
}

@Setup(State.class)
class StateSetup {
}

@Setup(District.class)
class DistrictSetup {
}

public class CompositeInstantiationTest {

    @Test
    public void shouldInstantiateComposedNonPrimitiveFields() {
        Country country = Instantiator.create(Country.class);
        assertNotNull(country.getState());
        assertNotNull(country.getState().getDistrict());
    }
}
