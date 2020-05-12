<template>
	<div class="main-content" style="padding:3px;">
		<el-row :gutter=20 class="toolbar searchparam">
			<el-col :span=4>
				<el-upload class='ensure' 
						v-loading="uploading"
						ref="uploadForm"
						name="uploadFile"
						:action="uploadUrl"
						:auto-upload="true"				
						:on-success="uploadSuc"
						:on-error="uploadFail"
						:before-upload="beoforeUpload"
						:show-file-list='false'
						>	
						<el-button size="small" type="primary">导入基站数据</el-button>
					</el-upload>
			</el-col>

            <el-col  :span=12 style="">
				<el-form   size="small" :model="searchForm" class="search-form" ref="searchForm">
					<el-form-item label="" prop="name" style="float:left;">
						<el-input style="width:300px;" size="small" v-model="searchForm.name" placeholder="设备名称"></el-input>
					</el-form-item>
					<el-button size="small" @click="getStations" >查询</el-button>
                    <el-button size="small" @click="analyzeStations" >分析</el-button>
				</el-form>
			</el-col>

			<el-col :span=10 class="paramleft">
				
			</el-col>
			
		</el-row>

		<section class="grid-content">
			<el-table :data="convertedData" resizable border 
                highlight-current-row stripe  
                v-loading="listLoading" ref="table" 
			    class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">				
				</el-table-column>
				
				<el-table-column  sortable="custom" prop="name" label="基站名称" width="220"></el-table-column>
				
				<el-table-column  prop="dataDate" label="日期" width="150" sortable="custom">
                    <template slot-scope="scope">
						{{scope.row.dataDate | dateFormat('yyyy-MM-dd') }}
					</template></el-table-column>
                </el-table-column>				
								
				<el-table-column  prop="dataType" label="数据类型" width="150" sortable="custom"></el-table-column>				
					
				<el-table-column  v-for="item in 24 " :key="item-1" align="center" :label="(item - 1)+''"  width="100">
                    <template slot-scope="scope">
                        {{scope.row.value[item-1]}}
                    </template>
                </el-table-column>				
				
			
			</el-table>
		</section>

		<el-col class="toolbar">			
		    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" 
				:current-page="page" :page-sizes="[50, 100]" 
				:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
			</el-pagination>
		</el-col>
		
	</div>
</template>

<script>
	import { AdminAPI } from '../../api';
    import Filters from '../../common/js/filters';

	export default {
		data() {
			return {
				stations:[],
                convertedData:[],
                page: 1,				
				pageSize: 28,
                total:0,
                sort:'name',
				order:'asc',
                uploadUrl:AdminAPI.uploadBaseStationUrl,
                listLoading:false,
				uploading:false,
                searchForm:{name:''}
			}
		},
		filters: {
			dateFormat: Filters.dateFormat,
		},
		computed: {

		},
		watch: {
			
		},		
		methods: {	
            handleSizeChange(size) {
				this.pageSize = size;				
				this.handleCurrentChange(1);
			},
			handleCurrentChange(val) {
				this.page = val;				
				this.getStations();
			},	
            analyzeStations(){
                
                AdminAPI.analyzeBaseStation({}).then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						  this.$emit("analyzeDone");
					} else {
						this.$message({
							messsage: `分析失败:${data.msg}`,
							type: 'error'
						})
					}
				});
              
            },
            splitData(){
                for(var i in this.stations){
                    var data = this.stations[i];
                    this.convertedData.push({name:data.name, dataDate:data.dataDate, dataType:'流量',value:data.traffic});
                    this.convertedData.push({name:data.name, dataDate:data.dataDate, dataType:'下行prb',value:data.prbdown})
                    this.convertedData.push({name:data.name, dataDate:data.dataDate, dataType:'上行prb',value:data.prbup})
                    this.convertedData.push({name:data.name, dataDate:data.dataDate, dataType:'激活用户数',value:data.activeUser})
                }
            },
            getStations(){
                var searchParams = _.omitBy(this.searchForm, (item) => item == "" || _.isNil(item));
				searchParams.page = this.page - 1;
				searchParams.size = this.pageSize;
				searchParams.sort=this.sort;//"name";
				searchParams.order=this.order;//"asc";
				
				this.listLoading = true;
				AdminAPI.searchBaseStation(searchParams).then(({
					data: jsonData
				}) => {
					this.listLoading = false;
					if(jsonData.status === 0) {
						this.stations = jsonData.data.content;
						this.total = jsonData.data.totalElements * 4;
                        this.splitData();
					} else {
						this.$message({
							messsage: `获取基站失败:${data.msg}`,
							type: 'error'
						})
					}
				});
            },
			uploadSuc(res, file, fileList){
				this.uploading = false;
				if(res.status === 0){
					this.$message({
						message: '上传成功!',
						type: 'success'
					});
					this.getStations();
					
				}else{
					this.$message.error('上传文件失败!'+res.msg);
				}
			},
			beoforeUpload(){
				this.uploading = true;
			},
			uploadFail(err){
				this.uploading = false;
				this.$message({
						message: '上传失败!'+err,
						type: 'error'
				});
			},
		},
		created() {
			
		},
		mounted() {
			this.getStations();
		}
	}
</script>

<style scoped lang="scss">
    	
</style>