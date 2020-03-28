<template>
    <div style="overflow:hidden;">

        <div  class="box"  style="width:25%;">       
          <div class="box-header">在线统计</div>   
           <div ref="certPie" class="chartcontainer lefttopchart"></div>                
        </div>

         <div  class="box"  style="width:25%;">       
          <div class="box-header">开合闸统计</div>   
          <div ref="devicePie" class="chartcontainer righttopchart"/>        
        </div>

         <div  class="box"  style="width:47.5%;">       
          <div class="box-header">电量及合闸时长</div>   
          
          <el-table :data="top10" resizable border highlight-current-row stripe ref="table" 
			 @sort-change="handleSortChange"  height="220"
			  class="cmcc-cell-nowrap">
				
				<el-table-column  prop="deviceName" label="名称" width="100"></el-table-column>				
				<el-table-column  prop="deviceNo" label="编号" width="130" ></el-table-column>
				
				<el-table-column  sortable="custom" prop="actionEnergy" label="电量(KW)" width="150">
                </el-table-column>
				
				<el-table-column  sortable="custom" prop="totalOnTime" label="合闸时长(h)" >
                </el-table-column>

			
			</el-table>
        </div>

        <div  class="box" style="width:49%;">       
          <div class="box-header" style="">电量统计</div>   
          <div ref="monthbar" class="chartcontainer lefttopchart" style="height:100%;"></div>    
        </div>

        <div  class="box" style="width:49%;">       
          <div class="box-header">电量对比</div>   
          <div ref="dayline" class="chartcontainer lefttopchart" style="height:100%;"></div>  
        </div>

             
    </div>

</template>

<script>
import { mapGetters } from 'vuex';

import { AdminAPI } from '../../api';
import Filters from '../../common/js/filters';
import echarts from 'echarts';

export default {
  data() {
			return {
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
    handleSortChange(){

    },  
   
    gettodayData(){
        AdminAPI.getOverview().then(({data}) => {
          if(data.status === 0){

            this.onlineCount = data.data.onlineCount;
            this.offlineCount = data.data.offlineCount;
            this.onCount = data.data.onCount;
            this.offCount = data.data.offCount;
            this.top10 = data.data.top10;
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
              color: ['#003366', '#006699', '#4cabce', '#e5323e'],
              legend: {
                        data: ['电量', '时长']
                },
               xAxis: {
                    type: 'category',
                    data: ['1日', '2日', '3日', '4日', '5日', '6日', '7日']
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    name:'电量',
                    data: [120, 500, 150, 80, 70, 110, 130, 150, 80, 70, 110, 130],
                    type: 'line'
                },
                {
                    name:'时长',
                    data: [100, 200, 100, 180, 100, 50, 100, 250, 180, 120, 200, 30],
                    type: 'line'
                }
                ]
          }
      },
      bar(){
          return {
                color: ['#003366', '#006699', '#4cabce', '#e5323e'],
                legend: {
                        data: ['电量', '时长']
                },
                xAxis: {
                    type: 'category',
                    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月','8月', '9月', '10月', '11月', '12月']
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name:'电量',
                        data: [120, 500, 150, 80, 70, 110, 130, 150, 80, 70, 110, 130],
                        type: 'bar'
                    },
                    {
                        name:'时长',
                        data: [120, 500, 150, 80, 70, 110, 130, 150, 80, 70, 110, 130],
                        type: 'bar'
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
        
        barOption.xAxis.data=[];
        barOption.series[0].data=[];
        barOption.series[1].data=[];
        for(var i = 0; i < this.months.length; i++){
            var tmp = this.months[i];
             barOption.xAxis.data.push(this.getYM(tmp.statDate));
             barOption.series[0].data.push(tmp.energy);
             barOption.series[1].data.push(tmp.ontime);
        }

        let monthBar = echarts.init(this.$refs.monthbar);
        monthBar.setOption(barOption);
        monthBar.on('click',this.onclickBar);

        ///////
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
      }
  },
  mounted() {
     // this.todayDate=Filters.dateFormat(new Date());
	  this.gettodayData();
      //this.loadData();
		}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.box{
  float:left;
  border-radius: 3px;
  background: #ecf5f0;
  border: 3px solid #d2d6de;
  
  height:260px;
  box-shadow: 0 1px 1px rgba(0,0,0,0.1);  
  
  border-top-left-radius: 0;
    border-top-right-radius: 0;
    border-bottom-right-radius: 3px;
    border-bottom-left-radius: 3px;
  
  .box-header{
    
    color: #444;  
    padding: 10px;  
    text-align:left;
  }
  .box-body{
    
    padding: 10px;
    overflow:hidden;
  }

}

.lefttopchart{
    border-right: 2px solid #f4f4f4;
}
.chartcontainer{
        box-sizing:border-box;
        height:90%;
        padding:15px;
        float:left;
        width:100%;       
        canvas{    
            box-shadow:rgba(0, 0, 0, 0.1) 0px 1px 2px 0px;
           background-color:rgb(249, 249, 249);
           border-radius:2px;
           border:1px solid black;              
        }
}
.small-box{
 
  color:white;

  width:250px;
  height:100px;
  float:left;
  margin-right:10px;  
}
.bar-blue{
  background-color: #00c0ef !important;
}
.bar-yellow{
  background-color: #f39c12 !important;
}
.bar-green{
  background-color: #00a65a !important;
}
.bar-red{
  background-color: #dd4b39  !important;
}


</style>
