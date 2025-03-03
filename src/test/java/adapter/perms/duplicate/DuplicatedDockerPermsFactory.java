/*
 * The MIT License (MIT) Copyright (c) 2020-2022 artipie.com
 * https://github.com/artipie/http/blob/master/LICENSE.txt
 */
package adapter.perms.duplicate;

import com.artipie.security.perms.ArtipiePermissionFactory;
import com.artipie.security.perms.PermissionConfig;
import com.artipie.security.perms.PermissionFactory;
import java.security.AllPermission;
import java.security.Permission;

/**
 * Test permission.
 * @since 1.2
 */
@ArtipiePermissionFactory("docker-perm")
public final class DuplicatedDockerPermsFactory implements PermissionFactory {
    @Override
    public Permission newPermission(final PermissionConfig config) {
        return new AllPermission();
    }
}
