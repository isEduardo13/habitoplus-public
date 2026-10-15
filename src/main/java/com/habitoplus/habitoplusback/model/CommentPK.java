package com.habitoplus.habitoplusback.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPK implements Serializable {

    private Integer group;

    private Integer profile;

    private LocalDateTime dateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CommentPK))
            return false;
        CommentPK that = (CommentPK) o;
        return Objects.equals(group, that.group) && Objects.equals(profile, that.profile) && (dateTime != null && dateTime.isEqual(that.dateTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, profile, dateTime);
    }
}
