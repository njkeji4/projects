import * as THREE from '../3js/build/three.module.js';
import {Tools}  from './tools.js';

class Room{
    constructor(width,depth,height){
        this.width = width | 5;
        this.depth = depth | 3;

        this.height = this.height | 4;

        this.group = new THREE.Group();

        this.wallDepth = 0.1;

        this.textureLoader = new THREE.TextureLoader();//.load( "../texture/checkerboard.jpg" );
    }

    build(){
        
        //this.ground();
        
        this.wall();

        //this.meetingTable();

        //this.chairs();

        return this.group;
    }

    ground(){
        var floor	= new THREE.PlaneGeometry(this.width+2, this.depth+2,1,1);
        
        var texture = this.textureLoader.load('textures/b.jpg');
        
        texture.wrapS = texture.wrapT = THREE.RepeatWrapping; 
        texture.repeat.set( 10, 10 );
        var material	= new THREE.MeshLambertMaterial({
            color:'#222222',
           // map:texture
        });
        var ground	= new THREE.Mesh(floor, material)
        ground.lookAt(new THREE.Vector3(0,1,0))
        this.group.add( ground);
    }

    wall(){
        var wallGeometry= new THREE.Geometry();

        //front wall
        var wall1 = Tools.boxWithoutBottom(this.width,this.height,this.wallDepth);
        var hole = new THREE.Mesh(new THREE.BoxGeometry(2.5,3.5,this.wallDepth).translate(-10,1.75,0));   //hole for door
        wall1 = Tools.digHole(wall1, hole);

        var hole = new THREE.Mesh(new THREE.BoxGeometry(10,2,this.wallDepth).translate(5,1.8,0));   //hole for window
        wall1 = Tools.digHole(wall1, hole);      
        wall1.position.z = this.depth / 2;
        wallGeometry.mergeMesh(wall1);

        //add window
        var texture = this.textureLoader.load('textures/d.jpg');
        texture.wrapS = texture.wrapT = THREE.RepeatWrapping; 
        texture.repeat.set( 10, 10 );
        var material	= new THREE.MeshLambertMaterial({
            //color:'#ffffff',
            map:texture,
            transparent:true,
            opacity:0.5
        });
        var window = new THREE.PlaneGeometry(2,0.5);
        var windowMesh = new THREE.Mesh(window,material);
        windowMesh.position.z = this.depth / 2-0.05;
        window.translate(0.5,0.5,0);
        this.group.add(windowMesh);


        //back wall
        wall1 = Tools.boxWithoutBottom();
        wall1.scale.x = this.width;
        wall1.scale.y = this.height;
        wall1.scale.z = this.wallDepth;
        wall1.position.z = -this.depth / 2;
        wallGeometry.mergeMesh(wall1);

        //left wall
        wall1 = Tools.boxWithoutBottom();
        wall1.scale.x = this.wallDepth;
        wall1.scale.y = this.height;
        wall1.scale.z = this.depth;
        wall1.position.x = -this.width / 2;
        wallGeometry.mergeMesh(wall1);

        //right wall
        wall1 = Tools.boxWithoutBottom();
        wall1.scale.x = this.wallDepth;
        wall1.scale.y = this.height;
        wall1.scale.z = this.depth;
        wall1.position.x = this.width / 2;
        wallGeometry.mergeMesh(wall1);
        
        var wallMaterial	= new THREE.MeshLambertMaterial({
            color	: 0x666666
        });
        this.group.add( new THREE.Mesh(wallGeometry, wallMaterial));
    }

    meetingTable(){        

        var t1 = this.textureLoader.load('textures/f.jpg');
        t1.wrapS = t1.wrapT = THREE.RepeatWrapping; 
        //texture.repeat.set( 100, 100 );
        var material	= new THREE.MeshPhongMaterial({
            specular: 0x333333,
            shininess: 15,
            map: t1,
        });

        var mesh = new THREE.Mesh(new THREE.BoxGeometry(3, 0.3, 1.5));
        var hole = new THREE.Mesh(new THREE.BoxGeometry(2.5,0.3,0.3));
        var mesh = Tools.digHole(mesh,hole);
        mesh.material = material;

        this.group.add(mesh);
    }

    chairs(){

        var chairGeo= new THREE.Geometry();

        var startX = -1;
        for(var i = 0; i < 3; i++ ){
            var geo = this.chair();
            geo.scale(0.005,0.005,0.005);
            geo.translate(startX+i*1,0,-1.1);
            chairGeo.merge(geo);
        }
        for(var i = 0; i < 3; i++ ){
            var geo = this.chair();
            geo.scale(0.005,0.005,0.005);
            geo.rotateY(Math.PI);
            geo.translate(startX+i*1,0,1.1);
            chairGeo.merge(geo);
        }

        var t1 = this.textureLoader.load('textures/f.jpg');
        t1.wrapS = t1.wrapT = THREE.RepeatWrapping; 
        //texture.repeat.set( 100, 100 );
        var material	= new THREE.MeshPhongMaterial({
            specular: 0x333333,
            shininess: 15,
            map: t1,
        });

        var meshs = new THREE.Mesh(chairGeo, material);

       
        this.group.add(meshs);
    }

    chair(){
        var chairGeo= new THREE.Geometry();
        
        //chair surface
        var width = 100, height = 100, width_segments =1, dep_segments = 20;			
        var plane = new THREE.BoxGeometry(width,1,height, 1,1,dep_segments);
        plane.translate(0,2.5,0);
        for(var i=0; i<plane.vertices.length; i++) {
            var z = plane.vertices[i].z;
            if(z < 0){
                plane.vertices[i].y += Math.pow(2, -z/8);
            }
            if(z > 0){
                plane.vertices[i].y += Math.pow(2, z/15);
            }
        }
        chairGeo.merge(plane);

        //chair foots
        var foot_width = 3;	
        var footgeo = new THREE.BoxGeometry(foot_width,180,foot_width);
        footgeo.translate(-40,-95,-20);
        footgeo.rotateZ(-Math.PI*0.1);
        chairGeo.merge(footgeo);
        

        var footgeo2 = new THREE.BoxGeometry(foot_width,180,foot_width);
        footgeo2.translate(40,-95,-20);
        footgeo2.rotateZ(Math.PI*0.1);
        chairGeo.merge(footgeo2);

        var footgeo3 = new THREE.BoxGeometry(foot_width,180,foot_width);
        footgeo3.translate(-40,-98,30);
        footgeo3.rotateZ(-Math.PI*0.1);
        chairGeo.merge(footgeo3);

        var footgeo4 = new THREE.BoxGeometry(foot_width,180,foot_width);
        footgeo4.translate(40,-97,30);
        footgeo4.rotateZ(Math.PI*0.1);
        chairGeo.merge(footgeo4);

        return chairGeo;
    }
}

export {Room};