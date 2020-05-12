<template>
    <div style="width:100%;height:100vh;overflow:hidden;">

        	<!--div style="background:rgba(2,9,17,0.8);color: #fff;
                        width:100%;
						line-height: 50px;height: 50px;position:absolute;left:0px;top:0px;" >
					
					<div style="color: #fff; 
							font-family: Helvetica Neue,Helvetica,Arial,sans-serif;
							font-weight: 400; line-height: 50px; height: 50px;float:left;">					 
						智慧机房管理系统
					</div>

					<div style="float:right;">						
						{{currenttime}} <span style="color:grey">|</span> {{currentdate}}
					</div>

			</div-->

        <div id="allmap" style="height:110%;width:100%;left:0;">
        </div>       
      
        <div id="mappopwin">
            <div>今日能耗：{{todayPow}}KW/h</div>
            </p>
            <div>总能耗：{{totalPow}}KW/h</div>
        </div>

         <!--iframe src="./city/building.html" style="width:100%;height:100vh;border:none;overflow:hidden;">
        </iframe-->

        <Stat></Stat>

             
    </div>

</template>

<script>

import Mapoption from '../conf/baidumap';

import Stat from './stat';

export default {

  components: {
		     Stat
	    },
  data() {        
		return {     
            currenttime:'',
			currentdate:'',     
           

            months:[],
            days:[],
            dayLine:null,
            monthBar:null,
      }
  },
 
  methods:{   
    controlPannel(){
         this.$router.push({
                    path: '/device'
                  });
    } ,
    nav(path) {	
		this.$router.push(path);
	},
   
      getMonthDays(yearmonth){
        AdminAPI.getOverviewMonthdays(yearmonth).then(({data}) => {
                    if(data.status === 0){                        
                        this.days = data.data.days;
                        let lineoption = this.lines();           
                        lineoption.xAxis.data=[];
                        lineoption.series[0].data=[];
                        lineoption.series[1].data=[];
                        for(var i = 0; i < this.days.length; i++){
                            var tmp = this.days[i];
                            lineoption.xAxis.data.push(this.getDay(tmp.statDate));
                            lineoption.series[0].data.push(tmp.energy);
                            lineoption.series[1].data.push(tmp.ontime);
                        }

                        this.dayLine.setOption(lineoption);
                    }					
				}).catch((err) => {
					this.$message.error('Error:'+ err);

				});
      },

      loadMap(_this){
          var map = new BMapGL.Map("allmap");    // 创建Map实例
            map.setMapStyleV2({styleJson:Mapoption.styleJson});
         


            var myIcon = new BMapGL.Icon(
                            require("../../assets/1.png"),
                            new BMapGL.Size(48, 48)
                            );

            var point = new BMapGL.Point(107.111924,29.157861);
            var marker = new BMapGL.Marker(point, {icon:myIcon});
            map.addOverlay(marker);
            
			 
            //marker.setTitle("example");
            marker.addEventListener("click", function(evt) {   
                  _this.nav("/path");
            });

           /* marker.addEventListener("mouseover", function(evt) {   
                   var pwin = document.getElementById("mappopwin");
                   pwin.style.display='block';
                   pwin.style.top=evt.clientY +"px";
                   pwin.style.left=evt.clientX + "px";
                });
             marker.addEventListener("mouseout", function(evt) {   
                   var pwin = document.getElementById("mappopwin");
                   pwin.style.display='none';
                });*/

            map.centerAndZoom(new BMapGL.Point(107.111924,29.157861), 19);  // 初始化地图,设置中心点坐标和地图级别
            //map.centerAndZoom("nanjing",19);
            map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
            map.setHeading(64.5);
            map.setTilt(50);

      }
  },
  mounted() {   
    var _this = this;
    this.loadMap(_this);   
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

#mappopwin{
    background-color:rgb(18,43,60);
    box-shadow: 2px 2px 2px 2px #888888 inset;
    position:fixed; 
    top:100px;left:100px;
    color:white;
    font-size:12px;
    width:150px;
    height:60px;
    padding-top:7px;
    display:none;
}


</style>
