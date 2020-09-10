/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fant.domain;

import java.nio.file.Files;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import no.ntnu.tollefsen.auth.Group;
import no.ntnu.tollefsen.auth.User;

/**
 *
 * @author sverrelofthus
 */
@Path("service")
@Stateless
@DeclareRoles({Group.USER})
public class FantService {

    @Context
    SecurityContext sc;

    @PersistenceContext
    EntityManager em;
    
    @Inject
    MailService mailService;

    /**
     * Returns a list of all the items in Fant
     */
    @GET
    @Path("items")
    public List<Item> getAllItems() {
        return em.createNamedQuery(Item.FIND_ALL_ITEMS, Item.class).getResultList();
    }

    /**
     *
     * Adds an item. To be able to add an item you have to have a user and
     * logged in
     *
     * @param itemid
     * @param item
     * @param description
     * @param price
     * @return
     */
    @POST
    @Path("additem")
    @RolesAllowed({Group.USER})
    public Response addItem(
            @FormParam("item") String item,
            @FormParam("description") String description,
            @FormParam("price") int price) {

        User user = this.getCurrentUser();
        Item newItem = new Item();

        newItem.setItemOwner(user);
        newItem.setItem(item);
        newItem.setDescription(description);
        newItem.setPrice(price);

        em.persist(newItem);

        return Response.ok().build();
    }

    
    private User getCurrentUser() {
        return em.find(User.class, sc.getUserPrincipal().getName());
    }

    /**
     * Purchases an item. Should only be possible if buyer is logged in
     *
     * @param itemid
     * @return
     *
     */
    @PUT
    @Path("purchase")
    @RolesAllowed({Group.USER})
    public Response purchaseItem(@FormParam("iid") long itemid) {
        User buyer = this.getCurrentUser();
        Item item = em.find(Item.class, itemid);
        
        if (item != null){
            item.setItemBuyer(buyer);
            mailService.sendEmail(item.getItemOwner().getEmail(),"Your item sold", "Your item was sold: " + item.getItem());
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    
    /**
     * Delete method.A user should be able to delete an item that is posted by
     * himself.
     * @param itemid
     * @return 
     */
    @Path("delete")
    @DELETE
    @RolesAllowed({Group.USER})
    public Response deleteItem(@QueryParam("iid") long itemid) {
        Item item = em.find(Item.class, itemid);
        User user = this.getCurrentUser();
        if (item != null) {
            if (item.getItemOwner().getUserid().equals(user.getUserid())) {
                em.remove(item);
                return Response.ok(item).build();   
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
