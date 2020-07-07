/*
 * MIT License
 *
 * Copyright (c) 2020 Artipie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie.http.headers;

import com.artipie.http.Headers;
import java.util.Iterator;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link WwwAuthenticate}.
 *
 * @since 0.12
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class WwwAuthenticateTest {

    @Test
    void shouldHaveExpectedName() {
        MatcherAssert.assertThat(
            new WwwAuthenticate("Basic").getKey(),
            new IsEqual<>("WWW-Authenticate")
        );
    }

    @Test
    void shouldHaveExpectedValue() {
        final String value = "Basic realm=\"http://artipie.com\"";
        MatcherAssert.assertThat(
            new WwwAuthenticate(value).getValue(),
            new IsEqual<>(value)
        );
    }

    @Test
    void shouldExtractValueFromHeaders() {
        final String value = "Basic realm=\"http://artipie.com/my-repo\"";
        final WwwAuthenticate header = new WwwAuthenticate(
            new Headers.From(
                new Header("Content-Length", "11"),
                new Header("www-authenticate", value),
                new Header("X-Something", "Some Value")
            )
        );
        MatcherAssert.assertThat(header.getValue(), new IsEqual<>(value));
    }

    @Test
    void shouldFailToExtractValueFromEmptyHeaders() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> new WwwAuthenticate(Headers.EMPTY).getValue()
        );
    }

    @Test
    void shouldFailToExtractValueWhenNoWwwAuthenticateHeaders() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> new WwwAuthenticate(
                new Headers.From("Content-Type", "text/plain")
            ).getValue()
        );
    }

    @Test
    void shouldFailToExtractValueFromMultipleHeaders() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> new WwwAuthenticate(
                new Headers.From(
                    new WwwAuthenticate("Basic realm=\"https://artipie.com\""),
                    new WwwAuthenticate("Bearer realm=\"https://artipie.com/token\"")
                )
            ).getValue()
        );
    }

    @Test
    void shouldParseHeaderWithoutParams() {
        final WwwAuthenticate header = new WwwAuthenticate("Basic");
        MatcherAssert.assertThat("Wrong scheme", header.scheme(), new IsEqual<>("Basic"));
        MatcherAssert.assertThat("Wrong params", header.params(), new IsEmptyCollection<>());
    }

    @Test
    void shouldParseHeaderWithParams() {
        final WwwAuthenticate header = new WwwAuthenticate(
            // @checkstyle LineLengthCheck (1 line)
            "Bearer realm=\"https://auth.docker.io/token\",service=\"registry.docker.io\",scope=\"repository:busybox:pull\""
        );
        MatcherAssert.assertThat(
            "Wrong scheme",
            header.scheme(),
            new IsEqual<>("Bearer")
        );
        MatcherAssert.assertThat(
            "Wrong realm",
            header.realm(),
            new IsEqual<>("https://auth.docker.io/token")
        );
        final Iterator<WwwAuthenticate.Param> params = header.params().iterator();
        final WwwAuthenticate.Param first = params.next();
        MatcherAssert.assertThat(
            "Wrong name of param #1",
            first.name(),
            new IsEqual<>("realm")
        );
        MatcherAssert.assertThat(
            "Wrong value of param #1",
            first.value(),
            new IsEqual<>("https://auth.docker.io/token")
        );
        final WwwAuthenticate.Param second = params.next();
        MatcherAssert.assertThat(
            "Wrong name of param #2",
            second.name(),
            new IsEqual<>("service")
        );
        MatcherAssert.assertThat(
            "Wrong value of param #2",
            second.value(),
            new IsEqual<>("registry.docker.io")
        );
        final WwwAuthenticate.Param third = params.next();
        MatcherAssert.assertThat(
            "Wrong name of param #3",
            third.name(),
            new IsEqual<>("scope")
        );
        MatcherAssert.assertThat(
            "Wrong value of param #3",
            third.value(),
            new IsEqual<>("repository:busybox:pull")
        );
    }
}
