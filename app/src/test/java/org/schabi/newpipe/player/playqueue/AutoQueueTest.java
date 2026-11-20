package org.schabi.newpipe.player.playqueue;

import org.junit.Test;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamType;
import org.schabi.newpipe.player.helper.PlayerHelper;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class AutoQueueTest {
    private static PlayQueueItem item(final String url) {
        return new PlayQueueItem(new StreamInfoItem(0, url, "", StreamType.VIDEO_STREAM));
    }

    private static StreamInfoItem stream(final String url) {
        return new StreamInfoItem(0, url, "", StreamType.VIDEO_STREAM);
    }

    private static StreamInfo infoWithRelated(final List<InfoItem> related) {
        final StreamInfo info = mock(StreamInfo.class);
        doReturn(related).when(info).getRelatedItems();
        return info;
    }

    @Test
    public void picksFirstRelatedWhenFresh() {
        final PlayQueue next = PlayerHelper.autoQueueOf(
                infoWithRelated(List.of(stream("url_next"), stream("url_other"))),
                List.of(item("url_now")));
        assertNotNull(next);
        assertEquals("url_next", next.getItem().getUrl());
        assertTrue(next.getItem().isAutoQueued());
    }

    @Test
    public void skipsUrlsAlreadyInQueue() {
        final PlayQueue next = PlayerHelper.autoQueueOf(
                infoWithRelated(List.of(stream("url_now"), stream("url_next"))),
                List.of(item("url_now")));
        assertNotNull(next);
        assertEquals("url_next", next.getItem().getUrl());
    }

    @Test
    public void returnsNullWhenAllRepeat() {
        assertNull(PlayerHelper.autoQueueOf(
                infoWithRelated(List.of(stream("url_now"))),
                List.of(item("url_now"))));
    }

    @Test
    public void returnsNullWhenEmpty() {
        assertNull(PlayerHelper.autoQueueOf(
                infoWithRelated(List.of()), List.of(item("url_now"))));
    }

    @Test
    public void ignoresNonStreamItems() {
        final InfoItem playlist = mock(InfoItem.class);
        doReturn("url_pl").when(playlist).getUrl();
        final PlayQueue next = PlayerHelper.autoQueueOf(
                infoWithRelated(List.of(playlist, stream("url_ok"))),
                List.of(item("url_now")));
        assertNotNull(next);
        assertEquals("url_ok", next.getItem().getUrl());
    }
}
