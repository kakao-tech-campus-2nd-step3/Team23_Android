import com.kappzzang.jeongsan.util.DateConverter
import com.kappzzang.jeongsan.util.DateConverter.formatToExpenseDate
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DateConverterTest {

    @Test
    fun `LocalDateTime에서 String으로의 파싱은 일관적으로 이루어진다`() {
        // given
        val dateTime = LocalDateTime.of(
            2000,
            10,
            8,
            11,
            15,
            30
        )

        // when
        val formatted = dateTime.formatToExpenseDate()

        // then
        assertThat(formatted).isEqualTo("10/08 11:15")
    }

    @Test
    fun `자정은 00시로 표시된다`() {
        // given
        val dateTime = LocalDateTime.of(
            2024,
            11,
            24,
            0,
            15,
            30
        )

        // when
        val formatted = dateTime.formatToExpenseDate()

        // then
        assertThat(formatted).isEqualTo("11/24 00:15")
    }

    @Test
    fun `SQL TimeStamp에서 LocalDateTime으로의 파싱은 정상적으로 이루어진다`() {
        // given
        val timeStamp = "2020-10-12 13:11:52"

        // when
        val dateTime = DateConverter.parseFromString(timeStamp)

        // then
        assertThat(dateTime.year).isEqualTo(2020)
        assertThat(dateTime.monthValue).isEqualTo(10)
        assertThat(dateTime.dayOfMonth).isEqualTo(12)
        assertThat(dateTime.hour).isEqualTo(13)
        assertThat(dateTime.minute).isEqualTo(11)
        assertThat(dateTime.second).isEqualTo(52)
    }

    @Test
    fun `밀리초가 포함된 형식의 파싱도 오류 없이 이루어진다`() {
        // given
        val timeStamp = "2020-10-12 13:11:52.45"

        // when
        val dateTime = DateConverter.parseFromString(timeStamp)

        // then
        assertThat(dateTime.year).isEqualTo(2020)
        assertThat(dateTime.monthValue).isEqualTo(10)
        assertThat(dateTime.dayOfMonth).isEqualTo(12)
        assertThat(dateTime.hour).isEqualTo(13)
        assertThat(dateTime.minute).isEqualTo(11)
        assertThat(dateTime.second).isEqualTo(52)
    }
}
