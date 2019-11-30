<template>
        <el-row class="" :gutter="20" style="height:100%;background: radial-gradient(circle,#081c59,#083171);">
          <el-col :span="12" class="" style="height:100%;">
              <el-row style="height:50%;">
                 <div ref="certPie" class="chartcontainer"></div>
                  <div ref="devicePie" class="chartcontainer"/>
                   
              </el-row>
              <el-row style="height:50%;">
                  <div ref="groupBar" class="chartcontainer2"/>
              </el-row>
           </el-col>
          <el-col :span="12" class="" style="height:100%;position:relative;">
               <div ref="map" class="mapcontainer"></div>
               <div style="color:white;position:absolute;left:20px;top:10px;">
                  <el-button type="success" circle></el-button> <span style="line-height:26px;vertical-align:middle;"> 在线:{{onlineCount}}</span>                
                </div>
                <div style="color:white;position:absolute;left:20px;top:50px;">                  
                  <el-button type="danger" circle></el-button><span style="line-height:26px;vertical-align:middle;"> 离线:{{offlineCount}}</span>
                </div>
          </el-col>
        </el-row>
</template>

<script>
import { mapGetters } from 'vuex';

import { AdminAPI } from '../../api';
import Filters from '../../common/js/filters';
import echarts from 'echarts';
import Qiannan from '../../assets/522700.json'
import CustomChart from '../../common/js/CustomChart';

export default {
  data() {
			return {
        listLoading:false,
        cerCount:100,
        cersuccCount:9000,
        cerfailCount:1000,

        usedcerCount:10000,
        usedcersuccCount:900,
        usedcerfailCount:10,

        deviceCount:100,
        onlineCount:90,
        offlineCount:10,

        alarmCount:5,
        usedalarmCount:50,

        devices:[{name:'test',status:0,lanlat:'107.736419707031,26.8329177070313'},
          {name:'test',status:1,lanlat:'106.242139921875,25.7986794257813'}],
        deviceGroups:[{name:'g1',succ:10,fail:20,alarm:1},{name:'g2',succ:10,fail:20,alarm:1}],

        pageSize: 10,
        page: 1,
        total: 0,

        checkdata:[ ]
      }
  },
  filters: {
			dateFormat: Filters.dateFormat,
			scoreFormat:(v,f) => {return parseFloat(v).toFixed(2);}
	},
  methods:{

      handleSizeChange(size) {
				//this.pageSize = size;
			  //this.handleCurrentChange(1);
			},
			handleCurrentChange(val) {
				//this.page = val;
				//this.getDeviceList();
			},
      gettodayData(){
        AdminAPI.getTodayData().then(({data}) => {
          if(data.status === 0){
            //this.checkdata = data.data.checks;

            this.cersuccCount = data.data.successCount;
            this.cerfailCount = data.data.failCount;
            this.cerCount = this.cersuccCount + this.cerfailCount;
            this.onlineCount = data.data.online;
            this.offlineCount = data.data.offline;
            this.deviceCount = this.onlineCount+this.offlineCount;

            this.alarmCount = data.data.alarmCount;

            this.usedcersuccCount = data.data.totalSuccess;
            this.usedcerfailCount = data.data.totalFail;
            this.usedcerCount = this.usedcersuccCount + this.usedcerfailCount;
            this.usedalarmCount = data.data.totalAlarmCount;

            this.devices= data.data.devices;
            this.deviceGroups = data.data.groups;
          }					
				}).catch((err) => {
					this.$message.error('Error', err);

				});
      },
      loadPie(){
        let cerPieOption = CustomChart.pie();//this.pie();
        let devicePieOption = CustomChart.pie();//this.pie();
        
        let certPie = echarts.init(this.$refs.certPie);
        let devicePie = echarts.init(this.$refs.devicePie);

        cerPieOption.series.data=[
          {name:"成功",value: this.usedcersuccCount},
          {name:"失败",value:this.usedcerfailCount},
          {name:"告警",value:this.usedalarmCount}];
        cerPieOption.legend.data=["成功","失败","告警"];
        cerPieOption.title.text="总计";

        devicePieOption.series.data=[
          {name:"成功",value: this.cersuccCount},
          {name:"失败",value:this.cerfailCount},
          {name:"告警",value:this.alarmCount}];
        devicePieOption.legend.data=["成功","失败","告警"];
        devicePieOption.title.text="今日";

        certPie.setOption(cerPieOption);
        devicePie.setOption(devicePieOption);

        
      },
      loadMap(){
             var offline = {
                name: 'offline',
                type: 'scatter',
                coordinateSystem: 'geo',
                data: [
                    {name:'one', value:[107.736419707031,26.8329177070313,1]}                   
                ],
                symbolSize: 20,
                label: {
                    normal: {
                        formatter:'{b}',
                        color:'white',
                        position:'right',
                        show: false
                    },
                    emphasis: {
                        show: false,
                        formatter:'{b}',
                    }
                },
                itemStyle: {
                    emphasis: {
                        borderColor: '#fff',
                        borderWidth: 1
                    }
                }
            };
            var online={
                name: 'online',
                type: 'effectScatter',
                coordinateSystem: 'geo',
                data: [ 
                ],
                symbolSize: 20,
                label: {
                    normal: {
                        formatter:'{b}',
                        color:'white',
                        position:'right',
                        show: false
                    },
                    emphasis: {
                        show: false,
                        formatter:'{b}',
                    }
                },
                itemStyle: {
                    emphasis: {
                        borderColor: '#fff',
                        borderWidth: 1
                    }
                }
            }
            echarts.registerMap('qiannan', Qiannan);
            var chart = echarts.init(this.$refs.map);
            var mapOption = CustomChart.map();
           
            for(var i = 0; i  < this.devices.length; i++){
                var lanlat = this.devices[i].lanlat.split(",");
                if(this.devices[i].status == 0){
                  online.data.push({name:this.devices[i].name,value:[lanlat[0],lanlat[1],10]});
                }else{
                  offline.data.push({name:this.devices[i].name,value:[lanlat[0],lanlat[1],1]});
                }
            }
            mapOption.series=[online,offline];
            chart.setOption(mapOption);
      },
      loadBar(){        
          let barOption = CustomChart.bar(); 
          barOption.xAxis.data=[];
         
          var succ={ name:'成功',type: 'bar',data: []},fail={name:'失败',type: 'bar',data: []},alarm={name:'告警',type: 'bar',data: []};
          
          for(var i = 0; i < this.deviceGroups.length; i++){
            barOption.xAxis.data.push(this.deviceGroups[i].name);
            succ.data.push(this.deviceGroups[i].succ);
            fail.data.push(this.deviceGroups[i].fail);
            alarm.data.push(this.deviceGroups[i].alarm);
          }   
          barOption.series = [succ,fail,alarm]; 
          let bar = echarts.init(this.$refs.groupBar);
          bar.setOption(barOption);
      },
  },
  mounted() {
			//this.gettodayData();

      this.loadPie();

      this.loadMap();

      this.loadBar();
		}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.chartcontainer{
        box-sizing:border-box;
        height:100%;
        float:left;
        box-shadow:0 0 10px #06C;
        width:50%;       
        canvas{    
            box-shadow:rgba(0, 0, 0, 0.1) 0px 1px 2px 0px;
           background-color:rgb(249, 249, 249);
           border-radius:2px;
           border:1px solid black;              
        }
}
.chartcontainer2{
        box-sizing:border-box;
        height:100%;
        float:left;
        box-shadow:0 0 10px #06C;
        width:100%;       
        canvas{    
            box-shadow:rgba(0, 0, 0, 0.1) 0px 1px 2px 0px;
           background-color:rgb(249, 249, 249);
           border-radius:2px;
           border:1px solid black;              
        }
}

.mapcontainer{
        box-shadow:0 0 10px #06C;
        box-sizing:border-box;
        height:100%;
        
       
        width:100%;       
        canvas{    
            box-shadow:rgba(0, 0, 0, 0.1) 0px 1px 2px 0px;
           background-color:rgb(249, 249, 249);
           border-radius:2px;
           border:1px solid black;              
        }
}
.stblock { 
  padding:5px;
  padding-right:30px;
  height:120px;
  background-color:white;
  
  -webkit-box-shadow: 0 2px 16px #dfedf7, 0 0 1px #dfedf7, 0 0 1px #dfedf7;
}
</style>
