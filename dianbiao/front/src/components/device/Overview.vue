<template>
    <div style="overflow:hidden; height:100%; position:relative;" >

        <div id="allmap" style="height:calc(100% + 35px);"></div>
        <div id="mappopwin">
            <div>今日能耗：{{todayPow}}KW/h</div>
            </p>
            <div>总能耗：{{totalPow}}KW/h</div>
        </div>


        <div style="background-color:#000;
                    position:absolute;left:0px;top:0px;
                    width:279px;height:100%;">
            
            <div  class="box">  
                 <div class="horn">
                    <div class="lt"></div>
                    <div class="rt"></div>
                    <div class="rb"></div>
                    <div class="lb"></div>
                </div>                                           
                <div class="box-header"><i class="el-icon-share"/> 在线统计</div>   
                <div ref="certPie" class="chartcontainer" style=""/>                
            </div>

             <div  class="box">    
                <div class="horn">
                    <div class="lt"></div>
                    <div class="rt"></div>
                    <div class="rb"></div>
                    <div class="lb"></div>
                </div>                  
                <div class="box-header" style=""><i class="el-icon-share"/> 能耗统计</div>   
                <div ref="monthbar" class="chartcontainer" style=""></div>    
            </div>

            <div  class="box">   
                 <div class="horn">
                    <div class="lt"></div>
                    <div class="rt"></div>
                    <div class="rb"></div>
                    <div class="lb"></div>
                </div>                   
                <div class="box-header"><i class="el-icon-share"/> 拉合闸统计</div>   
                <div ref="devicePie" class="chartcontainer"/>        
            </div>

             <div  class="box">    
                 <div class="horn">
                    <div class="lt"></div>
                    <div class="rt"></div>
                    <div class="rb"></div>
                    <div class="lb"></div>
                </div>                  
                <div class="box-header" style=""><i class="el-icon-share"/> 合闸时长</div>   
                <div ref="monthbar2" class="chartcontainer" style=""></div>    
            </div>

        </div>

        <div style="background-color:#000;
                    position:absolute;right:0px;top:0px;
                    width:279px;height:100%;">

            <div  class="box" style="height:35% !important;">   
                <div class="horn">
                    <div class="lt"></div>
                    <div class="rt"></div>
                    <div class="rb"></div>
                    <div class="lb"></div>
                </div>            
                <div class="box-header"><i class="el-icon-share"/> 设备数据情况</div>   
                <div style="color:white;font-size:13px;text-align:left;padding-left:20px;">
                     本日总能耗  <span style="margin-left:70px;">本年总能耗</span></div>
                <div style="color:blue;font-size:15px;text-align:left;padding-left:40px;margin-top:7px;">
                     1588.8  <span style="margin-left:100px;">16957.1</span></div>

                <div style="color:white;font-size:13px;text-align:left;padding-left:20px;">
                     本日节省能耗  <span style="margin-left:70px;">本年节省能耗</span></div>
                <div style="color:blue;font-size:15px;text-align:left;padding-left:40px;margin-top:7px;">
                     1588.8  <span style="margin-left:100px;">16957.1</span></div>

                <div style="color:white;font-size:13px;text-align:left;padding-left:20px;">
                     本日总电费  <span style="margin-left:70px;">本年总电费</span></div>
                <div style="color:blue;font-size:15px;text-align:left;padding-left:40px;margin-top:7px;">
                     1588.8  <span style="margin-left:100px;">16957.1</span></div>

                <div style="color:white;font-size:13px;text-align:left;padding-left:20px;">
                     本日节省电费  <span style="margin-left:70px;">本年节省电费</span></div>
                <div style="color:blue;font-size:15px;text-align:left;padding-left:40px;margin-top:7px;">
                     1588.8  <span style="margin-left:100px;">16957.1</span></div>
                
            </div>


            <div  class="box" >   
                <div class="horn">
                    <div class="lt"></div>
                    <div class="rt"></div>
                    <div class="rb"></div>
                    <div class="lb"></div>
                </div>            
                <div class="box-header"><i class="el-icon-share"/> 能耗趋势</div>   
                <div ref="dayline" class="chartcontainer" style="height:100%;"></div>  
            </div>

            <div  class="box" style="height:45% !important;">  
                 <div class="horn">
                    <div class="lt"></div>
                    <div class="rt"></div>
                    <div class="rb"></div>
                    <div class="lb"></div>
                </div>        
                <div class="box-header"><i class="el-icon-share"/> TOP排名</div> 
                <el-table :data="top10" ref="table" 
                            :header-cell-style="{background:'rgb(7,13,27)',color:'#fff'}"
                            :row-style="{background:'rgb(7,13,27)',color:'#fff'}"
                            size="mini" row-class-name="tablerow">
                            
                    			
                    <el-table-column  prop="deviceNo" label="编号" width="130" ></el-table-column>
                    
                    <el-table-column  prop="actionEnergy" label="电量(KW)">
                    </el-table-column>                              
                </el-table>                    
            </div>

        </div>
             
    </div>

</template>

<script>
import { mapGetters } from 'vuex';

import { AdminAPI } from '../../api';
import Filters from '../../common/js/filters';
import echarts from 'echarts';

import Mapoption from '../conf/baidumap';

export default {
  data() {
			return {
                todayPow:789,
                totalPow:1234,
        todayDate:'',
        listLoading:false,

        onlineCount:100,
        offlineCount:10,
        onCount:100,
        offCount:10,

        top10:[
                {'deviceName':'电表1','deviceNo':'000000000001','actionEnergy':69.2,'duration':108},
                {'deviceName':'电表2','deviceNo':'000000000001','actionEnergy':69.2,'duration':108},
                {'deviceName':'电表3','deviceNo':'000000000001','actionEnergy':69.2,'duration':108},
                {'deviceName':'电表4','deviceNo':'000000000001','actionEnergy':69.2,'duration':108},
                {'deviceName':'电表1','deviceNo':'000000000001','actionEnergy':69.2,'duration':108},
            ],

        months:[],
        days:[],
        dayLine:null,
      }
  },
  filters: {
			dateFormat: Filters.dateFormat,
			scoreFormat:(v,f) => {return parseFloat(v).toFixed(2);}
	},
  methods:{
   
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

      lines(){
          return {
               grid: {
                    x: 35,
                    y: 35,
                    x2: 30,
                    y2: 35
                },
              color: ['#1cffea', '#006699', '#4cabce', '#e5323e'],
              legend: {
                        data: ['电量'],
                        show:false
                },
               xAxis: {
                   axisLine:{
                       lineStyle:{
                           color:"#ffffff"
                       }
                   },
                    boundaryGap: false,
                    type: 'category',
                    data: ['1日', '2日', '3日', '4日', '5日', '6日', '7日']
                },
                yAxis: {
                    axisLine:{
                       lineStyle:{
                           color:"#ffffff"
                       }
                   },
                    splitLine: {
                        show:false,
                    },
                    type: 'value'
                },
                series: [{
                    name:'电量',
                    data: [120, 500, 650, 680, 670, 710, 830],
                    type: 'line',
                     areaStyle: {normal: {
                        color: 
                        new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                                offset: 0,
                                color: '#00c1de'
                            }, {
                                offset: 1,
                                color: 'rgba(0,0,0,0)'
                            }]),
                    }}
                 }]
          }
      },
      bar(){
          return {
                color: ['#1cffea', '#006699', '#4cabce', '#e5323e'],
                grid: {
                    x: 35,
                    y: 35,
                    x2: 30,
                    y2: 35
                },
                legend: {
                        data: ['能耗'],
                        show:false
                },
                xAxis: {
                     splitLine: {
                        show:false,
                    },
                    axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#ffffff'
                            }
                    },
                    axisLine:{
                       lineStyle:{
                           color:"#ffffff"
                       }
                   },
                    type: 'category',
                    data: ['1月', '2月', '3月', '4月', '5月', '6月']
                },
                yAxis: {
                    axisLine:{
                       lineStyle:{
                           color:"#ffffff"
                       }
                    },
                    splitLine:{show:false},
                    type: 'value',
                    
                     axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#ffffff'
                            }
                    },
                },
                series: [
                    {
                        name:'电量',
                        data: [120, 500, 150, 80, 70, 110],
                        type: 'bar',
                        barWidth: 20,
                    }
                ]
          };
      },
      pie(){
        return {
                        legend: {
                            orient: 'vertical',
                            x: 'right',
                            data:[]
                        },
                        title:{
                          text:'',
                          top:'top',
                          left:'right',                         
                        },
                        series: {
                            color:['#00a65a','#f56954'],
                            type: 'pie',
                            radius: ['40%', '80%'],
                            avoidLabelOverlap: true,
                            label: {
                                color:'#fff',
                                normal: {
                                    show: true,
                                    position: 'inside',
                                    formatter: '{c}',
                                },
                                emphasis: {
                                    show: false,
                                    textStyle: {
                                        fontSize: '30',
                                        fontWeight: 'bold'
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data: [
                            ]
                        }
                    };
      },
      loadData(){
        let cerPieOption = this.pie();
        let devicePieOption = this.pie();      
        
        let certPie = echarts.init(this.$refs.certPie);
        let devicePie = echarts.init(this.$refs.devicePie);

        cerPieOption.series.data=[
          {name:"在线",value: this.onlineCount},
          {name:"离线",value:this.offlineCount}]
        cerPieOption.legend.data=["在线","离线"];
       
        devicePieOption.series.data=[
          {name:"合闸",value:this.onCount},
          {name:"拉闸",value:this.offCount}];
        devicePieOption.legend.data=["合闸","拉闸"];
       
        certPie.setOption(cerPieOption);
        devicePie.setOption(devicePieOption);

        //////
        let barOption = this.bar();
        
        /*barOption.xAxis.data=[];
        barOption.series[0].data=[];
        barOption.series[1].data=[];
        for(var i = 0; i < this.months.length; i++){
            var tmp = this.months[i];
             barOption.xAxis.data.push(this.getYM(tmp.statDate));
             barOption.series[0].data.push(tmp.energy);
             barOption.series[1].data.push(tmp.ontime);
        }*/

        let monthBar = echarts.init(this.$refs.monthbar);
        monthBar.setOption(barOption);
        monthBar.on('click',this.onclickBar);

         let barOption2 = this.bar();
         barOption2.xAxis.type="value";
         barOption2.yAxis.type="category";
         barOption2.series[0].barWidth=5;
         barOption2.yAxis.data = barOption2.xAxis.data;
        
        /*barOption.xAxis.data=[];
        barOption.series[0].data=[];
        barOption.series[1].data=[];
        for(var i = 0; i < this.months.length; i++){
            var tmp = this.months[i];
             barOption.xAxis.data.push(this.getYM(tmp.statDate));
             barOption.series[0].data.push(tmp.energy);
             barOption.series[1].data.push(tmp.ontime);
        }*/
        let monthBar2 = echarts.init(this.$refs.monthbar2);
        monthBar2.setOption(barOption2);        

        ///////
        let lineoption = this.lines();
       /*
        lineoption.xAxis.data=[];
        lineoption.series[0].data=[];
        lineoption.series[1].data=[];
        for(var i = 0; i < this.days.length; i++){
            var tmp = this.days[i];
             lineoption.xAxis.data.push(this.getDay(tmp.statDate));
             lineoption.series[0].data.push(tmp.energy);
             lineoption.series[1].data.push(tmp.ontime);
        }*/
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

            var point = new BMapGL.Point(116.280190, 40.049191);
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

            map.centerAndZoom(new BMapGL.Point(116.280190, 40.049191), 19);  // 初始化地图,设置中心点坐标和地图级别
            //map.centerAndZoom("nanjing",19);
            map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
            map.setHeading(64.5);
            map.setTilt(50);

      }
  },
  mounted() {
    this.todayDate=Filters.dateFormat(new Date());
	this.gettodayData();
    this.loadData();
    
    this.loadMap();
    
  }


}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.box{  
  height:24%;
  width:100%;
  margin-top:3px;
  position: relative;
  background-color:rgb(7,13,27); 
  font-size:12px;
  .box-header{    
     color:rgb(246,248,250);
     text-align:left;
      padding:5px;
  }
   .horn{
        position: absolute;
        width: 100%;
        height: 100%;
    }
    .horn>div{
        width: 10px;
        height: 10px;
        position:absolute;
    }
    .horn .lt{
        border-top: 2px solid #00d3e7;
        border-left: 2px solid #00d3e7;
        left: 0px;
        top: 0px;
    }
    .horn .rt{
        border-top: 2px solid #00d3e7;
        border-right: 2px solid #00d3e7;
        right: 0px;
        top: 0px;
    }
    .horn .rb{
        border-bottom:2px solid #00d3e7;
        border-right: 2px solid #00d3e7;
        right: 0px;
        bottom: 0px;
    }
    .horn .lb{
        border-bottom:2px solid #00d3e7;
        border-left: 2px solid #00d3e7;
        left: 0px;
        bottom: 0px;
    }
}

.chartcontainer{        
        height:100%;
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

.el-table__header-wrappe, .el-table__header, .el-table, .el-table__expanded-cell {
    background-color: red !important;
}

.el-table th, .el-table tr {
    background-color: transparent;
}
.tablerow{
    background-color: transparent;
}

</style>
