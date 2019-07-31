/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eestec.thessaloniki.palermo.game_logic.roles;

import javax.enterprise.context.Dependent;
import javax.ws.rs.Produces;


public class RolesFactory {
    
    @Produces
    @Dependent
    public Roles getRoles(){
        return new Roles();
    }
}
