<template>
<div class="main-content cmcc-log">

	<el-row class="toolbar searchparam">
			
			<el-col  :span=60 class="search-action-wrap" style="margin-bottom:10px;">
				<div>		
					<el-button size="small" @click="batchRemove" :disabled="this.sels.length === 0">删除用户</el-button>
					<el-button size="small" @click="unlockUser" :disabled="this.sels.length === 0">解锁用户</el-button>
					<el-button size="small" @click="addUser">增加用户</el-button>					
				</div>				
				
			</el-col>
	</el-row>

	<!--列表-->
	<section class="grid-content">
		<el-table :data="userList" resizable border highlight-current-row stripe show-overflow-tooltip
			:default-sort="tableSort"  @selection-change="handleSelectionChange"
			ref="table"			
			v-loading="searchLoading">

			<el-table-column  type="selection" width="50">
			</el-table-column>
			<el-table-column  prop="name"  label="账号" width="200" align="center">
			</el-table-column>
			<el-table-column prop="role"  label="权限"  align="center" width="100">
				<template scope="scope">
					{{scope.row.role | roleFormat}}
				</template>	
			</el-table-column>

			<el-table-column prop="groups"  label="组"  align="center">
				<template scope="scope">
					{{scope.row.groups | groupsFormat(groupMaps)}}
				</template>	
			</el-table-column>
			
		</el-table>
	</section>
	
</div>
</template>

<script>
	import { mapGetters, mapActions } from 'vuex';
	import { openModal } from '../../common/js/modal';
	import { AdminAPI } from '../../api';
	import Filters from '../../common/js/filters';

	import addUserDlg from './AddUserDlg';
	const openAddUserDlg = openModal(addUserDlg);
	

	export default {
		data() {
			return {
				tableSort: {
					prop: 'name',
					order: 'desending',
				},
			
				searchLoading: false,
				// selector
				//searchForm: { ...defaultSearchForm },
				userList: [],
				sels: [],

				contextMenuRow: null,
				contextMenuPosition: {
					top: 0,
					left: 0
				},	
				groups:[],
				groupMaps:{}			
			}
		},
		filters: {
          dateFormat: Filters.dateFormat,
		  roleFormat: Filters.formatRole,
		  groupsFormat: (v,maps) => {
			    var g="";
				for(var i in v){
					g += maps[v[i].groupId] + ","
				}				
			  	return g;
			  }
      	},
		computed: {			
			...mapGetters('login', {
				sysUserInfo: 'loginInfo'
			}),
		},
		methods: {
			...mapActions('admin', ['updateGroupList']),
			// table
			handleSelectionChange(sels) {
				this.sels = sels;
			},
			reset() {
				//this.$refs.searchForm.resetFields();
			},

			addUser(){
				openAddUserDlg({
					data: {
						groups:this.groups
					}
				}).then((data) => {
					if(data) {
						this.$message({
							type: 'success',
							message: '添加用户成功!'
						});

						this.getUserList();
					}
				}).catch(() => {
					this.$message.error('添加用户成功失败!');
				});
			},
			unlockUser(){
				let names =this.sels.map(item => item.name);
				AdminAPI.unlock(
						names
					).then(({data}) => {								
						if(data.status === 0){
							this.$message({
								message: '解锁成功!',
								type: 'success'
							});
							this.getUserList();
						}else{
							this.$message({
								message: '解锁失败!',
								type: 'error'
							});
						}
					}).catch((error) => {
						this.$message.error(error);
					});
			},
			batchRemove(){
				let flag = false;
				this.sels.forEach((item, index) =>{					
					if(item.name === 'admin' || item.name === this.sysUserInfo.name){
							flag = true;
							return;
					}
				});
				if(flag){
					this.$message({
								message: '不能删除admin或者当前登陆用户',
								type: 'warning'
							});
					return;
				}

				let ids =this.sels.map(item => item.id);								
										
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {					
					
					this.listLoading = true;
					AdminAPI.removeUsers(
						ids
					).then(({data}) => {								
						if(data.status === 0){
							this.$message({
								message: '删除成功!',
								type: 'success'
							});
							this.getUserList();
						}else{
							this.$message({
								message: '删除失败!',
								type: 'error'
							});
						}
					}).catch((error) => {
						this.$message.error(error);
					});

					this.listLoading = false;
				}).catch((error) => {
					if(error instanceof Error) {
						this.$message.error('删除失败!'+error);
					}
				});
			},
			getUserList() {
				
				this.searchLoading = true;
				AdminAPI.getUserList().then(({data}) => {
					
					this.userList = data;
					this.total = data.length;

					this.searchLoading = false;
				}).catch(() => {
					this.$message.error(`获取用户列表失败!`);
					this.searchLoading = false;
				});
			},

			getDeviceGroup(){
				AdminAPI.getDeviceGroups().then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						this.getUserList();
						
						this.groups = jsonData.data;
						if(this.groups.length <= 0)return;						
						 for(var i in this.groups){
							 this.groupMaps[this.groups[i].groupId] = this.groups[i].groupName;
						 }
					} else {
						this.$message({
							messsage: `获取组失败:${data.msg}`,
							type: 'error'
						})
					}
				});
			},
			
			
		},
        mounted() {
			 
			 this.getDeviceGroup();
        }
	}

</script>

<style scoped lang="scss">
	.main-content {
		padding: 0 10px;
	}

	.search-action-wrap {
		text-align: right;
		white-space: nowrap;
		line-height: 36px;
		> div:not(:last-child) {
			margin-bottom: 10px;
		}
	}

	.color-gray {
		color: #48576a;
	}

	.device-batch-params-config {
		.cmcc-action-wrap {
			line-height: 36px;
			margin-bottom: 5px;
		}
		.upload {
			float: right;
		}
		.el-progress {
			width: 154px;
			float: right;
			margin-top: 10px;
			margin-right: -5px;
		}
	}
	.cmss-content-menu {
		position: absolute;
		margin-top: -40px;
	}

	.el-dropdown-menu {
		margin-top: -20px;
	}

</style>

<style lang="scss">
	.cell {
		.el-tag {
			margin-left: 0.2em;
			margin-right: 0.2em;
		}
	}
	.search-form {
		.el-form-item {
			white-space: nowrap;
			margin-right: 5px;
		}
		.el-form-item__content {
			width: 110px;
		}
		.link-net-status {
			> label {
				transform: translateX(-5px);
			}
		}
		.action-wrap {
			.el-form-item__content {
				width: auto;
			}
		}
		.date-range {
			.el-form-item__content {
				width: auto;
			}
		}
	}
.cmcc-log {
	.el-table .cell {
		white-space: nowrap;
	}
}

</style>
