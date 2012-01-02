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

public class CompositeInstantiationTest {

    @Test
    public void shouldInstantiateComposedNonPrimitiveFields() {
        Country country = Factory.create(Country.class);
        assertNotNull(country.getState());
        assertNotNull(country.getState().getDistrict());
    }
}
