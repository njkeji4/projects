<template>
  <el-tabs v-model="activeName">       
    <!--el-tab-pane label="服务器设置" name="ipsec"><IPSec></IPSec></el-tab-pane-->    
    <el-tab-pane label="管理员设置" name="adminSetting" ><AdminSetting></AdminSetting></el-tab-pane>   
  </el-tabs>
</template>
<script>
import { mapGetters } from 'vuex';


import IPSec from './ipsec';
import AdminSetting from './AdminSetting';


export default {
	components: {
		 AdminSetting
	},
	data() {
		return {
			activeName: 'adminSetting',
		};
	},
	computed: {
		
	},
	methods: {
		handleClick(tab, event) {
		
		}
	}
};
</script>
