<template>
	<div class="main-content" style="padding:3px;">
		
			
	
		<div style="">		
			<div style="float:left;">		
				<el-button size="small" @click="addRoom">增加机房</el-button>
				<el-button size="small" @click="batchRemove" :disabled="this.sels.length === 0">删除机房
				</el-button>
			</div>

			<div style="float:left;margin-left:50px;">
				<el-form size="small" :inline="true" :model="searchForm" 
					class="search-form" label-width="6em" ref="searchForm">
					
					<el-form-item label="" prop="name">
						<el-input size="small" v-model="searchForm.name" placeholder="机房名称" 
							style="width:230px;margin-right:10px;"></el-input>

						<el-button size="small" @click="searchDevice">查询</el-button>
					</el-form-item>
					
				</el-form>
			</div>
		</div>

		<section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe v-loading="listLoading" ref="table" 
			 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			  class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">				
				</el-table-column>
				
				<el-table-column  sortable="custom" prop="name" label="机房名称" width="200"></el-table-column>
				
							
						
				<el-table-column  prop="hostip" label="空开总数" width="120" >
				</el-table-column>		

				<el-table-column  prop="hostStatus" label="在线空开" width="120" >
				</el-table-column>		

				<el-table-column  prop="hostStatus" label="离线空开" width="120" >
				</el-table-column>		

				<el-table-column  prop="hostStatus" label="拉闸空开" width="120">
				</el-table-column>

				<el-table-column  prop="hostStatus" label="合闸空开" width="120">
				</el-table-column>	

				<el-table-column  prop="address" label="机房地址" >
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
	import { AdminAPI, RoomAPI } from '../../api';
	import { openModal } from '../../common/js/modal';
	
	import AddRoomDlg from './AddRoom';	
	const openAddRoomDlg = openModal(AddRoomDlg);

	export default {
		data() {
			return {					
				searchForm: { 
					name:''		
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
					name: 'name',
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
			'$route'(to,from){
				if(from.fullPath !== to.fullPath){
					this.getDeviceList();
				}
			}
		},		
		methods: {		

			addRoom() {
				openAddRoomDlg().then((data) => {
					
					if(data !== undefined){
						this.getDeviceList();
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
				this.searchDevice();
			},
			
			reset() {
				this.$refs.searchForm.resetFields();
			},
			
			searchDevice() {
				this.page = 1;
				this.getDeviceList();
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
				RoomAPI.search(searchParams).then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						this.total = jsonData.data.total;
						this.devices = jsonData.data.content;
						this.total = jsonData.data.totalElements;
						this.listLoading = false;
					} else {
						this.$message({
							messsage: `获取机房列表失败:${data.msg}`,
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
			
			batchRemove(){					
				if(this.sels && this.sels.length <= 0) {	
					return;
				}
				var ids = this.sels.map(item => item.id)	
				RoomAPI.del(ids).then(({
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