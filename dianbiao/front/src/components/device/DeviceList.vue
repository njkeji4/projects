<template>
	<div class="main-content" style="padding:3px;">
		<el-row :gutter=10 class="toolbar searchparam">			
			<el-col  :span=8 class="search-action-wrap" >
				<div style="float:left">	
				 
				 <el-select v-model="deviceType" size="small" style="margin-right:10px;" @change="changeType">
					<el-option
						v-for="item in deviceTypes"
						:key="item.value"
						:label="item.label"
						:value="item.value">
					</el-option>
				</el-select>	
					<el-button size="small" @click="addDevice">增加设备</el-button>
					<!--el-button size="small" @click="offDevice" :disabled="this.sels.length === 0" :loading="actionLoading">拉闸</el-button>
					<el-button size="small" @click="onDevice" :disabled="this.sels.length === 0" :loading="actionLoading">合闸</el-button-->
					<el-button size="small" @click="batchRemove" :disabled="this.sels.length === 0">删除设备</el-button>
				</div>
			</el-col>
			<el-col :span=10 class="paramleft">
				<el-form :inline="true" size="small" :model="searchForm" class="search-form" label-width="6em" ref="searchForm">
					<el-form-item label="" prop="roomName">
						<el-input size="small" v-model="searchForm.roomName" placeholder="机房名称"></el-input>
					</el-form-item>
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

		<DeviceDC v-if="deviceType === 1" :devices="devices" v-loading="listLoading"
		 	 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			 @onoffDevice="onoffDevice"
			 @settingDevice="settingDevice">
		</DeviceDC>

		<DeviceAC1 v-if="deviceType === 2" :devices="devices" v-loading="listLoading"
			 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			 @onoffDevice="onoffDevice"
			 @settingDevice="settingDevice">
		</DeviceAC1>

		<DeviceAC3 v-if="deviceType === 3" :devices="devices" v-loading="listLoading"
		 	 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			 @onoffDevice="onoffDevice"
			 @settingDevice="settingDevice">
		</DeviceAC3>

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

	import DeviceDC from './DeviceDC.vue';
	import DeviceAC1 from './DeviceAc1.vue';
	import DeviceAC3 from './DeviceAc3.vue';

	const openDeviceEditDlg = openModal(DeviceEditDlg);
	const openAddDeviceDlg = openModal(AddDeviceDlg);
	const openImportDeviceDlg = openModal(DeviceUpload);

	export default {
		components: {
		     DeviceDC,
			 DeviceAC1,
			 DeviceAC3
	    },
		data() {
			return {
				deviceType:1,
				deviceTypes:[{value:1,label:'直流4路'},{value:2,label:'单相交流'},{value:3,label:'三相交流'}],
				testvalue:1,			
				searchForm: { 
					deviceName:'',					
					deviceNo:'',	
					roomName:''			
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

			changeType(val){				
				this.searchDevice();
			},

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

			onoffDevice(val,row,branch){
				
				var device = row.deviceNo;	
				var states = ["aState", "bState", "cState", "dState"];			
				var state = states[branch];
				
				 row[states[branch-1]] = val == 0? 1 : 0;	//keep the value unchange

				if(val == 1){
					AdminAPI.switchOffDevice({addr:device,branch:branch}).then(({
						data: data
					}) => {
						this.actionLoading=false;
						
						if(data !== null && data.status === 0) {
							this.$message(data.message);
						} else {
							this.$message({
								messsage: `拉闸失败:${data.message}`,
								type: 'error'
							})
						}
					});
				}else{
					AdminAPI.switchOnDevice({addr:device,branch:branch}).then(({
						data: data
					}) => {
						this.actionLoading=false;						
						if(data !== null && data.status === 0) {
							this.$message(data.message);
						} else {
							this.$message({
								messsage: `合闸失败:${data.message}`,
								type: 'error'
							})
						}
					});
				}

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

			branchSetting(row,branch){
				if(row.branchSetting[branch] === 1){
					return 'hassetting';
				}
				return 'nosetting';
			},
			settingDevice(row,branch){
				console.log(row.branchSetting + "---"+branch);
				
				openDeviceEditDlg({
					data: {
						deviceInfo: row,
						branch:branch
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
				
				console.log("get devicelist:"+this.deviceType);

				var searchParams = _.omitBy(this.searchForm, (item) => item == "" || _.isNil(item));
				searchParams.page = this.page - 1;
				searchParams.size = this.pageSize;
				searchParams.sort=this.sort;//"deviceNo";
				searchParams.order=this.order;//"asc";
				searchParams.deviceType = this.deviceType;
				
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


<style lang="scss">
	.el-table th,
	.el-table__fixed-header-wrapper thead div,	
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
	.branchsetting{
		background-color:transparent !important;;
		border:none;
		padding:0px !important;
		font-size:20px;
		float:right;
		color:grey;
	}
	.hassetting{
		color:green !important;
	}
	
</style>