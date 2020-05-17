<template>
	<div class="main-content" style="padding:3px;">
		<el-row :gutter=20 class="toolbar searchparam">
			
		</el-row>

		<section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe v-loading="listLoading" ref="table"			  
			 @sort-change="handleSortChange"  max-height="1000"
			  class="cmcc-cell-nowrap">
				
				<el-table-column  sortable="custom" prop="roomName" label="机房" width="200"></el-table-column>				
				<el-table-column  sortable="custom" prop="deviceName" label="设备名称" width="200"></el-table-column>				
				<el-table-column  prop="deviceNo" label="设备编号" width="150" sortable="custom"></el-table-column>				
				<el-table-column  prop="statDate" label="统计日期" width="200" sortable="custom">
					<template slot-scope="scope">
						{{scope.row.statDate | dateFormat('yyyy-MM-dd') }}
					</template></el-table-column>
				</el-table-column>

				<el-table-column  prop="energy" label="总电量" width="150" sortable="custom"></el-table-column>
				<el-table-column  prop="aEnergy" label="1路电量" width="150" sortable="custom"></el-table-column>
				<el-table-column  prop="bEnergy" label="2路电量" width="150" sortable="custom"></el-table-column>
				<el-table-column  prop="cEnergy" label="3路电量" width="150" sortable="custom"></el-table-column>
				<el-table-column  prop="dEnergy" label="4路电量" width="150" sortable="custom"></el-table-column>
				<el-table-column  prop="aOntime" label="1路合闸时长(小时)"  width="150" ></el-table-column>	
				<el-table-column  prop="bOntime" label="2路合闸时长(小时)"  width="150"></el-table-column>
				<el-table-column  prop="cOntime" label="3路合闸时长(小时)"  width="150"></el-table-column>
				<el-table-column  prop="dOntime" label="4路合闸时长(小时)" width="150" ></el-table-column>
			
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
				sort:'statDate',
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
				searchParams.sort=this.sort;
				searchParams.order=this.order;

				AdminAPI.searchStat(searchParams).then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						this.total = jsonData.data.total;
						this.devices = jsonData.data.content;
						this.total = jsonData.data.totalElements;
						this.listLoading = false;
					} else {
						this.$message({
							messsage: `获取设备统计失败:${data.msg}`,
							type: 'error'
						})
					}
				});
				this.listLoading = false;
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

<style scoped lang="scss">

</style>

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
	
	.searchparam {
		.el-button {
			border-radius: 6px;
		}
		@media only screen and (min-width: 1470px) {
			.paramleft {
				width: 75%;
			}
			
		}
		@media only screen and (max-width: 1470px) {
			.paramleft {
				width: 83%;
			}
			.search-action-wrap {
				width: 100%;
			}
		}
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