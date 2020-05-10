class Tree2{
    constructor(){

    }

    build(){
        var group = new THREE.Group();

        group.add(fork());
        //group.add(twigs());
        
        return group;
    }

    fork(){
        var geometry = new THREE.CylinderGeometry( 5, 5, 20, 32 );
        var material = new THREE.MeshBasicMaterial( {color: 0xffff00} );
        var cylinder = new THREE.Mesh( geometry, material );
    }

    twigs(){

    }
}