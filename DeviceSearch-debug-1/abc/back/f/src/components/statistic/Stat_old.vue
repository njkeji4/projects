<template>
    	<el-row class="container">
        <el-row class="blockparent">
          <el-col :span="12" class="stblock">
              <el-row>
                <el-col :span="12" style="text-align:left"><el-button type="success">今日认证</el-button></el-col>
                <el-col :span="12" style="text-align:right">认证数量:{{cerCount}}</el-col>
              </el-row>
              <el-row style="margin-top:20px;">
                <el-col :span="12" style="text-align:center">认证成功</el-col>
                <el-col :span="12" style="text-align:center">认证失败</el-col>
              </el-row>
              <el-row style="margin-top:10px;">
                <el-col :span="12" style="text-align:center">{{cersuccCount}}</el-col>
                <el-col :span="12" style="text-align:center">{{cerfailCount}}</el-col>
              </el-row>
           </el-col>
          <el-col :span="12" class="stblock">
               <el-row>
                <el-col :span="12" style="text-align:left"><el-button type="primary">今日设备</el-button></el-col>
                <el-col :span="12" style="text-align:right">设备数量:{{deviceCount}}</el-col>
              </el-row>
              <el-row style="margin-top:20px;">
                <el-col :span="12" style="text-align:center">设备在线</el-col>
                <el-col :span="12" style="text-align:center">设备离线</el-col>
              </el-row>
              <el-row style="margin-top:10px;">
                <el-col :span="12" style="text-align:center">{{onlineCount}}</el-col>
                <el-col :span="12" style="text-align:center">{{offlineCount}}</el-col>
              </el-row>
          </el-col>
        </el-row>

         <el-row>
          <el-col :span="12" class="stblock">
              <el-row>
                <el-col :span="12" style="text-align:left"><el-button type="danger">今日黑名单</el-button></el-col>                
              </el-row>
              <el-row style="margin-top:20px;">
                <el-col :span="24" style="text-align:center">黑名单报警数</el-col>               
              </el-row>
              <el-row style="margin-top:10px;">
                <el-col :span="24" style="text-align:center">{{alarmCount}}</el-col>                
              </el-row>
           </el-col>                  
        </el-row>      

        <!--  -------------------------------------------------------------------------  -->
        <div style="">
        <el-row style="margin-top:50px;">
          <el-col :span="12" class="stblock">
              <el-row>
                <el-col :span="12" style="text-align:left"><el-button type="success">过往认证</el-button></el-col>
                <el-col :span="12" style="text-align:right">认证数量:{{usedcerCount}}</el-col>
              </el-row>
              <el-row style="margin-top:20px;">
                <el-col :span="12" style="text-align:center">认证成功</el-col>
                <el-col :span="12" style="text-align:center">认证失败</el-col>
              </el-row>
              <el-row style="margin-top:10px;">
                <el-col :span="12" style="text-align:center">{{usedcersuccCount}}</el-col>
                <el-col :span="12" style="text-align:center">{{usedcerfailCount}}</el-col>
              </el-row>
           </el-col>
           <el-col :span="12" class="stblock">
              <el-row>
                <el-col :span="12" style="text-align:left"><el-button type="danger">过往黑名统计</el-button></el-col>                
              </el-row>
              <el-row style="margin-top:20px;">
                <el-col :span="24" style="text-align:center">黑名单报警数</el-col>               
              </el-row>
              <el-row style="margin-top:10px;">
                <el-col :span="24" style="text-align:center">{{usedalarmCount}}</el-col>                
              </el-row>
           </el-col>       
        </el-row>
        
        </div>    


      </el-row> 

</template>

<script>
import { mapGetters } from 'vuex';

import { AdminAPI } from '../../api';
import Filters from '../../common/js/filters';

export default {
  data() {
			return {
        listLoading:false,
        cerCount:100,
        cersuccCount:90,
        cerfailCount:10,

        usedcerCount:100,
        usedcersuccCount:90,
        usedcerfailCount:10,

        deviceCount:100,
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
          }					
				}).catch((err) => {
					this.$message.error('Error', err);

				});
      },
  },
  mounted() {
			this.gettodayData();
		}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container {
	
		
    
}
.stblock { 
  padding:5px;
  padding-right:30px;
  height:120px;
  background-color:white;
  
  -webkit-box-shadow: 0 2px 16px #dfedf7, 0 0 1px #dfedf7, 0 0 1px #dfedf7;
}
</style>
