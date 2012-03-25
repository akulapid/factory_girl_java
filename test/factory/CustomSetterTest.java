package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

class Color {

    private String red;
    private String blue;

    public String giveMeRed() {
        return red;
    }

    public void colorMeRed(String red) {
        this.red = red;
    }

    public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }
}

@Factory(Color.class)
class ColorSetup {

    @Setter(method = "colorMeRed")
    public String red() {
        return "crimson";
    }

    public String blue() {
        return "indigo";
    }
}

public class CustomSetterTest {

    @Test
    public void shouldSetupUsingCustomSetter() {
        assertEquals("crimson", Instantiator.create(Color.class).giveMeRed());
    }

    @Test
    public void shouldUseSetPrefixConventionToFindTargetSetter() {
        assertEquals("indigo", Instantiator.create(Color.class).getBlue());
    }
}
