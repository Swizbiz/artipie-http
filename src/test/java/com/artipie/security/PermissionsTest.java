/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package com.artipie.security;

import com.amihaiemil.eoyaml.Yaml;
import com.artipie.ArtipieException;
import com.artipie.security.perms.AdapterBasicPermission;
import com.artipie.security.perms.PermissionConfig;
import com.artipie.security.perms.PermissionsLoader;
import java.security.AllPermission;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link PermissionsLoader}.
 * @since 1.2
 */
class PermissionsTest {

    @Test
    void createsBasicPermission() {
        MatcherAssert.assertThat(
            new PermissionsLoader().newObject(
                "adapter_basic_permission",
                new PermissionConfig.Yaml(
                    Yaml.createYamlMappingBuilder()
                        .add("my-repo", Yaml.createYamlSequenceBuilder().add("read").build())
                        .build()
                )
            ),
            new IsInstanceOf(AdapterBasicPermission.class)
        );
    }

    @Test
    void createsAllPermission() {
        MatcherAssert.assertThat(
            new PermissionsLoader().newObject(
                "adapter_all_permission",
                new PermissionConfig.Yaml(Yaml.createYamlMappingBuilder().build())
            ),
            new IsInstanceOf(AllPermission.class)
        );
    }

    @Test
    void throwsExceptionIfPermNotFound() {
        Assertions.assertThrows(
            ArtipieException.class,
            () -> new PermissionsLoader().newObject(
                "unknown_perm",
                new PermissionConfig.Yaml(Yaml.createYamlMappingBuilder().build())
            )
        );
    }

    @Test
    void throwsExceptionIfPermissionsHaveTheSameName() {
        Assertions.assertThrows(
            ArtipieException.class,
            () -> new PermissionsLoader(
                Collections.singletonMap(
                    PermissionsLoader.SCAN_PACK, "adapter.perms.docker;adapter.perms.duplicate"
                )
            )
        );
    }

    @Test
    void createsExternalPermissions() {
        final PermissionsLoader permissions = new PermissionsLoader(
            Collections.singletonMap(
                PermissionsLoader.SCAN_PACK, "adapter.perms.docker;adapter.perms.maven"
            )
        );
        MatcherAssert.assertThat(
            "Maven permission was created",
            permissions.newObject(
                "maven-perm",
                new PermissionConfig.Yaml(Yaml.createYamlMappingBuilder().build())
            ),
            new IsInstanceOf(AllPermission.class)
        );
        MatcherAssert.assertThat(
            "Docker permission was created",
            permissions.newObject(
                "docker-perm",
                new PermissionConfig.Yaml(Yaml.createYamlMappingBuilder().build())
            ),
            new IsInstanceOf(AllPermission.class)
        );
    }

}
