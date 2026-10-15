package com.habitoplus.habitoplusback.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberPK implements Serializable {
    private Integer group; 
    private Integer profile; 

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMemberPK)) return false;
        GroupMemberPK that = (GroupMemberPK) o;
        return Objects.equals(group, that.group) && Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, profile);
    }
}
