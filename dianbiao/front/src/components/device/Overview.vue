<template>
    <div style="overflow:hidden; height:100%; position:relative;background:rgba(2,9,17,1);" >


        <div id="allmap" style="height:calc(100% + 35px);"></div>
        <div id="mappopwin">
            <div>今日能耗：{{todayPow}}KW/h</div>
            </p>
            <div>总能耗：{{totalPow}}KW/h</div>
        </div>


        <div style="background:rgba(2,9,17,1);padding-left:10px;padding-right:14px;height:100%;
                width:24%;position:absolute;left:0px;top:0px;">
           
            <div class="box">
                <div class="horn"> <div class="lt"/><div class="rt"/><div class="rb"/><div class="lb"/></div>
                  <div class="box-header">
                        <img src="../../assets/title.png" style="vertical-align: middle;"/> 
                        节能率统计 
                        <span class="box-header-sub">/Energy saving efficiency</span>
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
        <div style="background:rgba(2,9,17,1);padding-left:9px;float:right;height:100%;
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
                            本日总能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本日总能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本日总能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>
                        
                        
                    </div>

                    <div class="col2" style="position:absolute;right:0;text-align:left;font-size:2vh;">
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
                            本日总能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本日总能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
                        </div>

                        <div style="font-size:120%;font-family:Verdana;color:rgba(24,117,201,1);">
                            1688.9 
                            <i class="el-icon-caret-top"/>
                            <span style="font-size:80%;font-family:Verdana;color:rgba(255,255,255,1);">3.6%</span>
                        </div>

                         <div style="margin-top:2vh;font-size:90%;font-family:MicrosoftYaHei;color:rgba(255,255,255,1);">
                            本日总能耗
                            <span class="" style="font-size:80%;"> （单位：kwh）</span>
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
                </div>   
                 <div class="box-body">
                    <el-table :data="top10" style="color:#c2deff;"
                        :row-style="{background:'#112244',height:'10px', 'margin-top':'10px'}"
                        :cell-style="{'font-size':'10px',padding:'5px',border:'none'}"
                        :header-row-class-name="alarm-header"
                        :header-cell-style="{'font-size':'10px',background:'#2C579F',color:'#c2deff',height:'10px !important'}"
                    >
                        <el-table-column prop="deviceName" label="设备名称" width="100"/>
                         <el-table-column prop="alarmReason" label="告警原因" width="100" />
                         <el-table-column prop="alarmTime" label="告警时间" />
                       
                    </el-table>
                </div> 
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
            monthBar:null,
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
                                color: '#c3eaff'
                            }
                    },
                    axisLine:{
                       lineStyle:{
                           color:"#0B50AC"
                       }
                   },
                   axisTick:{
                        show:false
                   },
                    type: 'category',
                    data: ['1月', '2月', '3月', '4月', '5月', '6月','6月','6月']
                },
                yAxis: {
                    axisLine:{
                       lineStyle:{
                           color:"#0B50AC"
                       }
                    },
                    axisTick:{
                        show:false
                    },
                    splitLine:{
                        show:true,
                        lineStyle:{
                             color:['#0F4E87'],
                            type:'dotted'
                        }
                    },
                    type: 'value',
                    
                     axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#c3eaff'
                            }
                    },
                },
                series: [
                    {
                        name:'电量',
                        data: [950, 800, 500, 800, 500, 400,210,150],
                        type: 'bar',
                        itemStyle: {			// 颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,     //代表从正上方开始
                                [
                                    {offset: 0, color: '#0CC8FF'},
                                    
                                    {offset: 1, color: '#258FFF'}
                                ]
                            )
                        },
                        barWidth: 20,
                    }
                ]
          };
      },
      pie(){
        return {
                        legend: {
                            textStyle:{
                                color:'#7df5f9',
                                fontFamily:'MicrosoftYaHei',
                                fontSize:10
                            },
                            orient: 'vertical',
                            right:20,
                            top:10,
                            
                            formatter: '{name}'
                        },
                        title:{
                          text:'',
                          top:'top',
                          left:'right',                         
                        },
                        series: {
                            color:['#0E59FF','#27ECDC','#CC00FD','#FF73B8','#A5EF67','#FCA24A'],
                            type: 'pie',
                            radius: ['60%', '80%'],
                            avoidLabelOverlap: true,
                            center:['30%','50%'],
                            label: {
                                color:'#fff',
                                normal: {
                                    show: false,
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
                                {name:"一组   862KW",value:862},
                                {name:"二组   792KW",value:792},
                                {name:"三组   626KW",value:626},
                                {name:"四组   708KW",value:708},
                                {name:"五组   543KW",value:543},
                              
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
       
        //devicePieOption.series.data=[
        //  {name:"合闸",value:this.onCount},
        //  {name:"拉闸",value:this.offCount}];
        //devicePieOption.legend.data=["合闸","拉闸"];
       
        certPie.setOption(cerPieOption,true);
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

        this.monthBar = echarts.init(this.$refs.monthbar);
        this.monthBar.setOption(barOption);
        this.monthBar.on('click',this.onclickBar);

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
      /* */
    this.todayDate=Filters.dateFormat(new Date());
	this.gettodayData();
    this.loadData();
    
    const _this = this;
    //this.loadMap();

    window.onresize = function windowResize () {
        _this.monthBar.resize();
    }
   
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
.tableheader{
    color:#fff;
    text-align:left;
     padding-left:25px;
    font-size:15px;
    margin-bottom:5px;
}
.srow{
    text-align:left;
     padding-left:25px;
    background:rgba(22,28,52,0.8);color:#fff;
    height:30px; 
    line-height:30px; 
     font-size:12px; 
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

</style>
