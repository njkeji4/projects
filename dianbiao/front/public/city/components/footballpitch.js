
import * as THREE from '../3js/build/three.module.js';
import * as Util from './utils.js';

var FootballPitch = function(){
	this.group = new THREE.Group;
	this.x = 110;
	this.y= 75;
	
	this.textureLoader = new THREE.TextureLoader();//.load( "../texture/checkerboard.jpg" );
}

FootballPitch.prototype.newPitch = function(){
	
	this.createGround(this.x+5, this.y+5);
	
	this.drawLine();
	
	this.gate(this.group);
	
	return this.group;
	
}


FootballPitch.prototype.createGround = function(x,y){
	
	var material	= new THREE.MeshLambertMaterial({
		color	: 0x2d5517
	})
	
	var ground	= new THREE.Mesh(new THREE.PlaneGeometry( x, y), material)
	ground.lookAt(new THREE.Vector3(0,1,0));//rotate

	this.group.add(ground);
}

FootballPitch.prototype.drawLine = function(){
	
	var gl = 7.32;
	var gh = 2.44;
	var l = 110;
	var w = 75
	
	var layh = 0;
	
	var positions = [
		{x:-l/2, y:layh, z:-37.5}, {x:l/2, y:layh, z:-37.5}, 
		{x:55, y:layh, z:-37.5},  {x:55,y:layh,z:37.5},
		{x:55,y:layh,z:37.5},     {x:-55, y:layh, z:37.5},
		{x:-55, y:layh, z:37.5},  {x:-55, y:layh, z:-37.5},
		
		//middle
		{x:0,y:layh,z:-w/2},{x:0,y:layh,z:w/2},
		
		//ban area
		{x:-55,y:layh,z:-20.16},{x:-38.5,y:layh,z:-20.16},
		{x:-38.5,y:layh,z:-20.16},{x:-38.5,y:layh,z:20.16},
		{x:-38.5,y:layh,z:20.16},{x:-55,y:layh,z:20.16},
		
		{x:-55,y:layh,z:-9.16},{x:-49.5,y:layh,z:-9.16},
		{x:-49.5,y:layh,z:-9.16},{x:-49.5,y:layh,z:9.16},
		{x:-49.5,y:layh,z:9.16},{x:-55,y:layh,z:9.16},
		
		{x:55,y:layh,z:-20.16},{x:38.5,y:layh,z:-20.16},
		{x:38.5,y:layh,z:-20.16},{x:38.5,y:layh,z:20.16},
		{x:38.5,y:layh,z:20.16},{x:55,y:layh,z:20.16},
		
		{x:55,y:layh,z:-9.16},{x:49.5,y:layh,z:-9.16},
		{x:49.5,y:layh,z:-9.16},{x:49.5,y:layh,z:9.16},
		{x:49.5,y:layh,z:9.16},{x:55,y:layh,z:9.16},
	];
	
	var line = Util.drawSegLine(positions);
	
	this.group.add(line);
	
	//middle circle
	this.group.add(Util.circle(9.15,{x:0,y:0}));
	
	//
	this.group.add(Util.circle_q(1,{x:-55,y:37.5}, 3));
	this.group.add(Util.circle_q(1,{x:-55,y:-37.5}, 0));
	this.group.add(Util.circle_q(1,{x:55,y:37.5}, 2));
	this.group.add(Util.circle_q(1,{x:55,y:-37.5}, 1));
	
	this.group.add(Util.circle_se(9.15, {x:-44,y:0}, 0, Math.PI * 0.3));
	this.group.add(Util.circle_se(9.15, {x:-44,y:0}, 1.7*Math.PI, 2*Math.PI));
	
	this.group.add(Util.circle_se(9.15, {x:44,y:0}, 0.7*Math.PI, 1.3*Math.PI));
	
	
}

FootballPitch.prototype.gate = function(geo){
	var group = new THREE.Group();
	group.renderOrder = 11;
	
	var poleGeo = new THREE.BoxBufferGeometry( 0.1, 2.44, 0.1 );
	var beamGeo = new THREE.BoxBufferGeometry( 0.1, 0.1, 7.32 );
	var poleMat = new THREE.MeshLambertMaterial();

	var mesh = new THREE.Mesh( poleGeo, poleMat );
	mesh.position.x = - 55;
	mesh.position.y = 1.22 ;
	mesh.position.z = - 3.66;
	mesh.receiveShadow = true;
	mesh.castShadow = true;
	group.add( mesh );
	
	var mesh = new THREE.Mesh( poleGeo, poleMat );
	mesh.position.x = - 55;
	mesh.position.y = 1.22 ;
	mesh.position.z =  3.66;
	mesh.receiveShadow = true;
	mesh.castShadow = true;
	group.add( mesh );

	var mesh = new THREE.Mesh( beamGeo, poleMat );
	mesh.position.y = 2.44;
	mesh.position.x = -55;
	mesh.position.z = 0 ;
	mesh.receiveShadow = true;
	mesh.castShadow = true;
	group.add( mesh );
	
	var mesh = new THREE.Mesh( poleGeo, poleMat );
	mesh.position.x =  55;
	mesh.position.y = 1.22 ;
	mesh.position.z = - 3.66;
	mesh.receiveShadow = true;
	mesh.castShadow = true;
	group.add( mesh );
	
	var mesh = new THREE.Mesh( poleGeo, poleMat );
	mesh.position.x =  55;
	mesh.position.y = 1.22 ;
	mesh.position.z =  3.66;
	mesh.receiveShadow = true;
	mesh.castShadow = true;
	group.add( mesh );

	var mesh = new THREE.Mesh( beamGeo, poleMat );
	mesh.position.y = 2.44;
	mesh.position.x = 55;
	mesh.position.z = 0 ;
	mesh.receiveShadow = true;
	mesh.castShadow = true;
	group.add( mesh );
	
	geo.add(group);
}

export { FootballPitch };