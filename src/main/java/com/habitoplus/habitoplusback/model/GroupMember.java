package com.habitoplus.habitoplusback.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.habitoplus.habitoplusback.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groupmembers")
@IdClass(GroupMemberPK.class)
public class GroupMember {

    @Id
    @OneToOne
    @JoinColumn(name = "id_profile", referencedColumnName = "idProfile", nullable = false)
    @NotNull(message = "Profile cannot be null")
    private Profile profile;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_group", nullable = false)
    @JsonBackReference()
    @NotNull(message = "Group cannot be null")
    private Group group;

    @NotNull(message = "Role cannot be null")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull(message = "Union date cannot be null")
    @Column(nullable = false, name = "union_date")  
    private Date unionDate;
}
