/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.slice;

import com.artipie.asto.Key;
import com.artipie.asto.Remaining;
import com.artipie.asto.Storage;
import com.artipie.asto.memory.InMemoryStorage;
import com.artipie.http.hm.RsHasStatus;
import com.artipie.http.rq.RequestLine;
import com.artipie.http.rs.RsStatus;
import io.reactivex.Flowable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.cactoos.map.MapEntry;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link SliceUpload}.
 * @since 0.6
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class SliceUploadTest {

    @Test
    void uploadsKeyByPath() throws Exception {
        final Storage storage = new InMemoryStorage();
        final String hello = "Hello";
        final byte[] data = hello.getBytes(StandardCharsets.UTF_8);
        final String path = "uploads/file.txt";
        MatcherAssert.assertThat(
            "Wrong HTTP status returned",
            new SliceUpload(storage).response(
                new RequestLine("PUT", path, "HTTP/1.1").toString(),
                Collections.singleton(
                    new MapEntry<>("Content-Size", Long.toString(data.length))
                ),
                Flowable.just(ByteBuffer.wrap(data))
            ),
            new RsHasStatus(RsStatus.CREATED)
        );
        MatcherAssert.assertThat(
            new String(
                new Remaining(
                    Flowable.fromPublisher(storage.value(new Key.From(path)).get()).toList()
                        .blockingGet().get(0)
                ).bytes(),
                StandardCharsets.UTF_8
            ),
            new IsEqual<>(hello)
        );
    }
}
