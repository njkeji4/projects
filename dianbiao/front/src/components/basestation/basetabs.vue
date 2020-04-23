<template>

  <el-tabs v-model="activeName"   tabPosition="left">

	<el-tab-pane label="参数设置" name="baseparam" lazy>
        <BaseParam></BaseParam>
    </el-tab-pane>

    <el-tab-pane label="数据导入" name="basedata">
        <BaseData  @analyzeDone="analyzeDone"></BaseData>
    </el-tab-pane>

    <el-tab-pane label="结果分析" name="baseanalysis" lazy>
        <BaseAnalysis :active="activeName === 'baseanalysis' "></BaseAnalysis>
    </el-tab-pane>

  </el-tabs>

</template>


<script>

	import BaseData from './basedata';
    import BaseAnalysis from './baseanalysis';
	import BaseParam from './baseparam';

	export default {
        components: {
		     BaseData,
             BaseAnalysis,
			 BaseParam
	    },
		data() {
			return {	
				activeName:'basedata',				
			}
		},
		computed: {
			
		},
		filters: {
			//formatRole
		},
		methods: {			
            analyzeDone(){               
                this.activeName='baseanalysis';				
            }
		},
		created() {
			
		},
		mounted() {
					
		}
	}
</script>

<style lang="scss">
	
	
</style>