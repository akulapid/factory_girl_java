package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

class Country {
    State state;
}

class State {
    District district;
}

class District {
}

class Zone {
    String name;
    City city;

    public void setName(String name) {
        this.name = name;
    }
}

class City {
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

@Setup(Zone.class)
class ZoneSetup {
    public String name() {
        return "zone";
    }
}

public class CompositeInstantiationTest {

    @Test
    public void shouldInstantiateComposedNonPrimitiveFields() {
        Country country = Instantiator.create(Country.class);
        assertNotNull(country.state);
        assertNotNull(country.state.district);
    }

    @Test
    public void shouldNotThrowExceptionIfCompositeDoesNotHaveSetupDefined() {
        assertEquals("zone", Instantiator.create(Zone.class).name);
    }
}
