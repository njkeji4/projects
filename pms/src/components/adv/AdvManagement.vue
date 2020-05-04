<template>
	<div class="main-content">

		<el-row class="toolbar searchparam">			
			<el-col  :span=60 class="search-action-wrap" style="margin-bottom:10px;">
				<div>
					<el-button size="small" @click="upload">上传</el-button>
					<el-button size="small" :disabled="sels.length === 0" @click="batchRemove">删除</el-button>
				</div>				
			</el-col>
		</el-row>
		
		<section class="grid-content">
			<el-table resizable border highlight-current-row stripe class="cmcc-cell-nowrap" ref="table" 
				@selection-change="handleSelectionChange"  :data="tableData"
				@sort-change="handleSortChange">
				
				<el-table-column header-align="center" type="selection">
				</el-table-column>	

				<el-table-column  prop="title" label="标题" sortable="custom" width="240" align="center">
				</el-table-column>

				<el-table-column  prop="advType" label="类型" sortable="custom" width="240" align="center">
					<template slot-scope="scope">						
						{{scope.row.advType == 1?'图片':'视频'}}					
					</template>
				</el-table-column>

				<el-table-column  prop="createTime" label="上传日期" sortable="custom" width="200" align="center">
					<template slot-scope="scope">
						{{scope.row.createTime | dateFormat}}
					</template>
				</el-table-column>

				<el-table-column  prop="expireDate" label="截止日期" sortable="custom" width="200" align="center">
					<template slot-scope="scope">
						{{scope.row.expireDate | dateFormat}}
					</template>
				</el-table-column>
				
				<el-table-column prop="groups"  label="适用组"  align="center">
					<template scope="scope">
						{{scope.row.groups | groupsFormat}}
					</template>	
				</el-table-column>

				<!--el-table-column  prop="requested" label="下发状态" sortable="custom" align="center" >
					<template slot-scope="scope">						
						{{scope.row.requested == '1'?'已下发':'未下发'}}					
					</template>
				</el-table-column-->
				
			</el-table>
		</section>		

		<el-col class="toolbar">
			<!--<el-button type="danger" size="small" @click="batchRemove" :disabled="this.sels.length === 0">批量删除</el-button>-->
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page" :page-sizes="[10, 15, 20, 30, 40, 50, 100]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
			</el-pagination>
		</el-col>

	</div>
</template>

<script>
	import { mapGetters } from 'vuex';

	import { AdminAPI } from '../../api';
	import AdvUpload from './AdvUpload';
	import { openModal } from '../../common/js/modal';
	import Filters from '../../common/js/filters';
	
	const openUploadModal = openModal(AdvUpload);
	export default {
		data() {
			return {
				total: 0,
				page: 1,
				pageSize: 10,
				sort:'createTime',
				order:'desc',
				sels: [],
				tableData: [],
				groups:[],
			}
		},
		computed: {			
		},
		filters: {
			dateFormat: Filters.dateFormat,	
			groupsFormat: (v) => {
			    var g="";
				for(var i in v){
					g += v[i].groupName + " "
				}				
			  	return g;
			}
		},
		methods: {
			handleSizeChange(size) {
				this.pageSize = size;
				this.handleCurrentChange(1);
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getAdvList();
			},
			upload() {
				openUploadModal({
					data: {
						groups:this.groups
					}
				}).then((data)=>{
					//console.log(data);
					this.getAdvList();
				});
			},
			handleSelectionChange: function(sels) {
				this.sels = sels;
			},
			_getSelectedVersion() {
				//console.log(this.sels);
				let version;
				if(!this.sels || this.sels.length === 0) {
					version = "";
				} else {
					version = this.sels.map(item => item.id);
				}
				//console.log(version);
				return version;
			},
			batchRemove: function() {
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {
					
					let adv = this._getSelectedVersion();
					console.log(adv);
                    AdminAPI.removeAdv(adv).then((data) => {                    	
						this.$message({
							message: '删除成功!',
							type: 'success'
						});
						this.getAdvList();
					});
				}).catch((error) => {
					if(error instanceof Error) {
						this.$message.error('删除失败!' + error);
					}
				});
			},

			handleSortChange(col){		
				if(col.prop == null)
				{
					return;
				}		
				this.order = (col.order === 'ascending')? 'asc':'desc';
				this.sort = col.prop ;				
				this.getAdvList();
			},

			getDeviceGroup(){
				AdminAPI.getDeviceGroups().then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						this.groups = jsonData.data;
					} else {
						this.$message({
							messsage: `获取组失败:${data.msg}`,
							type: 'error'
						})
					}
				});
			},

			getAdvList() {
				let param = {};
				param.page = this.page - 1;
				param.size = this.pageSize;
				param.sort= this.sort;
				param.order=this.order;			
				AdminAPI.searchAdvList(param).then(({data}) => {
					this.total = data.data.totalElements;
					this.tableData = data.data.content;
				}).catch((err) => {
					this.$message.error('Error', err);
				});

			},
		},
		mounted() {
			this.getAdvList();
			this.getDeviceGroup();
		}

	}
</script>

<style scoped="scoped">
	.main-content {
		padding-top: 30px;
	}
</style>