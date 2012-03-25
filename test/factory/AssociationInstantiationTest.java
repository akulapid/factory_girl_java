package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

class Driver {
    String licenseId;

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }
}

class License {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

@Factory(Driver.class)
class DriverSetup {
    public String licenseId(License license) {
        return license.getId();
    }
}

@Factory(License.class)
class LicenseSetup {
    public String id() {
        return "123";
    }
}

public class AssociationInstantiationTest {

    @Test
    public void shouldInstantiateAssociations() {
        Driver driver = Instantiator.create(Driver.class);
        assertEquals("123", driver.getLicenseId());
    }
}
