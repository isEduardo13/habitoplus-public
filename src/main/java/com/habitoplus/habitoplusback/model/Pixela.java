package com.habitoplus.habitoplusback.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="pixela")
public class Pixela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPixela;
    private String username; 
    @JsonIgnore
    private String token;

    @OneToMany(mappedBy ="pixela", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Graph> graphs;

    @OneToOne
    @JoinColumn(name = "idAccount", referencedColumnName = "idAccount")
    @JsonBackReference(value = "account-pixela")  
    private Account account;
}
