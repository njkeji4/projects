<template>
    <div>

        <div class="box">
          <div class="box-header">总计</div>
          <div class="box-body">           
              <div class="small-box bar-green">
                <p>在线电表:</p><h3>{{usedcersuccCount}}</h3>
              </div>     
              <div class="small-box bar-yellow"><p> 离线电表:</p><h3>{{usedcerfailCount}}</h3></div>  
              <div class="small-box bar-red"><p> 合闸电表:</p><h3>{{usedalarmCount}}</h3></div>   
              <div class="small-box bar-blue"> <p>拉闸电表:</p><h3>{{deviceCount}}</h3></div>   

          </div>
        </div>

        <div class="box">
          <div class="box-header">今日统计: {{todayDate}}</div>
          <div class="box-body">           
                <div ref="certPie" class="chartcontainer lefttopchart"></div>
                <div ref="devicePie" class="chartcontainer righttopchart"/>             
          </div>
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
        cerCount:100,
        cersuccCount:90,
        cerfailCount:10,

        usedcerCount:100,
        usedcersuccCount:90,
        usedcerfailCount:10,

        deviceCount:40,
        onlineCount:90,
        offlineCount:10,

        alarmCount:50,
        usedalarmCount:50,

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

            this.loadData();
          }					
				}).catch((err) => {
					this.$message.error('Error', err);

				});
      },
      pie(){
        return {
                        legend: {
                            orient: 'vertical',
                            x: 'left',
                            data:[]
                        },
                        title:{
                          text:'',
                          top:'top',
                          left:'right',                         
                        },
                        series: {
                            type: 'pie',
                            radius: ['40%', '80%'],
                            avoidLabelOverlap: true,
                            label: {
                                normal: {
                                    show: true,
                                    //position: 'inside',
                                    formatter: '{b}({c})',
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
                                    show: true
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
          {name:"成功",value: this.cersuccCount},
          {name:"失败",value:this.cerfailCount},
          {name:"告警",value:this.alarmCount}];
        cerPieOption.legend.data=["成功","失败","告警"];
        cerPieOption.title.text="操作";

        devicePieOption.series.data=[
          {name:"在线",value:this.onlineCount},
          {name:"离线",value:this.offlineCount}];
        devicePieOption.legend.data=["在线","离线"];
        devicePieOption.title.text="设备";

        certPie.setOption(cerPieOption);
        devicePie.setOption(devicePieOption);


      },
  },
  mounted() {
      this.todayDate=Filters.dateFormat(new Date());
			//this.gettodayData();
      this.loadData();
		}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.box{
 
  border-radius: 3px;
  background: #ffffff;
  border-top: 3px solid #d2d6de;
  margin-bottom: 20px;
  width: 100%;
  box-shadow: 0 1px 1px rgba(0,0,0,0.1);  
  
  .box-header{
    border-bottom: 1px solid #f4f4f4;
    color: #444;  
    padding: 10px;  
    text-align:left;
  }
  .box-body{
    border-top-left-radius: 0;
    border-top-right-radius: 0;
    border-bottom-right-radius: 3px;
    border-bottom-left-radius: 3px;
    padding: 10px;
    overflow:hidden;
  }

}

.lefttopchart{
    border-right: 2px solid #f4f4f4;
}
.chartcontainer{
        box-sizing:border-box;
        height:300px;
        padding:15px;
        float:left;
        width:50%;       
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
