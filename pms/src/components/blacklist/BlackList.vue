<template>
	<div class="main-content" style="padding:3px;">

		<el-row class="" :gutter=20 style="">
			
			<el-col  :span=6 class="" style="">
				<div style="float:left">
					<el-button size="small" @click="addBlacklist">新增</el-button>	
					<el-button size="small" @click="removeBlacklist" :disabled="this.sels.length === 0" style="margin-right:10px">删除</el-button>
					<el-button size="small" @click="exportBlacklist" style="margin-right:10px;">导出</el-button>	
					<el-button size="small" @click="upload">批量导入</el-button>
				</div>
			</el-col>

			
			<el-col :span=15 class="paramleft">
				<el-form :inline="true" size="small" :model="searchForm" class="search-form" label-width="6em" ref="searchForm">
					<el-form-item label="" prop="name">
						<el-input size="small" v-model="searchForm.name" placeholder="姓名"></el-input>
					</el-form-item>
					<el-form-item label="" prop="cardNo">
						<el-input size="small" v-model="searchForm.cardNo" placeholder="身份证号码"></el-input>
					</el-form-item>
					<el-form-item label="" prop="groupName">
						<el-input size="small" v-model="searchForm.groupName" placeholder="所属组"></el-input>
					</el-form-item>
					<el-form-item label="" prop="sex">
						<el-select clearable size="small" v-model="searchForm.sex" placeholder="性别">
							<el-option  label="男" value="1"></el-option>
							<el-option  label="女" value="2"></el-option>
						</el-select>
					</el-form-item>
					
					<el-button size="small" @click="searchBlacklist">查询</el-button>	
					<el-button size="small" @click="reset">清除</el-button>					
					
				</el-form>
			</el-col>

		</el-row>

		<section class="grid-content">
			<el-table :data="blacklist" resizable border highlight-current-row stripe v-loading="listLoading" ref="table"
			  @selection-change="handleSelectionChange" 
			  class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">
				</el-table-column>
				
				<el-table-column   prop="name" label="姓名" width="240">
				</el-table-column>				
				<el-table-column  prop="cardNo" label="身份证号码" >
				</el-table-column>				
				<el-table-column  prop="sex" label="性别" >
					<template slot-scope="scope">						
						{{scope.row.sex == '2'?'女':'男'}}					
					</template>
				</el-table-column>

				<el-table-column  prop="groupName" label="所属组" >
				</el-table-column>		
					
			</el-table>	
		</section>

		<el-col class="toolbar">			
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page" :page-sizes="[10, 15, 20, 30, 40, 50, 100]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
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

	import BatchAdd from './BatchAdd';

	import BlackListAddDlg from './BlackListAddDlg';
	import { openModal } from '../../common/js/modal';
	import Filters from '../../common/js/filters';
	
	const openAddBlacklistDlg = openModal(BlackListAddDlg);
	const openBatchAddModal = openModal(BatchAdd);

	export default {
		data() {
			return {
				searchForm: { 
					name: '',	
					cardNo:'',
					sex:'',
					groupName:''
				},
				groups:[],
				batchAddUploadURL: AdminAPI.uploadBlackListUrl,    
				blacklist: [],
				total: 2,
				page: 1,				
				pageSize: 10,				
				listLoading: false,
				loading:false,
				sels: [], //列表选中列				
			}
		},
		computed: {

		},
		watch: {
			
		},
		methods: {		
			upload() {
				openBatchAddModal({
					data: {
						groups:this.groups
					}
				}).then((data)=>{
					
					this.getBlackList();
				});
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
			exportBlacklist(){
				//AdminAPI.exportBlackList();
				setTimeout(() => {
								window.open(location.origin+"/blacklist/export");
							}, 0);
			},
			
			uploadFileSuccess(data) {
                if(data.status === 0) {					                
                    this.$message({
                        message: '文件导入成功!',
                        type: 'success',
                    });
                    this.$refs.fileUpload.clearFiles();
					this.getBlackList();
                } else {
                    this.$message.error(data.msg);
                }
            },
            uploadFileError(data) {
                this.$message.error('文件导入失败!');
            },
            uploadFileBeforeUpload(file) {
                const suffix = file.name.substring(file.name.length-3);
                if(suffix !== 'xls' && suffix !== 'xlsx'){
                    this.$message.error('选择excel文件上传');
                    return false;
                }
                return this.$confirm('是否确定导入文件?', '导入文件', {
                    confirmButtonText: '导入',
                    cancelButtonText: '取消',
                    type: 'info'
                });
            },

			importFromFile(){
				openBatchAddModal({}).then(data => {
					if(data) {
						this.getBlackList();
					}
				});
			},

			handleSelectionChange: function(sels) {
				this.sels = sels;				
			},
			
			handleSizeChange:function(size){
				this.pageSize = size;
				this.handleCurrentChange(1);
			},
			handleCurrentChange:function(val){
				this.page = val;
				this.getBlackList();
			},

			addBlacklist(){
				openAddBlacklistDlg({
					data: {
						groups:this.groups
					}
				}).then((data)=>{
					if(data !== undefined){
						this.getBlackList();
					}
				});
			},			

			getBlackList:function(){
				let params = {
					page: this.page-1,
					size:this.pageSize,
					order:'asc',
					sort:'groupName'
				};				
				this._getBlackList(params);
			},

			_getBlackList(param){
				AdminAPI.searchBlackList(param).then(({data}) => {
					if(data.status === 0){
						this.blacklist = data.data.content;
						this.total = data.data.totalElements;
					}
				}).catch((err) => {
					this.$message.error('Error', err);
				});
			},

			reset() {
				this.$refs.searchForm.resetFields();
			},
			
			searchBlacklist(){

				var searchParams = _.omitBy(this.searchForm, (item) => item == "" || _.isNil(item));
				searchParams.page = this.page - 1;
				searchParams.size = this.pageSize;
				searchParams.sort="groupName";
				searchParams.order="asc";			

				this._getBlackList(searchParams);
			},

			removeBlacklist:function(){
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {
					
					let cardNos = this.sels.map(item => ({cardNo:item.cardNo, groupId:item.groupId}));
					
                    AdminAPI.removeBlacklist(cardNos).then((data) => {                    	
						this.$message({
							message: '删除成功!',
							type: 'success'
						});
						this.getBlackList();
					});
				}).catch((error) => {
					if(error instanceof Error) {
						this.$message.error('删除失败!' + error);
					}
				});
			}
		},
		created() {
			
		},
		mounted() {
			this.getBlackList();
			this.getDeviceGroup();
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