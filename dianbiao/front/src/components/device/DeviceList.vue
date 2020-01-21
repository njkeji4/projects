<template>
	<div class="main-content" style="padding:3px;">
		<el-row :gutter=20 class="toolbar searchparam">
			
			<el-col  :span=10 class="search-action-wrap" style="margin-bottom:10px;">
				<div style="float:left">		
					<el-button size="small" @click="addDevice">增加设备</el-button>
					<el-button size="small" @click="offDevice" :disabled="this.sels.length === 0" :loading="actionLoading">拉闸</el-button>
					<el-button size="small" @click="onDevice" :disabled="this.sels.length === 0" :loading="actionLoading">合闸</el-button>
					<el-button size="small" @click="batchRemove" :disabled="this.sels.length === 0">删除设备</el-button>
					<el-button size="small" @click="settingDevice" :disabled="this.sels.length == 0">设置自动拉合闸时间</el-button>			
						
				</div>
			</el-col>
			<el-col :span=10 class="paramleft">
				<el-form :inline="true" size="small" :model="searchForm" class="search-form" label-width="6em" ref="searchForm">
					<el-form-item label="" prop="deviceName">
						<el-input size="small" v-model="searchForm.deviceName" placeholder="设备名称"></el-input>
					</el-form-item>
					<el-form-item label="" prop="deviceNo">
						<el-input size="small" v-model="searchForm.deviceNo" placeholder="设备编号"></el-input>
					</el-form-item>

					<el-button size="small" @click="searchDevice">查询</el-button>
					
				</el-form>
			</el-col>

			<el-col :span=4>
				<el-upload class='ensure' 
						ref="uploadForm"
						name="uploadFile"
						
						:action="uploadUrl"  
						
						:auto-upload="true"				
						:on-success="uploadSuc"
						:on-error="uploadFail"
						:show-file-list='false'
						>	
						<el-button size="small" type="primary">导入设备</el-button>
					</el-upload>
			</el-col>
			
		</el-row>

		<section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe v-loading="listLoading" ref="table" 
			 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			  class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">				
				</el-table-column>
				
				<el-table-column  sortable="custom" prop="deviceName" label="设备名称" width="200"></el-table-column>
				
				<el-table-column  prop="deviceNo" label="设备编号" width="150" sortable="custom"></el-table-column>				
						
				<el-table-column  prop="status" label="设备状态" width="120" sortable="custom">
					<template slot-scope="scope">						
						<el-tag :type="scope.row.status == '0' ? 'success' : 'danger'" close-transition>{{scope.row.status == '0'?'在线':'离线'}}</el-tag>					
					</template>
				</el-table-column>

				<el-table-column  sortable="custom" prop="switchStat" label="开关状态" width="140">
					<template slot-scope="scope">						
						<el-tag :type="scope.row.switchStat == 0 ? 'success' : 'danger'" close-transition>{{scope.row.switchStat == 0?'合闸':'拉闸'}}</el-tag>					
					</template>
				</el-table-column>

				<!--el-table-column  sortable="custom" prop="groupactionEnergy" label="组合有功电能量" width="150">
				</el-table-column-->
				
				<el-table-column  sortable="custom" prop="actionEnergy" label="电量(KW)" width="150"></el-table-column>
				<!--el-table-column  sortable="custom" prop="reactionEnergy" label="反向有功电能量" width="150"></el-table-column-->
				<el-table-column  sortable="custom" prop="vol" label="电压" width="120"></el-table-column>
				<el-table-column  sortable="custom" prop="cur" label="电流" ></el-table-column>
				<!--el-table-column  sortable="custom" prop="actionPower" label="有功功率" width="140"></el-table-column-->
				<!--el-table-column  sortable="custom" prop="freq" label="电网频率" width="140"></el-table-column>
				<el-table-column  sortable="custom" prop="factor" label="功率因数" width="140"></el-table-column-->

			
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

	import DeviceEditDlg from './SettingDevice';
	import AddDeviceDlg from './AddDevice';
	import DeviceUpload from './DeviceUpload.vue';

	const openDeviceEditDlg = openModal(DeviceEditDlg);
	const openAddDeviceDlg = openModal(AddDeviceDlg);
	const openImportDeviceDlg = openModal(DeviceUpload);

	export default {
		data() {
			return {				
				searchForm: { 
					deviceName:'',					
					deviceNo:'',				
				},
				uploadUrl:AdminAPI.uploadUrl,
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
					name: 'deviceNo',
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
				sort:'deviceNo',
				order:'desc'
			}
		},
		filters: {
			groupIdFormat:(v,maps) => {return maps[v];}
		},
		computed: {

		},
		watch: {
			'$route'(to,from){
				if(from.fullPath !== to.fullPath){
					this.getDeviceList();
				}
			}
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
				openAddDeviceDlg().then((data) => {
					
					if(data !== undefined){
						this.getDeviceList();
					}
				});
			},

			importDevice(){
				openImportDeviceDlg().then((data) => {
					
					if(data !== undefined){
						this.getDeviceList();
					}
				});
			},

			offDevice() {
				var row;
				if(this.sels && this.sels.length > 0) {
					row = this.sels[0];
				}else
					return;
				
				var ids = this.sels.map(item => item.deviceNo)			
				this.actionLoading=true;
				AdminAPI.switchOffDevice(ids).then(({
					data: data
				}) => {
					this.actionLoading=false;
					console.log(data);
					if(data !== null) {
						this.$message(data.message);
						
					} else {
						this.$message({
							messsage: `拉闸失败:${data.message}`,
							type: 'error'
						})
					}
				});
				
				
			},

			onDevice() {
				var row;
				if(this.sels && this.sels.length > 0) {
					row = this.sels[0];
				}else
					return;

				this.actionLoading=true;
				var ids = this.sels.map(item => item.deviceNo)	
				AdminAPI.switchOnDevice(ids).then(({
					data: data
				}) => {
					this.actionLoading=false;
					
					if(data !== null) {
						this.$message(data.message);
						
					} else {
						this.$message({
							messsage: `合闸失败:${data.message}`,
							type: 'error'
						})
					}
				});
				

			},

			settingDevice(){
				var row;
				if(this.sels && this.sels.length > 0) {
					row = this.sels[0];
				}
				openDeviceEditDlg({
					data: {
						deviceInfo: this.sels
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

				var searchParams = _.omitBy(this.searchForm, (item) => item == "" || _.isNil(item));
				searchParams.page = this.page - 1;
				searchParams.size = this.pageSize;
				searchParams.sort=this.sort;//"deviceNo";
				searchParams.order=this.order;//"asc";
				
				this.listLoading = true;
				AdminAPI.searchDevice(searchParams).then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						this.total = jsonData.data.total;
						this.devices = jsonData.data.content;
						this.total = jsonData.data.totalElements;
						this.listLoading = false;
					} else {
						this.$message({
							messsage: `获取设备列表失败:${data.msg}`,
							type: 'error'
						})
					}
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
			uploadSuc(res, file, fileList){
				if(res.status === 0){
					this.$message({
						message: '上传成功!',
						type: 'success'
					});
					this.getDeviceList();
					
				}else{
					this.$message.error('上传文件失败!'+res.msg);
				}
			},
			uploadFail(err){
				this.$message({
						message: '上传失败!'+err,
						type: 'error'
					});
			},
			batchRemove(){
					
				if(this.sels && this.sels.length > 0) {					
				}else
					return;

				var ids = this.sels.map(item => item.deviceNo)	
				AdminAPI.deleteDevices(ids).then(({
					data: data
				}) => {	
					if(data !== null) {
						this.$message('操作成功');
						this.getDeviceList();
					} else {
						this.$message({
							messsage: `操作失败:${data.message}`,
							type: 'error'
						})
					}
				});
			}
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