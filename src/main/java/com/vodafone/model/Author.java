package com.vodafone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
@Builder
@Data
@Entity
@Table(name="authers")
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "auther_name")
    private String name;
    @OneToMany(mappedBy = "author" ,cascade = CascadeType.REMOVE,orphanRemoval = true)
//    @JsonBackReference
    @JsonIgnore
    private List<Article> articleList;
    @JsonProperty("_links")
    @Transient
    private List<Links> links;
}
