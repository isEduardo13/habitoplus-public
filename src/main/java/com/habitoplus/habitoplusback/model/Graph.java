package com.habitoplus.habitoplusback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.auto.value.AutoValue.Builder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="graphs")
public class Graph {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idGraph;
    
    private String graphId;  
    private String graphName; 
    private String unit;  
    private String type;  
    private String color;
    
    @ManyToOne
    @JoinColumn(name = "idPixela")
    @JsonBackReference  
    private Pixela pixela;

    
}
