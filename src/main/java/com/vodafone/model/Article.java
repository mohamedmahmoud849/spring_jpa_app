package com.vodafone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import javax.persistence.*;
@Getter
@Setter
@Builder
@Entity
@Table(name="article")
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "auther_id")

    private Author author;
    @JsonProperty("_links")
    @Transient
    private List<Links> links;
    
}
