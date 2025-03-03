/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.http.rq.multipart;

import com.artipie.asto.Content;
import com.artipie.asto.Key;
import com.artipie.asto.ext.PublisherAs;
import com.artipie.asto.fs.FileStorage;
import com.artipie.http.Headers;
import com.artipie.http.Response;
import com.artipie.http.Slice;
import com.artipie.http.async.AsyncResponse;
import com.artipie.http.headers.ContentDisposition;
import com.artipie.http.rs.RsStatus;
import com.artipie.http.rs.RsWithBody;
import com.artipie.http.rs.RsWithStatus;
import com.artipie.http.rs.StandardRs;
import com.artipie.vertx.VertxSliceServer;
import hu.akarnokd.rxjava2.interop.SingleInterop;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.reactivestreams.Publisher;

/**
 * Integration tests for multipart feature.
 * @since 1.2
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 line)
 * @checkstyle MagicNumberCheck (500 line)
 */
final class MultipartITCase {

    /**
     * Vertx instance.
     */
    private Vertx vertx;

    /**
     * Vertx slice server instance.
     */
    private VertxSliceServer server;

    /**
     * Server port.
     */
    private int port;

    /**
     * Container for slice.
     */
    private SliceContainer container;

    @BeforeEach
    void init() throws Exception {
        this.vertx = Vertx.vertx();
        this.container = new SliceContainer();
        this.server = new VertxSliceServer(this.vertx, this.container);
        this.port = this.server.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        this.server.stop();
        this.server.close();
        this.vertx.close();
    }

    @Test
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    void parseMultiparRequest() throws Exception {
        final AtomicReference<String> result = new AtomicReference<>();
        this.container.deploy(
            (line, headers, body) -> new AsyncResponse(
                new PublisherAs(
                    Flowable.fromPublisher(
                        new RqMultipart(new Headers.From(headers), body).inspect(
                            (part, sink) -> {
                                final ContentDisposition cds =
                                    new ContentDisposition(part.headers());
                                if (cds.fieldName().equals("content")) {
                                    sink.accept(part);
                                } else {
                                    sink.ignore(part);
                                }
                                final CompletableFuture<Void> res = new CompletableFuture<>();
                                res.complete(null);
                                return res;
                            }
                        )
                    ).flatMap(part -> part)
                ).asciiString().thenAccept(result::set).thenApply(
                    none -> StandardRs.OK
                )
            )
        );
        final String data = "hello-multipart";
        try (CloseableHttpClient cli = HttpClients.createDefault()) {
            final HttpPost post = new HttpPost(String.format("http://localhost:%d/", this.port));
            post.setEntity(
                MultipartEntityBuilder.create()
                    .addTextBody("name", "test-data")
                    .addTextBody("content", data)
                    .addTextBody("foo", "bar")
                    .build()
            );
            try (CloseableHttpResponse rsp = cli.execute(post)) {
                MatcherAssert.assertThat(
                    "code should be 200", rsp.getCode(), Matchers.equalTo(200)
                );
            }
        }
        MatcherAssert.assertThat(
            "content data should be parsed correctly", result.get(), Matchers.equalTo(data)
        );
    }

    @Test
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    void parseBigMultiparRequest() throws Exception {
        final AtomicReference<String> result = new AtomicReference<>();
        this.container.deploy(
            (line, headers, body) -> new AsyncResponse(
                new PublisherAs(
                    Flowable.fromPublisher(
                        new RqMultipart(new Headers.From(headers), body).inspect(
                            (part, sink) -> {
                                final ContentDisposition cds =
                                    new ContentDisposition(part.headers());
                                if (cds.fieldName().equals("content")) {
                                    sink.accept(part);
                                } else {
                                    sink.ignore(part);
                                }
                                final CompletableFuture<Void> res = new CompletableFuture<>();
                                res.complete(null);
                                return res;
                            }
                        )
                    ).flatMap(part -> part)
                ).asciiString().thenAccept(result::set).thenApply(
                    none -> StandardRs.OK
                )
            )
        );
        final byte[] buf = testData(2048 * 17);
        try (CloseableHttpClient cli = HttpClients.createDefault()) {
            final HttpPost post = new HttpPost(String.format("http://localhost:%d/", this.port));
            post.setEntity(
                MultipartEntityBuilder.create()
                    .addTextBody("name", "test-data")
                    .addBinaryBody("content", buf)
                    .addTextBody("foo", "bar")
                    .build()
            );
            try (CloseableHttpResponse rsp = cli.execute(post)) {
                MatcherAssert.assertThat(
                    "code should be 200", rsp.getCode(), Matchers.equalTo(200)
                );
            }
        }
        MatcherAssert.assertThat(
            "content data should be parsed correctly",
            result.get(),
            Matchers.equalTo(new String(buf, StandardCharsets.US_ASCII))
        );
    }

    @Test
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    void saveMultipartToFile(@TempDir final Path path) throws Exception {
        this.container.deploy(
            (line, headers, body) -> new AsyncResponse(
                Flowable.fromPublisher(
                    new RqMultipart(new Headers.From(headers), body).inspect(
                        (part, sink) -> {
                            final ContentDisposition cds =
                                new ContentDisposition(part.headers());
                            if (cds.fieldName().equals("content")) {
                                sink.accept(part);
                            } else {
                                sink.ignore(part);
                            }
                            final CompletableFuture<Void> res = new CompletableFuture<>();
                            res.complete(null);
                            return res;
                        }
                    )
                ).flatMapSingle(
                    part -> Single.fromFuture(
                        new FileStorage(path).save(
                            new Key.From(new ContentDisposition(part.headers()).fileName()),
                            new Content.From(part)
                        ).thenApply(none -> 0)
                    )
                ).toList().to(SingleInterop.get()).thenApply(none -> StandardRs.OK)
            )
        );
        final byte[] buf = testData(2048 * 17);
        final String filename = "data.bin";
        try (CloseableHttpClient cli = HttpClients.createDefault()) {
            final HttpPost post = new HttpPost(String.format("http://localhost:%d/", this.port));
            post.setEntity(
                MultipartEntityBuilder.create()
                    .addTextBody("name", "test-data")
                    .addBinaryBody("content", buf, ContentType.APPLICATION_OCTET_STREAM, filename)
                    .addTextBody("foo", "bar")
                    .build()
            );
            try (CloseableHttpResponse rsp = cli.execute(post)) {
                MatcherAssert.assertThat(
                    "code should be 200", rsp.getCode(), Matchers.equalTo(200)
                );
            }
        }
        MatcherAssert.assertThat(
            "content data should be save correctly",
            Files.readAllBytes(path.resolve(filename)),
            Matchers.equalTo(buf)
        );
    }

    /**
     * Create new test data buffer for payload.
     * @param size Buffer size
     * @return Byte array
     */
    private static byte[] testData(final int size) {
        final byte[] buf = new byte[size];
        final byte[] chunk = "0123456789ABCDEF\n".getBytes(StandardCharsets.US_ASCII);
        for (int pos = 0; pos < buf.length; pos += chunk.length) {
            System.arraycopy(chunk, 0, buf, pos, chunk.length);
        }
        return buf;
    }

    /**
     * Container for slice with dynamic deployment.
     * @since 1.2
     * @checkstyle ReturnCountCheck (100 lines)
     */
    private static final class SliceContainer implements Slice {

        /**
         * Target slice.
         */
        private volatile Slice target;

        @Override
        @SuppressWarnings("PMD.OnlyOneReturn")
        public Response response(final String line,
            final Iterable<Entry<String, String>> headers,
            final Publisher<ByteBuffer> body) {
            if (this.target == null) {
                return new RsWithBody(
                    new RsWithStatus(RsStatus.UNAVAILABLE),
                    "target is not set", StandardCharsets.US_ASCII
                );
            }
            return this.target.response(line, headers, body);
        }

        /**
         * Deploy slice to container.
         * @param slice Deployment
         */
        void deploy(final Slice slice) {
            this.target = slice;
        }
    }
}
