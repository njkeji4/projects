<template>
	<div class="main-content" style="padding:3px;">
		<div style="text-align:left;">
				<el-form :inline="true" size="small" :model="searchForm" ref="searchForm">
					
					<el-form-item label="" prop="deviceName">
						<el-input size="small" v-model="searchForm.deviceName" placeholder="设备名称"></el-input>
					</el-form-item>
					<el-form-item label="" prop="deviceNo">
						<el-input size="small" v-model="searchForm.deviceNo" placeholder="设备编号"></el-input>
					</el-form-item>

					<el-button size="small" @click="searchDevice">查询</el-button>
					
				</el-form>
		</div>

		<section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe 
				v-loading="listLoading" ref="table"			  
				@sort-change="handleSortChange"
				class="cmcc-cell-nowrap">
				
				<el-table-column  sortable="custom" prop="deviceName" label="设备名称" width="200"></el-table-column>				
				<el-table-column  prop="deviceNo" label="设备编号" width="150" sortable="custom"></el-table-column>
				<el-table-column  prop="cmdName" label="命令" width="150" ></el-table-column>
				<el-table-column  prop="cmdContent" label="内容" width="150" ></el-table-column>
				<el-table-column  prop="status" label="执行状态" width="150" sortable="custom">
					<template slot-scope="scope">
						{{scope.row.status | statusFormt }}
					</template></el-table-column>
				</el-table-column>
				<el-table-column  prop="retMessage" label="返回消息" width="150"></el-table-column>
				<el-table-column  prop="cmdTime" label="执行时间" width="160" sortable="custom">
					<template slot-scope="scope">
						{{scope.row.cmdTime | dateFormat('yyyy-MM-dd hh:mm:ss') }}
					</template></el-table-column>
				<el-table-column  prop="retTime" label="完成时间" width="160" sortable="custom">
					<template slot-scope="scope">
						{{scope.row.retTime  | dateFormat('yyyy-MM-dd hh:mm:ss') }}
					</template></el-table-column>				
					
			
			</el-table>
		</section>

		<el-col class="toolbar">			
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" 
				:current-page="page" :page-sizes="[10, 20, 30, 40, 50, 100]" 
				:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
			</el-pagination>
		</el-col>
		
	</div>
</template>

<script>
	import axios from 'axios';
	import Vue from 'vue'
	import { mapGetters, mapActions } from 'vuex';
	import util from '../../common/js/util';
	import Filters from '../../common/js/filters';
	import { AdminAPI } from '../../api';
	import { openModal } from '../../common/js/modal';

	import DeviceEditDlg from './SettingDevice';
	import AddDeviceDlg from './AddDevice';
	const openDeviceEditDlg = openModal(DeviceEditDlg);
	const openAddDeviceDlg = openModal(AddDeviceDlg);

	export default {
		data() {
			return {				
				searchForm: { 
					deviceName:'',					
					deviceNo:'',
					cmdName:''					
				},
				actionLoading:false,
				groups:[],
				groupMaps:{},
				devices: [],
				total: 0,
				page: 1,				
				pageSize: 10,
				tableSort: {
					prop: 'sn',
					order: 'descending'
				},
				orderBy: [{
					name: 'sn',
					order: 'DESC'
				}],
				listLoading: false,
				sels: [], //列表选中列
				contextMenuRow: null,
				contextMenuPosition: {
					top: 0,
					left: 0
				},			

				// table event data
				deviceParamsConfigVisible: false,
				selectedDevice: null,
				sort:'cmdTime',
				order:'desc'
			}
		},
		filters: {
			dateFormat: Filters.dateFormat,
			statusFormt:(v) => {if(v==2)return '执行中';if(v==0)return '成功';if(v==1)return '失败';},
			groupIdFormat:(v,maps) => {return maps[v];}
		},
		computed: {

		},
		watch: {
			
		},
		methods: {		

			handleSortChange(col){		
				if(col.prop == null)
				{
					return;
				}		
				this.order = (col.order === 'ascending')? 'asc':'desc';
				this.sort = col.prop ;				
				this.searchDevice();
			},
			
			reset() {
				this.$refs.searchForm.resetFields();
			},
			
			searchDevice() {
				this.page = 1;
				this.getDeviceCmdList();
			},
			
			handleSizeChange(size) {
				this.pageSize = size;				
				this.handleCurrentChange(1);
			},
			handleCurrentChange(val) {
				this.page = val;				
				this.getDeviceCmdList();
			},
			getDeviceCmdList() {

				this.listLoading = true;

				var searchParams = _.omitBy(this.searchForm, (item) => item == "" || _.isNil(item));
				searchParams.page = this.page - 1;
				searchParams.size = this.pageSize;
				searchParams.sort=this.sort;//"deviceNo";
				searchParams.order=this.order;//"asc";

				AdminAPI.getDeviceCmdList(searchParams).then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						this.total = jsonData.data.total;
						this.devices = jsonData.data.content;
						this.total = jsonData.data.totalElements;
					} else {
						this.$message({
							messsage: `获取设备命令失败:${data.msg}`,
							type: 'error'
						})
					}
					this.listLoading = false;
				});
				
			},
		
			// context menu
			handleSelectionChange: function(sels) {
				this.sels = sels;
				this.ids=_.map(this.sels , (device) => device.deviceNo);				
				//console.log(this.ids);
			},
			handleContextMenu(row, event) {
				
			},

			handleContextMenuCommand(command) {				
			},
			getSelection() {
				let list;
				if(this.sels && this.sels.length > 0) {
					list = this.sels;
				} else {
					list = this.contextMenuRow ? [this.contextMenuRow] : [];
				}				
				return list;
			},			
		},
		created() {
			
		},
		mounted() {
			this.getDeviceCmdList();
		}
	}
</script>


<style lang="scss">
	.el-table th,
	.el-table__fixed-header-wrapper thead div,
	,
	.el-table__header-wrapper thead div {
		background-color: #177fd8;
		color: #fff;
	}
	
	.el-table .sort-caret.ascending {
		border-bottom: 5px solid #fff;
	}
	
	.el-table .sort-caret.descending {
		border-top: 5px solid #fff;
		border-top-color: #fff !important;
	}	
	.add-device-form {
		.el-input,
		.el-select {
			width: 200px;
		}
		.el-dialog {
			width: 480px;
		}
		.el-form-item__tips {
			font-size: 12px;
			line-height: 1;
			padding-top: 4px;
			position: absolute;
			left: auto;
			right: 0;
			top: 10px;
		}
	}
	
	.device-params-picker {
		.el-input {
			width: 350px;
		}
		.el-dialog {
			width: 650px;
		}
		.el-form-item__error {
			top: 8px;
			left: 360px;
		}
	}
	
	.el-table {
		.el-button {
			background-color: #fff;
			color: #333;
		}
	}
</style>