<template>
	<div class="main-content" style="padding:3px;">
		<el-row :gutter=20 class="toolbar searchparam">
			
			<el-col  :span=5 class="search-action-wrap" style="margin-bottom:10px;">
				<div style="float:left">		
					<!--el-button size="small" @click="addDevice">增加设备</el-button-->
					<!--el-button size="small" @click="batchRemove" :disabled="this.sels.length === 0">删除设备</el-button-->
					<el-button size="small" @click="settingDevice" :disabled="this.sels.length !== 1">设置自动拉合闸时间</el-button>				
				</div>				
				
			</el-col>
			
		</el-row>

		<section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe v-loading="listLoading" ref="table" 
			 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			  class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">				
				</el-table-column>
				
				<el-table-column  sortable="custom" prop="deviceName" label="设备名称" width="240"></el-table-column>
				
				<el-table-column  prop="deviceNo" label="设备编号" width="200" sortable="custom"></el-table-column>				
						
				<el-table-column  prop="status" label="设备状态" width="120" sortable="custom">
					<template slot-scope="scope">						
						<el-tag :type="scope.row.status == '0' ? 'success' : 'danger'" close-transition>{{scope.row.status == '0'?'在线':'离线'}}</el-tag>					
					</template>
				</el-table-column>

				<el-table-column  sortable="custom" prop="SwitchStat" label="开关状态" width="240">
					<template slot-scope="scope">						
						<el-tag :type="scope.row.status == '0' ? 'success' : 'danger'" close-transition>{{scope.row.status == '0'?'拉闸':'合闸'}}</el-tag>					
					</template>
				</el-table-column>
			
				<el-table-column  sortable="custom" prop="GroupactionEnergy" label="组合有功电能量" width="240"></el-table-column>
				<el-table-column  sortable="custom" prop="ActionEnergy" label="正向有功电能量" width="240"></el-table-column>
				<el-table-column  sortable="custom" prop="ReactionEnergy" label="反向有功电能量" width="240"></el-table-column>
				<el-table-column  sortable="custom" prop="vol" label="电压" width="240"></el-table-column>
				<el-table-column  sortable="custom" prop="cur" label="电流" width="240"></el-table-column>
				<el-table-column  sortable="custom" prop="ActionPower" label="有功功率" width="240"></el-table-column>
				<el-table-column  sortable="custom" prop="Freq" label="电网频率" width="240"></el-table-column>
				<el-table-column  sortable="custom" prop="Factor" label="功率因数" width="240"></el-table-column>
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
	import { AdminAPI } from '../../api';
	import { openModal } from '../../common/js/modal';

	import DeviceEditDlg from './DeviceEditDlg';
	//import DeviceGroupAddDlg from './BindDeviceDlg';
	const openDeviceEditDlg = openModal(DeviceEditDlg);
	//const openDeviceGroupAddDlg = openModal(DeviceGroupAddDlg);

	export default {
		data() {
			return {				
				searchForm: { 
					name:'',
					versionNo:'',
					deviceNo:'',
					status:'',
					groupId:''
				},
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
				sort:'name',
				order:'desc'
			}
		},
		filters: {
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
				this.getDeviceList();
			},
			
			addDevice() {
				this.getDeviceList();
			},

			settingDevice(){
				var row;
				if(this.sels && this.sels.length > 0) {
					row = this.sels[0];
				}
				openDeviceEditDlg({
					data: {
						deviceInfo: row
					}
				}).then((data) => {
					
					if(data !== undefined){
						this.getDeviceList();
					}
				});
			},
			
			handleSizeChange(size) {
				this.pageSize = size;				
				this.handleCurrentChange(1);
			},
			handleCurrentChange(val) {
				this.page = val;				
				this.getDeviceList();
			},
			getDeviceList() {

				this.listLoading = true;
				AdminAPI.getDeviceList().then(({
					data: jsonData
				}) => {
					if(jsonData !== null) {
						this.devices = jsonData;
					} else {
						this.$message({
							messsage: `获取设备列表失败:${data.msg}`,
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
			this.getDeviceList();
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