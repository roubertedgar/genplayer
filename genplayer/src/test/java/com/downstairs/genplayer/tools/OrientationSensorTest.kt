import androidx.test.ext.junit.runners.AndroidJUnit4
import com.downstairs.genplayer.tools.orientation.Orientation
import com.downstairs.genplayer.tools.orientation.OrientationListener
import com.downstairs.genplayer.tools.orientation.OrientationSensor
import io.mockk.*
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class OrientationSensorTest {

    private val context = RuntimeEnvironment.systemContext

    private lateinit var orientationSensor: OrientationSensor
    private lateinit var orientationListener: OrientationListener

    private val orientationSlot = slot<Orientation>()

    @Before
    fun setUp() {
        orientationListener = spyk(OrientationListener(context))
        orientationSensor = OrientationSensor(orientationListener)
        every { orientationListener.canDetectOrientation() } returns true

        orientationSlot.clear()
    }

    @Test
    fun `enable orientation listener on set orientation change listener`() {
        orientationSensor.setOrientationChangeListener(mockk())

        verify { orientationListener.enable() }
    }

    @Test
    fun `disable orientation listener on call disable method`() {
        orientationSensor.disable()

        verify { orientationListener.disable() }
    }

    @Test
    fun `notify landscape orientation when device is above 240 and under 300 degrees`() {
        val listener = mockk<(Orientation) -> Unit>(relaxed = true)
        orientationSensor.setOrientationChangeListener(listener)

        orientationListener.onOrientationChanged(299)

        verify { listener(capture(orientationSlot)) }

        assertThat(orientationSlot.captured).isEqualTo(Orientation.LANDSCAPE)
    }

    @Test
    fun `notify landscape orientation when device is above 60 and under 120 degrees`() {
        val listener = mockk<(Orientation) -> Unit>(relaxed = true)
        orientationSensor.setOrientationChangeListener(listener)

        orientationListener.onOrientationChanged(70)

        val capture = slot<Orientation>()
        verify { listener(capture(capture)) }

        assertThat(capture.captured).isEqualTo(Orientation.LANDSCAPE)
    }

    @Test
    fun `notify portrait orientation when device is above 330 and under 360 degrees`() {
        val listener = mockk<(Orientation) -> Unit>(relaxed = true)
        orientationSensor.setOrientationChangeListener(listener)

        orientationListener.onOrientationChanged(350)

        val capture = slot<Orientation>()
        verify { listener(capture(capture)) }

        assertThat(capture.captured).isEqualTo(Orientation.PORTRAIT)
    }

    @Test
    fun `notify portrait orientation when device is above 0 and under 30 degrees`() {
        val listener = mockk<(Orientation) -> Unit>(relaxed = true)
        orientationSensor.setOrientationChangeListener(listener)

        orientationListener.onOrientationChanged(20)

        val capture = slot<Orientation>()
        verify { listener(capture(capture)) }

        assertThat(capture.captured).isEqualTo(Orientation.PORTRAIT)
    }

    @Test
    fun `notify unknown orientation when device is above 30 and under 60 degrees`() {
        val listener = mockk<(Orientation) -> Unit>(relaxed = true)
        orientationSensor.setOrientationChangeListener(listener)

        orientationListener.onOrientationChanged(31)
        orientationListener.onOrientationChanged(40)
        orientationListener.onOrientationChanged(59)

        verify { listener.invoke(any()) }
    }

    @Test
    fun `notify unknown orientation when device is above 300 and under 330 degrees`() {
        val listener = mockk<(Orientation) -> Unit>(relaxed = true)
        orientationSensor.setOrientationChangeListener(listener)

        orientationListener.onOrientationChanged(301)
        orientationListener.onOrientationChanged(315)
        orientationListener.onOrientationChanged(329)

        verify { listener.invoke(any()) }
    }

    @Test
    fun `notify again just when orientation changes`() {
        val listener = mockk<(Orientation) -> Unit>(relaxed = true)
        orientationSensor.setOrientationChangeListener(listener)

        this.orientationListener.onOrientationChanged(50) // unknown - notify on first orientation detected
        this.orientationListener.onOrientationChanged(59) // unknown - does not notify
        this.orientationListener.onOrientationChanged(60) // landscape - notify on change
        this.orientationListener.onOrientationChanged(90) // landscape - does not notify
        this.orientationListener.onOrientationChanged(0) // portrait - notify on change
        this.orientationListener.onOrientationChanged(29) // portrait - does not notify

        verify(exactly = 3) { listener.invoke(any()) }
    }
}
