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
				@selection-change="handleSelectionChange"  :data="tableData">
				
				<el-table-column header-align="center" type="selection">
				</el-table-column>				
				<el-table-column  prop="versionNo" label="版本号" width="240" align="center">
				</el-table-column>

				<el-table-column  prop="updateDate" label="上传日期" width="200" align="center">
					<template slot-scope="scope">
						{{scope.row.updateDate | dateFormat}}
					</template>
				</el-table-column>
				
				<el-table-column  prop="oldVersion1" label="最低适用版本" min-width="130" align="center" >
				</el-table-column>

				<el-table-column  prop="oldVersion2" label="最高适用版本" min-width="130" align="center" >
				</el-table-column>
			</el-table>
		</section>		

	</div>
</template>

<script>
	import { mapGetters } from 'vuex';

	import { AdminAPI } from '../../api';
	import VersionUpload from './VersionUpload';
	import { openModal } from '../../common/js/modal';
	import Filters from '../../common/js/filters';
	
	const openVersionUploadModal = openModal(VersionUpload);
	export default {
		data() {
			return {
				total: 0,
				page: 1,
				pageSize: 10,
				sels: [],
				tableData: [],
			}
		},
		computed: {
			...mapGetters('device', {
				DeviceTypes: 'deviceTypes',
			}),
		},
		filters: {
			dateFormat: Filters.dateFormat,			
		},
		methods: {
			handleSizeChange(size) {
				this.pageSize = size;
				this.handleCurrentChange(1);
			},
			handleCurrentChange(val) {
				this.page = val;

			},
			upload() {
				openVersionUploadModal().then((data)=>{
					//console.log(data);
					this.getVersionList();
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
					
					let version = this._getSelectedVersion();
					console.log(version);
                    AdminAPI.removeVersion(version).then((data) => {                    	
						this.$message({
							message: '删除成功!',
							type: 'success'
						});
						this.getVersionList();
					});
				}).catch((error) => {
					if(error instanceof Error) {
						this.$message.error('删除失败!' + error);
					}
				});
			},
			getVersionList() {
				let param = {};
				AdminAPI.getVersionList().then(({data}) => {
					//console.log(data);
					this.tableData = data;
				}).catch((err) => {
					this.$message.error('Error', err);

				});

			},
		},
		mounted() {
			this.getVersionList();
			
		}

	}
</script>

<style scoped="scoped">
	.main-content {
		padding-top: 30px;
	}
</style>