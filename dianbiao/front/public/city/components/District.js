
import * as THREE from '../3js/build/three.module.js';

class District{
    constructor(x, y){
        this.width = x | 80;
        this.depth = y | 80;

        this.group = new THREE.Group();
    }

    build(){

        this.ground();

        this.circleBelt(this.width,this.depth);

        //this.onebuilding();

        return this.group;
    }

    ground(){
        var geometry	= new THREE.PlaneGeometry( this.width, this.depth );
        geometry.rotateX(-Math.PI/2);
		var material	= new THREE.MeshLambertMaterial({
			color	: 0x222222
		})
		var ground	= new THREE.Mesh(geometry, material);
        this.group.add(ground);
        
        var block = 30;
        var sep = 5;
        var geo	= new THREE.Geometry();
        for(var i = 1; i <= 2; i++){
            var startx = -this.width/2 + sep*i + i*block - block / 2;
            for(var j = 1; j <= 2; j++){
                
                var startz = -this.depth/2 + sep*j + j*block - block / 2 + 5;

                var tmp = this.boxWithoutBottom(block,0.1,block);

                tmp.translate(startx, 0.05, startz);

                geo.merge(tmp);
            }
        }
        var material	= new THREE.MeshLambertMaterial({
			color	: 0x444444
        });
        this.group.add(new THREE.Mesh(geo, material));
    }

    colorifyBuilding(buildingMesh){
        var light	= new THREE.Color( 0xffffff )
        var shadow	= new THREE.Color( 0x303050 )

		// establish the base color for the buildingMesh
		var value	= 1 - Math.random() * Math.random();
		var baseColor	= new THREE.Color().setRGB( value + Math.random() * 0.1, value, value + Math.random() * 0.1 );
		// set topColor/bottom vertexColors as adjustement of baseColor
		var topColor	= baseColor.clone().multiply( light );
		var bottomColor	= baseColor.clone().multiply( shadow );
		// set .vertexColors for each face
		var geometry	= buildingMesh.geometry;		
		for ( var j = 0, jl = geometry.faces.length; j < jl; j ++ ) {
			if ( j === 4 || j === 5 ) {
				// set face.vertexColors on root face
				//geometry.faces[ j ].vertexColors = [ baseColor, baseColor, baseColor, baseColor ];
				geometry.faces[ j ].color= baseColor;
			} else {
				// set face.vertexColors on sides faces
				//geometry.faces[ j ].vertexColors = [ topColor, bottomColor, bottomColor, topColor ];
				geometry.faces[j].color=topColor;
			}
		}		
    }
    
    onebuilding(){
        var nBlockX	= 10
        var nBlockZ	= 10
        var blockSizeX	= 50
        var blockSizeZ	= 50
        var blockDensity= 20
        var roadW	= 8
        var roadD	= 8
        var buildingMaxW= 15
        var buildingMaxD= 15
        var sidewalkW	= 2
        var sidewalkH	= 0.1
        var sidewalkD	= 2
        var lampDensityW= 4
        var lampDensityD= 4
        var lampH	= 3
    
        var building= this.boxWithoutBottom(1,1,1);
        var buildingMesh= new THREE.Mesh( building );
        var cityGeometry= new THREE.Geometry();
        for( var i = 0; i < 20; i++){

            // set position
            buildingMesh.position.x	= (Math.random()-0.5)*(blockSizeX-buildingMaxW-roadW-sidewalkW)
            buildingMesh.position.z	= (Math.random()-0.5)*(blockSizeZ-buildingMaxD-roadD-sidewalkD)

            // add position for the blocks
            buildingMesh.position.x	+= (0.5-nBlockX/2)*blockSizeX
            buildingMesh.position.z	+= (0.5-nBlockZ/2)*blockSizeZ

            // put a random scale
            buildingMesh.scale.x	= Math.min(Math.random() * 5 + 10, buildingMaxW);
            buildingMesh.scale.y	= (Math.random() * Math.random() * buildingMesh.scale.x) * 3 + 4;
            buildingMesh.scale.z	= Math.min(buildingMesh.scale.x, buildingMaxD)

            this.colorifyBuilding(buildingMesh)

            // merge it with cityGeometry - very important for performance
            //THREE.GeometryUtils.merge( cityGeometry, buildingMesh );
            cityGeometry.mergeMesh(buildingMesh);
        }
        var buildingTexture		= new THREE.Texture( this.generateTextureCanvas() );
	    //buildingTexture.anisotropy	= renderer.capabilities.getMaxAnisotropy();//.getMaxAnisotropy();
	    buildingTexture.needsUpdate	= true;
        var material	= new THREE.MeshLambertMaterial({
			map		: buildingTexture,
			vertexColors	: THREE.VertexColors
		});
        var cityMesh	= new THREE.Mesh(cityGeometry, material );
        
        this.group.add(cityMesh);
    }

    generateTextureCanvas(){
		// build a small canvas 32x64 and paint it in white
		var canvas	= document.createElement( 'canvas' );
		canvas.width	= 32;
		canvas.height	= 64;
		var context	= canvas.getContext( '2d' );
		// plain it in white
		context.fillStyle	= '#ffffff';
		context.fillRect( 0, 0, 32, 64 );
		// draw the window rows - with a small noise to simulate light variations in each room
		for( var y = 2; y < 64; y += 2 ){
			for( var x = 0; x < 32; x += 2 ){
				var value	= Math.floor( Math.random() * 64 );
				context.fillStyle = 'rgb(' + [value, value, value].join( ',' )  + ')';
				context.fillRect( x, y, 2, 1 );
			}
		}

		// build a bigger canvas and copy the small one in it
		// This is a trick to upscale the texture without filtering
		var canvas2	= document.createElement( 'canvas' );
		canvas2.width	= 512;
		canvas2.height	= 1024;
		var context	= canvas2.getContext( '2d' );
		// disable smoothing
		context.imageSmoothingEnabled		= false;
		context.webkitImageSmoothingEnabled	= false;
		context.imageSmoothingEnabled	= false;
		// then draw the image
		context.drawImage( canvas, 0, 0, canvas2.width, canvas2.height );
		// return the just built canvas2
		return canvas2;
	}

    circleBelt(width,depth){

        var stoneWidth = 0.1;
        var stoneHeight = 0.3
        var beltWidth = 1.75;

        var g = new THREE.Geometry();    
        var g2 = new THREE.Geometry();    

        var west = new THREE.BoxGeometry(stoneWidth, stoneHeight, depth); 
        var west2 = new THREE.BoxGeometry(stoneWidth, stoneHeight, depth-beltWidth); 
        var close = new THREE.BoxGeometry(beltWidth+stoneWidth, stoneHeight, stoneWidth); 
        west.translate(-width/2,0,0);
        west2.translate(-width/2+beltWidth, 0, beltWidth/2);
        close.translate(-width/2+beltWidth/2,0,depth/2);
        g.merge(west);g.merge(west2);g.merge(close)

        //grass
        var p1 = new THREE.PlaneGeometry(beltWidth, depth);
        p1.rotateX(-Math.PI/2);
        p1.translate(-width/2+beltWidth/2,0.1,0);
        g2.merge(p1);

        var east = new THREE.BoxGeometry(stoneWidth, stoneHeight, depth); 
        var east2 = new THREE.BoxGeometry(stoneWidth, stoneHeight, depth-beltWidth); 
        var close2 = new THREE.BoxGeometry(beltWidth+stoneWidth, stoneHeight, stoneWidth); 
        east.translate(width/2,0,0);
        east2.translate(width/2-beltWidth,0,beltWidth/2);
        close2.translate(width/2-beltWidth/2,0,depth/2);
        g.merge(east);g.merge(east2);

        //grass
        var p1 = new THREE.PlaneGeometry(beltWidth, depth);
        p1.rotateX(-Math.PI/2);
        p1.translate(width/2-beltWidth/2,0.1,0);
        g2.merge(p1);

        var north = new THREE.BoxGeometry(width+0.1, stoneHeight, 0.1); 
        var north2 = new THREE.BoxGeometry(width+stoneWidth-2*beltWidth, stoneHeight, 0.1); 
        north.translate(0,0,-depth/2);
        north2.translate(0,0,-depth/2+beltWidth);
        g.merge(north);g.merge(north2);g.merge(close2)

        var p1 = new THREE.PlaneGeometry(depth-beltWidth*2, beltWidth);
        p1.rotateX(-Math.PI/2);
        p1.translate(0,0.1,-depth/2+beltWidth/2);
        g2.merge(p1);
      
        if(depth > 5){
            beltWidth = 0.9 //don't change it
            var road = 10;
            var south = new THREE.BoxGeometry(depth-road-0.5, 0.3, stoneWidth); 
            south.translate(0,0,depth/2);
            var south2 = new THREE.BoxGeometry(depth-road-0.5, 0.3, stoneWidth);
            south2.translate(0,0,depth/2+beltWidth);
            
            var arc1 = this.arc();
            arc1.rotateY(Math.PI/2);
            arc1.translate(-width/2+road/2, 0, depth/2+beltWidth/2);

            var arc2 = this.arc();
            arc2.rotateY(-Math.PI/2);
            arc2.translate(width/2-road/2, 0, depth/2+beltWidth/2);
            
            g.merge(south);g.merge(south2);g.merge(arc1);g.merge(arc2);

            //grass
            var p1 = new THREE.PlaneGeometry(depth-road-0.2, beltWidth);
            p1.rotateX(-Math.PI/2);
            p1.translate(0,0.1,depth/2+beltWidth/2);
            g2.merge(p1);
        }

        var material = new THREE.MeshLambertMaterial( { 
            color: 0xbbbbbb,
           // wireframe: true,
            //transparent:true,
            //map: texture,
        } );
     
        var mesh = new THREE.Mesh(g,material);
        this.group.add(mesh);

        //grass
        var textureLoader = new THREE.TextureLoader();
        var texture = textureLoader.load( "./textures/g2.png" );
        texture.wrapS = THREE.RepeatWrapping;
        texture.wrapT = THREE.RepeatWrapping;
        //texture.repeat.set(2,2);
        var grassMaterial = new THREE.MeshLambertMaterial( { 
            //color: 0xbbbbbb,
           // wireframe: true,
            //transparent:true,
            map: texture,
        } );

        var mesh = new THREE.Mesh(g2,grassMaterial);
        

        this.group.add(mesh);
    }
    
    block(){

    }

    arc(){
        var stoneWidth = 0.1;
        var stoneHeight = 0.3

        var geo = new THREE.BoxGeometry(1, stoneHeight, stoneWidth, 10); 
        for(var i = 0; i < geo.vertices.length; i++){
            var p = geo.vertices[i];
            p.z += Math.pow(Math.abs(p.x), 2);
        }
        return geo;
    }

    boxWithoutBottom(x,y,z){
        var geometry = new THREE.CubeGeometry( x, y, z );
        
        // get rid of the bottom face - it is never seen
        geometry.faces.splice(6,2);
        geometry.faceVertexUvs[0].splice( 6, 2 );

        return geometry;
    }

    belt(width,depth){
        
        width = width | 10;
        depth = depth | 5;

        var stoneWidth = 0.1;
        var stoneHeight = 0.3
        var beltWidth = 0.9;

        var g = new THREE.Geometry();    

        var geo = new THREE.BoxGeometry(stoneWidth, 0.3, depth-0.5); 
        geo.translate(-beltWidth/2,0,0);
        var geo2 = new THREE.BoxGeometry(stoneWidth, 0.3, depth-0.5);
        geo2.translate(beltWidth/2,0,0);
        
        var arc1 = this.arc();
        arc1.translate(0, 0, -this.depth/2);

        var arc2 = this.arc();
        arc2.rotateY(Math.PI);
        arc2.translate(0, 0, this.depth/2);

        g.merge(geo);
        g.merge(geo2);
        g.merge(arc1);
        g.merge(arc2);

        return g;
    }

}

export {District}