package cosc202.andie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Robert
 */
public class ImagePanelTest {

    public ImagePanelTest() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void test() {
        assertTrue(true);
    }

    @Test
    public void testGetZoomAfterSetZoom() {
        ImagePanel testPanel = new ImagePanel();
        testPanel.setZoom(120.0);
        assertEquals(testPanel.getZoom(), 120.0);
    }

}
