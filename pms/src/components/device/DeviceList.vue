<template>
	<div class="main-content">
		

		<section class="grid-content">
			<el-table :data="devices" 
				v-loading="listLoading" ref="table"
			 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			 :row-class-name="function(row){return ('row-'+ row.rowIndex % 2) ;}"
			  >

				<el-table-column  prop="deviceName" label="合同编号" width="200"></el-table-column>
				
				<el-table-column  prop="deviceNo" label="合同名称" width="150" ></el-table-column>				
				
				<el-table-column  prop="dState" label="合同状态">
					<template slot-scope="scope">						
						<el-switch v-model="scope.row.dState"
								:active-value=0
								 :inactive-value=1
								@change="onoffDevice($event,scope.row,4)"
								active-color="#13ce66" inactive-color="#ff4949" />				
					</template>
				</el-table-column>
				
			
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
				
				testvalue:1,			
				searchForm: { 
					deviceName:'',					
					deviceNo:'',				
				},
				uploadUrl:AdminAPI.uploadUrl,
				actionLoading:false,
				groups:[],
				groupMaps:{},
				devices: [{deviceName:'abc'},{deviceName:'abc'},{deviceName:'abc'},{deviceName:'abc'},{deviceName:'abc'},{deviceName:'abc'}],
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
			
		}
	}
</script>


<style lang="scss">
	
	
	
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
	
	.el-pagination{
		text-align:right;
		.el-pagination__total{
			color:white;
		}
		.el-input__inner{
			background-color:transparent;
			color:white;
			border:none;
		}
		.btn-next, .btn-prev{
			background-color:transparent !important;
			color:white;
		}
		.el-pager > li {
			background-color:transparent;
			color:white;
		}
	}

	.el-table::before {//去掉最下面的那一条线
		height: 0px;
	}
	.el-table{
		background:rgba(10,13,51,1);
		opacity:0.7;
	
		.el-table__header-wrapper{
			.el-table__header > thead > tr{
				background-color:transparent;				
				th{
					background-color:transparent;
					border:none;
					.cell{
						color:rgb(255, 255, 255);
					}
				}
			}
		}
		
		.el-table__body-wrapper{
			
			.el-table__body > tbody > tr.row-0 {
				background:rgba(158,162,192,0.05);
				td{
					border:none;
					.cell{
						color:rgb(158, 162, 192);
					}
				}
			}
			.el-table__body > tbody > tr.row-1 {
				background:transparent;
				td{
					border:none;
					.cell{
						color:rgb(158, 162, 192);
					}
				}
			}
			.el-table__body > tbody > tr:hover {				
				background-color:transparent;
				 td{
					 background:rgba(158,162,192,0.5);
				 }
			}
		}
	}
</style>