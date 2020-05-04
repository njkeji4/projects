<template>
	<div class="main-content" style="padding:3px;">
		

		<section class="grid-content">
			<el-table :data="convertedData" resizable border 
                highlight-current-row stripe 
                v-loading="listLoading" ref="table" 
			    class="cmcc-cell-nowrap">
				
				<el-table-column  sortable="custom" prop="name" label="基站名称" width="220"></el-table-column>
								
				<el-table-column  sortable="custom" prop="start" label="起始时间" width="220">
                </el-table-column>

                <el-table-column  sortable="custom" prop="end" label="结束时间" width="220">
                </el-table-column>				
			
			</el-table>
		</section>

		
	</div>
</template>

<script>
	import { AdminAPI } from '../../api';
    import Filters from '../../common/js/filters';

	export default {
		 props: {
			 active: {
				type: Boolean,
				default: false,
				required: true
			},
		 },
		data() {
			return {
				stations:[],
                convertedData:[],
                listLoading:false
			}
		},
		filters: {
			dateFormat: Filters.dateFormat,
		},
		computed: {

		},
		watch: {
			 active(val, oldVal) {
				console.log(val);
				val && this.getAnalysiResult();
			}
		},		
		methods: {

            getAnalysiResult(){

				AdminAPI.getBaseAnalyzeResult().then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						this.convertedData = jsonData.data;						
						this.listLoading = false;
					} else {
						this.$message({
							messsage: `获取基站分析结果失败:${data.msg}`,
							type: 'error'
						})
					}
				});
            },
			
		},
		created() {
			
		},
		mounted() {			
			this.getAnalysiResult();
		}
	}
</script>

<style scoped lang="scss">
    	
</style>