package com.spilkor.webgamesapp.model;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;


    public Long  getId() {
        return id;
    }

    public void setId(Long  id) {
        this.id = id;
    }

}
