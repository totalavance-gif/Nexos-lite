package org.schabi.newpipe.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReleaseVersionUtilTest {
    @Test
    public void equalVersionsCompareZero() {
        assertEquals(0, ReleaseVersionUtil.compareVersionNames("1.0.0", "1.0.0"));
    }

    @Test
    public void newerPatchIsPositive() {
        assertTrue(ReleaseVersionUtil.compareVersionNames("1.0.1", "1.0.0") > 0);
    }

    @Test
    public void newerMinorBeatsOlderPatch() {
        assertTrue(ReleaseVersionUtil.compareVersionNames("1.2.0", "1.1.9") > 0);
    }

    @Test
    public void olderVersionIsNegative() {
        assertTrue(ReleaseVersionUtil.compareVersionNames("1.0.0", "2.0.0") < 0);
    }

    @Test
    public void missingPartsCountAsZero() {
        assertEquals(0, ReleaseVersionUtil.compareVersionNames("1.0", "1.0.0"));
        assertTrue(ReleaseVersionUtil.compareVersionNames("1.1", "1.0.9") > 0);
    }

    @Test
    public void numericComparisonNotLexicographic() {
        assertTrue(ReleaseVersionUtil.compareVersionNames("1.0.10", "1.0.9") > 0);
    }

    @Test
    public void nonNumericPartsCountAsZero() {
        assertEquals(0, ReleaseVersionUtil.compareVersionNames("1.0.x", "1.0.0"));
    }
}
