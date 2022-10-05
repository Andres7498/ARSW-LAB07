/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import com.google.gson.Gson;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    BlueprintsServices bPServices;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprints() {

        try {
            return new ResponseEntity<>(bPServices.getAllBlueprints(), HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al consultar blueprints", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author) {
        try {
            return new ResponseEntity<>(bPServices.getBlueprintsByAuthor(author), HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al consultar blueprints por autor", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/{author}/{bpname}")
    public ResponseEntity<?> getBlueprint(@PathVariable String author, @PathVariable String bpname) {

        try {
            return new ResponseEntity<>(bPServices.getBlueprint(author, bpname), HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al consultar blueprint", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/blueprints/{author}/{bpname}/{planos}")
    public ResponseEntity<?> getBlueprintPost(@PathVariable String author, @PathVariable String bpname, @PathVariable String planos) {
        try {
            Blueprint bp=new Blueprint(author, bpname,convertStringtoObject(planos));
            bPServices.addNewBlueprint(bp);
        } catch (BlueprintPersistenceException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al ingresar blueprint con post", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{author}/{bpname}")
    public ResponseEntity<?> putBlueprintsByAuthor(@PathVariable String author, @PathVariable String bpname, @RequestBody JSONObject blueprintJson) {
        Blueprint blueprint;
        String jsonString = "";
        try {
            blueprint = bPServices.getBlueprint(author, bpname);
            String[] list = blueprintJson.get("Puntos").toString().split("-");
            blueprint.setAuthor(blueprintJson.get("Autor").toString());
            blueprint.setName(blueprintJson.get("Nombre").toString());

            for (String str : list) {
                List<String> templis = Arrays.asList(str.split(","));
                String corX = templis.get(0).substring(2);
                String corY = templis.get(1).substring(2, templis.get(0).length());
                Point po = new Point(Integer.parseInt(corX), Integer.parseInt(corY));
                blueprint.addPoint(po);
            }

            Set<Blueprint> blueprintSet = new HashSet<Blueprint>();
            blueprintSet.add(blueprint);
            jsonString = crearJsonString(blueprintSet);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al editar blueprint con put", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new Gson().toJson(jsonString), HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json", value = "/blueprints/{bpname}/{author}")
    public ResponseEntity<?> getBlueprintPost(@PathVariable String bpname, @PathVariable String author) {
        try {
            bPServices.deleteBlueprint(author, bpname);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("Error al borrar blueprint con delete", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED);
    }

    private String crearJsonString(Set<Blueprint> blueprints) {
        List<Blueprint> blueprintList = new ArrayList<>(blueprints);
        String blueprintsString = "[";

        for (Blueprint blueprint : blueprintList) {
            String author = blueprint.getAuthor();
            String name = blueprint.getName();
            String points = blueprint.getPointsString();
            blueprintsString += "{\"author\": \"" + author + "\", \"name\": \"" + name + "\", \"points\": \"" + points + "\"},";
        }
        blueprintsString = blueprintsString.substring(0, blueprintsString.length() - 1);
        blueprintsString += "]";
        return blueprintsString;
    }
    private Point[] convertStringtoObject(String points){
        String[] strArray = points.split(":");
        Point[] pts1=new Point[strArray.length/2];
        int index  = 0;
        for (int i = 1; i< strArray.length; i+=2){
            Point point = null;
            try{
                int x = Integer.parseInt(strArray[i].replace("}","").replace(",","").replace("'y'","").replace("{","").replace("'x'",""));
                int y = 0 ;
                if (i==strArray.length-2) {
                    String s = strArray[i + 1].replace("}", "").replace(",", "").replace("'y'", "").replace("{", "").replace("'x'", "");
                    s = s.substring(0, s.length() - 1);
                    y = Integer.parseInt(s);
                }else{y = Integer.parseInt(strArray[i + 1].replace("}", "").replace(",", "").replace("'y'", "").replace("{", "").replace("'x'", ""));}
                point = new Point(x,y);
            }
            catch (NumberFormatException ex){
                point = new Point(0,0);
            }pts1[index] = point;
            index++;
        }
        return pts1;
    }
}
