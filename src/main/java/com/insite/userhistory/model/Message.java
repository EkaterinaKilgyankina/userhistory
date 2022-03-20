package com.insite.userhistory.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Accessors (chain = true)
@Table(name = "message", schema = "public")
@Data
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    long clientId;

    @Column(name = "text")
    private String text;
}