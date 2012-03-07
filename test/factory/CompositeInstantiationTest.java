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

@FactorySetup(type = Country.class)
class CountrySetup {
}

@FactorySetup(type = State.class)
class StateSetup {
}

@FactorySetup(type = District.class)
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
