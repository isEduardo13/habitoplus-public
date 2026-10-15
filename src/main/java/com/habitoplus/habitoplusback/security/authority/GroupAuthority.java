package com.habitoplus.habitoplusback.security.authority;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import com.habitoplus.habitoplusback.enums.Role;

public class GroupAuthority implements GrantedAuthority{

    private final Integer groupId;
    private final Role role;
    private final Set<GrantedAuthority> authorities;

    public GroupAuthority(Integer groupId, Role role, Set<GrantedAuthority> authorities) {
        this.groupId = groupId;
        this.role = role;
        this.authorities = authorities;

    }
    @Override
    public String getAuthority() {
        return role.name()  + "_GROUP_" + groupId;
    }

}
