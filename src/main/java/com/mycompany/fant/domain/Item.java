/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fant.domain;

/**
 *
 * @author Bruker
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ntnu.tollefsen.auth.User;

import javax.persistence.*;
import java.io.Serializable;

import static com.mycompany.fant.domain.Item.FIND_ALL_ITEMS;

@Entity
@Table(name = "Items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = FIND_ALL_ITEMS, query = "select i from Item i")
public class Item implements Serializable {

    public static final String FIND_ALL_ITEMS = "Item.findAllItems";

   // private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemid;

    private String item;
    private String description;
    private int price;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User itemOwner;
/*
    @ManyToOne
    private User itemBuyer;
*/
}
