<template>
  <el-tabs v-model="activeName" >
    <el-tab-pane label="基站数据" name="first"><BaseData></BaseData></el-tab-pane>
    <el-tab-pane label="基站分析" name="second">基站分析</el-tab-pane>
  </el-tabs>
</template>


<script>

	import BaseData from './basedata';

	export default {
        components: {
		     BaseData
	    },
		data() {
			return {	
				activeName:'first'
			}
		},
		computed: {
			
		},
		filters: {
			//formatRole
		},
		methods: {			
		
		},
		created() {
			
		},
		mounted() {
					
		}
	}
</script>

<style lang="scss">
	
	
</style>