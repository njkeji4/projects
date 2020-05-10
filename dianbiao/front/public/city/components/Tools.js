import * as THREE from '../3js/build/three.module.js';
import {ThreeBSP}  from '../lib/ThreeCSG.js';

class Tools{

   static boxWithoutBottom(x,y,z){
        if(x === undefined)x = 1;
        if(y === undefined)y = 1;
        if(z === undefined)z = 1;
 
        var geometry = new THREE.BoxGeometry( x,y,z );
     
         // translate the geometry to place the pivot point at the bottom instead of the center
         //geometry.applyMatrix( new THREE.Matrix4().makeTranslation( 0, y/2, 0 ) );
         geometry.translate(0, y/2, 0);
         
         // get rid of the bottom face - it is never seen
         geometry.faces.splice(6,2);
         geometry.faceVertexUvs[0].splice( 6, 2 );
        
         // buildMesh
         return  new THREE.Mesh( geometry);
     } 
    
     static digHole(mesh, hole){
         var mesh_bsp = new ThreeBSP(mesh);
         var hole_bsp = new ThreeBSP(hole);
         var result = mesh_bsp.subtract(hole_bsp);
         
         return result.toMesh();
     }
}

export {Tools};