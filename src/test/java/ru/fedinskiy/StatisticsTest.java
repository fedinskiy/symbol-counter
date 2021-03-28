package ru.fedinskiy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatisticsTest {
	@Test
	void simple() {
		final Statistics statistics = Statistics.fromString("abc");
		Assertions.assertEquals("abc", statistics.source);
		Assertions.assertEquals(3, statistics.letterCount);
		Assertions.assertEquals(3, statistics.totalCount);
	}

	@Test
	void space() {
		final Statistics stats = Statistics.fromString("ab c");
		Assertions.assertEquals(3, stats.letterCount);
		Assertions.assertEquals(4, stats.totalCount);
	}

	@Test
	void newline() {
		final Statistics stats = Statistics.fromString("ab\r\nc");
		Assertions.assertEquals(3, stats.letterCount);
		Assertions.assertEquals(4, stats.totalCount);
	}

	@Test
	void wtf() {
		Assertions.assertTrue(Character.isWhitespace('\r'));
	}
}
