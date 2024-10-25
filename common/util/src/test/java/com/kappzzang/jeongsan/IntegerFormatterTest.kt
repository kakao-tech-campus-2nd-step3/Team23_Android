package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class IntegerFormatterTest {
    @Test
    fun `IntegerFormatter는 정수 값을 세 자리마다 콤마로 끊어준다`() {
        // given
        val input = 1205000

        // when
        val formatted = input.formatDecimalSeparator()

        // then
        assertThat(formatted).isEqualTo("1,205,000")
    }

    @Test
    fun `IntegerFormatter는 세 자리 정수값에 대해서 정상적으로 포매팅할 수 있다`() {
        // given
        val input = 500

        // when
        val formatted = input.formatDecimalSeparator()

        // then
        assertThat(formatted).isEqualTo("500")
    }

    @Test
    fun `IntegerFormatter는 음수에 대해서 정상적으로 포매팅할 수 있다`() {
        // given
        val input = -14500

        // when
        val formatted = input.formatDecimalSeparator()

        // then
        assertThat(formatted).isEqualTo("-14,500")
    }

    @Test
    fun `IntegerFormatter는 0에 대해서 정상적으로 포매팅할 수 있다`() {
        // given
        val input = 0

        // when
        val formatted = input.formatDecimalSeparator()

        // then
        assertThat(formatted).isEqualTo("0")
    }
}
