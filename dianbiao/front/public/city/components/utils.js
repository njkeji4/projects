
import * as THREE from '../3js/build/three.module.js';

import { Line2 } from '../3js/examples/jsm/lines/Line2.js';
import { LineMaterial } from '../3js/examples/jsm/lines/LineMaterial.js';
import { LineGeometry } from '../3js/examples/jsm/lines/LineGeometry.js';
import { Tree } from './tree.js';

function waterPool(){
	var group = new THREE.Group();

	var wh = 0.1, ww = 3, pd=50, pw=25	//wall height, wall width, pool width, pool length

	var plane = new THREE.PlaneGeometry(pw,pd);
	
	var textureLoader = new THREE.TextureLoader();
	var texture = textureLoader.load( "./textures/water.png" );
	texture.wrapS = THREE.RepeatWrapping;
	texture.wrapT = THREE.RepeatWrapping;
	//texture.repeat.set(2,2);
	var material = new THREE.MeshLambertMaterial( { 
		//color: 0xdddddd,
		wireframe: false,
		transparent:true,
		map: texture,
	} );
	var mesh = new THREE.Mesh(plane,material);
	mesh.lookAt(0,1,0);
	group.add(mesh);

	var wallgeo = new THREE.Geometry();
	
	var w1 = new THREE.BoxGeometry(ww,wh,pd); w1.translate(-pw/2,0,0);
	var w2 = new THREE.BoxGeometry(pw+ww,wh,3); w2.translate(0,0,-pw);
	var w3 = new THREE.BoxGeometry(ww,wh,pd); w3.translate(pw/2,0,0);
	var w4 = new THREE.BoxGeometry(pw+ww,wh,ww); w4.translate(0,0,pw);

	wallgeo.merge(w1);
	wallgeo.merge(w2);wallgeo.merge(w3);wallgeo.merge(w4);

	var texture = textureLoader.load( "./textures/c.jpg" );
	texture.wrapS = THREE.RepeatWrapping;
	texture.wrapT = THREE.RepeatWrapping;
	texture.repeat.set(2,5);
	var wallmaterial = new THREE.MeshLambertMaterial( { 
		//color: 0xdddddd,
		wireframe: false,
		//transparent:true,
		map: texture,
	} );
	var mesh = new THREE.Mesh(wallgeo,wallmaterial);
	group.add(mesh);

	return group;
}

function grassGround(){
	var plane = new THREE.PlaneGeometry(50,50);
	var textureLoader = new THREE.TextureLoader();
	var texture = textureLoader.load( "./textures/g.png" );
	texture.wrapS = THREE.RepeatWrapping;
	texture.wrapT = THREE.RepeatWrapping;
	texture.repeat.set(10,10);
	var material = new THREE.MeshLambertMaterial( { 
		//color: 0xdddddd,
		wireframe: false,
		transparent:true,
		map: textureLoader.load( "./textures/g.png" ),
	} );
	var mesh = new THREE.Mesh(plane,material);
	mesh.lookAt(0,1,0);

	return mesh;
}

function oneTree(){
	var group = new THREE.Group();

	var myTree = new Tree({
		"seed": 262,
		"segments": 6,
		"levels": 3,
		"vMultiplier": 2.36,
		"twigScale": 0.39,
		"initalBranchLength": 0.49,
		"lengthFalloffFactor": 0.85,
		"lengthFalloffPower": 0.99,
		"clumpMax": 0.454,
		"clumpMin": 0.404,
		"branchFactor": 2.45,
		"dropAmount": -0.1,
		"growAmount": 0.235,
		"sweepAmount": 0.01,
		"maxRadius": 0.139,
		"climbRate": 0.371,
		"trunkKink": 0.093,
		"treeSteps": 5,
		"taperRate": 0.947,
		"radiusFalloffRate": 0.73,
		"twistRate": 3.02,
		"trunkLength": 2.4
	});

	var newTreeGeometry= function(tree, isTwigs) {

		var output = new THREE.Geometry();
		tree[ isTwigs ? 'vertsTwig' : 'verts'].forEach(function(v) {
			output.vertices.push(new THREE.Vector3(v[0], v[1], v[2]));
		});

		var uv = isTwigs ? tree.uvsTwig : tree.UV;
		tree[ isTwigs ? 'facesTwig' : 'faces'].forEach(function(f) {
			output.faces.push(new THREE.Face3(f[0], f[1], f[2]));
			output.faceVertexUvs[0].push(f.map(function(v) {
				return new THREE.Vector2(uv[v][0], uv[v][1]);
			}));
		});

		output.computeFaceNormals();
		output.computeVertexNormals(true);

		return output;
	}

	var textureLoader = new THREE.TextureLoader();

	var texture = textureLoader.load( "./textures/color1.jpg" );
	texture.wrapS = THREE.RepeatWrapping;
	texture.wrapT = THREE.RepeatWrapping;
	var trunkGeo = newTreeGeometry(myTree);			
	var trunkMaterial = new THREE.MeshLambertMaterial( { 
		color: 0xdddddd, wireframe: false, 
		map: texture
		} );

	var trunkMesh = new THREE.Mesh(trunkGeo, trunkMaterial);
	group.add(trunkMesh);

	var twigsGeo = newTreeGeometry(myTree, true);
	var twigsMaterial = new THREE.MeshLambertMaterial( { 
		//color: 0xdddddd,
		wireframe: false,
		transparent:true,
		map: textureLoader.load( "./textures/branch1.png" ),
	} );
	var twigsMesh = new THREE.Mesh(twigsGeo, twigsMaterial);
	group.add(twigsMesh); 

	return group;
}

function drawLoopLine(vectors, scene){
	var material = new THREE.LineBasicMaterial({
		color: 0xffffff
	});

	var geometry = new THREE.Geometry();
	for(var i = 0; i < vectors.length; i++){
		geometry.vertices.push(
			new THREE.Vector3(vectors[i].x,vectors[i].y,vectors[i].z)
		);
	}
	
	var line = new THREE.LineLoop(geometry, material);
	scene.add(line);
}

function drawSegLine(vectors){
	
	var material = new THREE.LineBasicMaterial({
		color: 0xffffff
	});
	
	var geometry = new THREE.Geometry();
	for(var i = 0; i < vectors.length; i++){
		geometry.vertices.push(
			new THREE.Vector3(vectors[i].x,vectors[i].y,vectors[i].z)
		);
	}
	
	var line = new THREE.LineSegments(geometry, material);
	return line;
}

function drawLine(vectors, scene){
	var material = new THREE.LineBasicMaterial({
		color: 0xffffff
	});

	var geometry = new THREE.Geometry();
	for(var i = 0; i < vectors.length; i++){
		geometry.vertices.push(
			new THREE.Vector3(vectors[i].x,vectors[i].y,vectors[i].z)
		);
	}
	
	var line = new THREE.Line(geometry, material);
	scene.add(line);
}

function circle(radius, point){
	
	var curve = new THREE.EllipseCurve(
		0,0,            // ax, aY
		radius, radius,           // xRadius, yRadius
		0,  2 * Math.PI,  // aStartAngle, aEndAngle
		false,            // aClockwise
		0                 // aRotation
	);

	var points = curve.getPoints( 50 );
	
	console.log(points[0]);
	
	var geometry = new THREE.BufferGeometry().setFromPoints( points );


	var material = new THREE.LineBasicMaterial( { color : 0xffffff } );
	var ellipse = new THREE.Line( geometry, material );
	
	ellipse.lookAt(new THREE.Vector3(0,1,0));
	
	return ellipse;
}

function circle_q(radius, point,start){
	var an = 2 * Math.PI / 4;
	
	var curve = new THREE.EllipseCurve(
		point.x,point.y,            // ax, aY
		radius, radius,           // xRadius, yRadius
		start*an,  (start+1)*an ,  // aStartAngle, aEndAngle
		false,            // aClockwise
		0                 // aRotation
	);

	var points = curve.getPoints( 50 );
	var geometry = new THREE.BufferGeometry().setFromPoints( points );


	var material = new THREE.LineBasicMaterial( { color : 0xffffff } );
	var ellipse = new THREE.Line( geometry, material );
	
	ellipse.lookAt(new THREE.Vector3(0,1,0));
	
	return ellipse;//scene.add( ellipse);
}

function circle_se(radius, point,start, end){
	var an = 2 * Math.PI / 4;
	
	var curve = new THREE.EllipseCurve(
		point.x,point.y,            // ax, aY
		radius, radius,           // xRadius, yRadius
		start,  end ,  // aStartAngle, aEndAngle
		false,            // aClockwise
		0                 // aRotation
	);

	var points = curve.getPoints( 50 );
	var geometry = new THREE.BufferGeometry().setFromPoints( points );


	var material = new THREE.LineBasicMaterial( { color : 0xffffff } );
	var ellipse = new THREE.Line( geometry, material );
	
	ellipse.lookAt(new THREE.Vector3(0,1,0));
	
	return ellipse;
}

function LinewithW(p1,p2, group){
	
	//var positions = [0,0,-35,  0,0,35];
	
	var geometry = new LineGeometry();
	geometry.setPositions( [0,0,-35,  0,0,35] );
	geometry.setColors( [255,255,255, 255,255,255 ]);

	var matLine = new LineMaterial( {
		
		color: 0xffffff,
		linewidth: 5, // in pixels
		vertexColors: THREE.VertexColors,
		//resolution:  // to be set by renderer, eventually
		dashed: false

	} );
	
	//very important
	matLine.resolution.set( window.innerWidth, window.innerHeight );

	var line = new Line2( geometry, matLine );
	line.computeLineDistances();
	line.scale.set( 1, 1, 1 );
	group.add( line );
	
}

export { drawLoopLine, drawSegLine , drawLine, circle,circle_q, LinewithW, circle_se, oneTree, grassGround, waterPool};