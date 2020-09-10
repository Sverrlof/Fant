/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fant.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author mikael
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo implements Serializable {
    @Id
    @GeneratedValue
    Long id;
    
    @Column(nullable = false)
    String subpath;

    public Photo(String subpath) {
        this.subpath = subpath;
    }
}