<template>
    <div>

        	<!--div style="background:rgba(2,9,17,0.8);color: #fff;
                        width:100%;
						line-height: 50px;height: 50px;position:absolute;left:0px;top:0px;" >
					
					<div style="color: #fff; 
							font-family: Helvetica Neue,Helvetica,Arial,sans-serif;
							font-weight: 400; line-height: 50px; height: 50px;float:left;">					 
						智慧机楼管理系统
					</div>

					<div style="float:right;">						
						{{currenttime}} <span style="color:grey">|</span> {{currentdate}}
					</div>

			</div-->

        <div id="allmap" style="/*height:calc(100% + 35px);width:52%;left:24%;*/height:100vh;width:100%;">
            <iframe src="./city/building.html" style="width:100%;height:100vh;border:none;overflow:hidden;"></iframe>
        </div>
        <!--div id="mappopwin">
            <div>今日能耗：{{todayPow}}KW/h</div>
            </p>
            <div>总能耗：{{totalPow}}KW/h</div>
        </div-->

        <div style="background:rgba(2,9,17,0.8);padding-left:10px;padding-right:14px;height:100%;
                width:24%;position:absolute;left:0px;top:0px;">
           
            <div class="box">
                <div class="horn"> <div class="lt"/><div class="rt"/><div class="rb"/><div class="lb"/></div>
                  <div class="box-header">
                        <img src="../../assets/title.png" style="vertical-align: middle;"/> 
                        节能率统计 
                        <span class="box-header-sub">/Energy saving efficiency</span>
                  </div>       
                  <div style="position:absolute;left:160px;top:50px;font-size:12px;color:white;">
                        <div>今日总能耗：53667.4KW</div>
                        <div >
                            <div style="float:left;margin-left:5px;margin-top:7px;width:55px;height:10px;background-color:rgba(61,255,244);"/>
                            <div style="float:left;margin-top:7px;width:50px;height:10px;background-color:#494A4C;"/>
                        </div>
                       
                        <div style="margin-top:30px;">今日节约能耗：26833.7KW</div>
                        <div>
                            <div style="float:left;margin-left:5px;margin-top:7px;width:55px;height:10px;background-color:rgba(61,255,244);"/>
                            <div style="float:left;margin-top:7px;width:50px;height:10px;background-color:#494A4C;"/>
                        </div>
                       
                  </div>          

                <div ref="certPie" class="chartcontainer" style=""/> 

            </div>

            <div class="box">
                <div class="horn"> <div class="lt"/><div class="rt"/><div class="rb"/><div class="lb"/></div>
                  <div class="box-header">
                    <img src="../../assets/title.png" style="vertical-align: middle;"/> 
                    总用电量 
                    <span class="box-header-sub" >/Total power consumption</span>
                    <span class="box-header-sub" style="float:right;">更多> </span>
                </div>   

                 <div ref="monthbar" class="chartcontainer" style=""></div>  

            </div>

            <div class="box">
                <div class="horn"> <div class="lt"/><div class="rt"/><div class="rb"/><div class="lb"/></div>
                  <div class="box-header">
                    <img src="../../assets/title.png" style="vertical-align: middle;"/> 
                    节电量 
                    <span class="box-header-sub">/Power saving</span>
                </div>   

                 <div ref="devicePie" class="chartcontainer"/> 

            </div>

            <div class="box">
                <div class="horn"> <div class="lt"/><div class="rt"/><div class="rb"/><div class="lb"/></div>
                  <div class="box-header">
                    <img src="../../assets/title.png" style="vertical-align: middle;"/> 
                    拉闸时长 
                    <span class="box-header-sub">/Braking time</span>
                </div>   

                <div ref="monthbar2" class="chartcontainer" style=""></div>    

            </div>

        
        </div>

        <!--    right -------------------->
        <div style="background:rgba(2,9,17,0.8);padding-left:9px;float:right;height:100%;
            width:26%;position:absolute;right:0px;top:0px;">

            <div class="box" style="height:36%;">
                <div class="horn"> <div class="lt"/><div class="rt"/><div class="rb"/><div class="lb"/></div>
                <div class="box-header">
                    <img src="../../assets/title.png" style="vertical-align: middle;"/> 
                    能源数据情况 
                    <span class="box-header-sub">/Energy data</span>
                </div>   

                <div class="box-body">

                    <div class="col1" style="float:left;padding-left:25px;text-align:left;font-size:2vh;">
                        <div style="font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本日总能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本日节省能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本日电费
                            <span class="" style="font-size:80%;"> （单位：元）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本日节省电费
                            <span class="" style="font-size:80%;"> （单位：元）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>
                        
                        
                    </div>

                    <div class="col2" style="position:absolute;right:0;text-align:left;font-size:2vh;">
                        <div style="font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本年总能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本年节省能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本年电费
                            <span class="" style="font-size:80%;"> （单位：元）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本年节省电费
                            <span class="" style="font-size:80%;"> （单位：元）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>                        
                    </div>
                   
                </div>
            </div>

            <div class="box" style="height:25%;">
                <div class="horn"> <div class="lt"/><div class="rt"/><div class="rb"/><div class="lb"/></div>
                <div class="box-header">
                    <img src="../../assets/title.png" style="vertical-align: middle;"/> 
                    用电量统计 
                    <span class="box-header-sub">/Power consumption statistics</span>
                </div>

                <div ref="dayline" class="chartcontainer" style="height:100%;"></div>  

            </div>

            <div class="box" style="height:35%;">
                <div class="horn"> <div class="lt"/><div class="rt"/><div class="rb"/><div class="lb"/></div>
                <div class="box-header">
                    <img src="../../assets/title.png" style="vertical-align: middle;"/> 
                    告警列表 
                    <span class="box-header-sub">/Alarm list</span>
                    <span class="box-header-sub" style="float:right;">更多> </span>
                </div>  
                <div style="padding-left:25px;padding-right:10px;">
                    <div class="tableheader">  
                        <span class="col">编号</span>
                        <span class="col" style="margin-left:50px;!important;">告警时间</span>
                        <span class="col" style="margin-left:70px;!important;">告警原因</span>
                    </div>
                     <div class="drow">  
                        <span  class="col">000000000001</span>
                        <span  class="col" style="margin-left:10px;">2020-04-20 14:41:12</span>
                        <span  class="col" style="margin-left:15px;">跳闸</span>
                    </div>
                     <div class="drow">  
                        <span  class="col">000000000002</span>
                        <span  class="col" style="margin-left:10px;">2020-04-20 14:41:12</span>
                        <span  class="col" style="margin-left:15px;">电压过载</span>
                    </div>
                     <div class="drow">  
                        <span  class="col">000000000003</span>
                        <span  class="col" style="margin-left:10px;">2020-04-20 14:41:12</span>
                        <span  class="col" style="margin-left:15px;">电流过载</span>
                    </div>
                     <div class="drow">  
                        <span  class="col">000000000004</span>
                        <span  class="col" style="margin-left:10px;">2020-04-20 14:41:12</span>
                        <span  class="col" style="margin-left:15px;">跳闸</span>
                    </div>
                    
                </div>             
            </div>
        </div><!-- end of right panel-->
      
        <!-- bottom -->
        <div class="bottom-panel">
            <el-button round class="panel-button" @click="nav('/device')">智能空开</el-button>
            <el-button round class="panel-button" @click="nav('/base')">基站分析</el-button>
            
        </div>
             
    </div>

</template>

<script>
import { mapGetters } from 'vuex';

import { AdminAPI } from '../../api';
import Filters from '../../common/js/filters';
import echarts from 'echarts';

import Mapoption from '../conf/baidumap';

import Echartconf from './echartsconf';


export default {
  data() {
		return {     
            currenttime:'',
			currentdate:'',     
            todayPow:789,
            totalPow:1234,
            todayDate:'',
            listLoading:false,

            onlineCount:100,
            offlineCount:10,
            onCount:100,
            offCount:10,

            top10:[
                    {'deviceName':'电表1','deviceNo':'000000000001','alarmTime':'2020-10-20 13:56:56','alarmReason':'跳闸'},
                    {'deviceName':'电表2','deviceNo':'000000000001','alarmTime':'2020-10-20 13:56:56','alarmReason':'过载'},
                    {'deviceName':'电表3','deviceNo':'000000000001','alarmTime':'2020-10-20 13:56:56','alarmReason':'超负荷'},
                    {'deviceName':'电表4','deviceNo':'000000000001','alarmTime':'2020-10-20 13:56:56','alarmReason':'跳闸'},
                    {'deviceName':'电表1','deviceNo':'000000000001','alarmTime':'2020-10-20 13:56:56','alarmReason':'跳闸'},
                ],

            months:[],
            days:[],
            dayLine:null,
            monthBar:null,
      }
  },
  filters: {
			dateFormat: Filters.dateFormat,
			scoreFormat:(v,f) => {return parseFloat(v).toFixed(2);}
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
    gettodayData(){
        AdminAPI.getOverview().then(({data}) => {
          if(data.status === 0){

            this.onlineCount = data.data.onlineCount;
            this.offlineCount = data.data.offlineCount;
            this.onCount = data.data.onCount;
            this.offCount = data.data.offCount;
            //this.top10 = data.data.top10;
            this.months = data.data.months;
            this.days = data.data.days;

            this.loadData();
          }					
				}).catch((err) => {
					this.$message.error('Error:'+ err);

				});
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

    
      loadData(){
        let cerPieOption = Echartconf.nestedBar(); 
        let certPie = echarts.init(this.$refs.certPie);
        certPie.setOption(cerPieOption,true);

        let devicePieOption = Echartconf.pie() ;     
        let devicePie = echarts.init(this.$refs.devicePie);
        devicePie.setOption(devicePieOption);

        //////
        let barOption = Echartconf.bar();
        this.monthBar = echarts.init(this.$refs.monthbar);
        this.monthBar.setOption(barOption);
        this.monthBar.on('click',this.onclickBar);

        let barOption2 = Echartconf.bar2();
        let monthBar2 = echarts.init(this.$refs.monthbar2);
        monthBar2.setOption(barOption2);        

        ///////
        let lineoption = Echartconf.lines();
        this.dayLine = echarts.init(this.$refs.dayline);
        this.dayLine.setOption(lineoption);
      },
      onclickBar(param){        
        this.getMonthDays(this.months[param.dataIndex].statDate);
      },
      getYM(v){
         var d =  new Date(v);        
         return (d.getFullYear()+'').substr(2,2)+"年"+(d.getMonth()+1) +"月";
      },
      getDay(v){
         var d =  new Date(v);
         return d.getDate()+'日';
      },
      loadMap(){
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
            marker.addEventListener("mouseover", function(evt) {   
                   var pwin = document.getElementById("mappopwin");
                   pwin.style.display='block';
                   pwin.style.top=evt.clientY +"px";
                   pwin.style.left=evt.clientX + "px";
                });
             marker.addEventListener("mouseout", function(evt) {   
                   var pwin = document.getElementById("mappopwin");
                   pwin.style.display='none';
                });

            map.centerAndZoom(new BMapGL.Point(107.111924,29.157861), 19);  // 初始化地图,设置中心点坐标和地图级别
            //map.centerAndZoom("nanjing",19);
            map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
            map.setHeading(64.5);
            map.setTilt(50);

      }
  },
  mounted() {
      /* */
    this.todayDate=Filters.dateFormat(new Date());
	//this.gettodayData();
    this.loadData();
    
    const _this = this;
    //this.loadMap();

    window.onresize = function windowResize () {
        _this.monthBar.resize();
    }

    setInterval(function(){
        var myDate=new Date();
        var m = myDate.getMinutes();
        var s = myDate.getSeconds();
        m = m < 10 ? '0' + m : m;
        s = s < 10 ? '0' + s : s;
        _this.currentdate = myDate.getFullYear() + '年'+ (myDate.getMonth() + 1) + '月' + myDate.getDate() + '日';
        _this.currenttime = myDate.getHours()+':'+ m  + ":" + s;				
    },1000);			
   
  }


}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.box{  
  height:24%;
  width:100%;
  margin-top:0.5vh;
  position: relative;

  .box-header{    
    text-align:left;
    height:5vh;
    line-height:30px;
    font-size:2vh;
    font-family:MicrosoftYaHei-Bold,MicrosoftYaHei;
    font-weight:bold;
    color:rgba(135,246,241,1);
    padding-left:10px;
    position:relative;
    .box-header-sub {
        font-size:1.5vh;
        font-family:Tahoma;
        color:rgba(135,246,241,1);
        line-height:30px;
        margin-left:1vh;
    }
  }
   .horn{
        position: absolute;
        width: 100%;
        height: 100%;
    }
    .horn>div{
        width: 7px;
        height: 7px;
        position:absolute;
    }
    .horn .lt{
        border-top: 1px solid #87b2fb;
        border-left: 1px solid #87b2fb;
        left: 0px;
        top: 0px;
    }
    .horn .rt{
        border-top: 1px solid #87b2fb;
        border-right: 1px solid #87b2fb;
        right: 0px;
        top: 0px;
    }
    .horn .rb{
        border-bottom:1px solid #87b2fb;
        border-right: 1px solid #87b2fb;
        right: 0px;
        bottom: 0px;
    }
    .horn .lb{
        border-bottom:1px solid #87b2fb;
        border-left: 1px solid #87b2fb;
        left: 0px;
        bottom: 0px;
    }
}

.chartcontainer{        
    height:95%;
    width:100%;    
    position:relative;
    top:-20px;
}

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
.tableheader{
    color:#fff;
    text-align:left;
     padding-left:25px;
    font-size:15px;
    margin-bottom:5px;
}
.drow{
    height:30px; 
    line-height:30px; 
    text-align:left;
    padding-left:25px;
    background:rgba(7,13,27,0.8);color:#fff;   
     font-size:12px;
}
.col1{

}
.col2{
    margin-left:50px;
}

.tableheader{
    color:#c2deff; 
    text-align:left;
     padding-left:25px;
    font-size:14px;
    font-weight:600;
    margin-bottom:5px;
    height:20px;
    background:linear-gradient(90deg,rgba(20,74,148,1) 0%,rgba(27,71,145,1) 100%);
    border-radius:2px;
    border:1px solid rgba(44,87,159,1);
}

.drow{
    margin-top:10px;
    height:24px; 
    line-height:24px; 
    text-align:left;
    padding-left:10px;
    background:#112244;
    
    color:#c2deff; 
    font-size:12px;
}
.bottom-panel{
    
    position:absolute;
    left:0;
    bottom:10px;
    height:50px;
    width:100%;
}
.panel-button{
    border:none;
    background-color:rgba(2,9,17,0.8);
    color:white;
    height:50px;
    font-size:20px;
}

</style>
