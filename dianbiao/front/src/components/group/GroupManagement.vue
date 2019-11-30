<template>
<div class="main-content cmcc-log">

	<el-row class="toolbar searchparam">
			
			<el-col  :span=60 class="search-action-wrap" style="margin-bottom:10px;">
				<div>		
					<el-button size="small" @click="batchRemove" :disabled="this.sels.length === 0">删除用户组</el-button>
					<el-button size="small" @click="addUser">增加用户组</el-button>					
				</div>				
				
			</el-col>
	</el-row>

	<!--列表-->
	<section class="grid-content">
		<el-table :data="groupList" resizable border highlight-current-row stripe show-overflow-tooltip
			:default-sort="tableSort"  @selection-change="handleSelectionChange"
			ref="table"			 @row-dblclick="handleDblClickRow"
			v-loading="searchLoading">

			<el-table-column  type="selection" width="50">
			</el-table-column>
			<el-table-column  prop="groupName"  label="组名" align="center">
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

	import addUserGroupDlg from './AddUserGroupDlg';
	import editUserGroupDlg from './editUserGroupDlg';
	const openAddUserDlg = openModal(addUserGroupDlg);

	const openGroupEditDlg =openModal(editUserGroupDlg);
	

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
				groupList: [],
				sels: [],

				contextMenuRow: null,
				contextMenuPosition: {
					top: 0,
					left: 0
				},				
			}
		},
		filters: {
          dateFormat: Filters.dateFormat,
		  roleFormat: Filters.formatRole
      	},
		computed: {			
			...mapGetters('login', {
				sysUserInfo: 'loginInfo'
			}),
		},
		methods: {
			...mapActions('admin', ['updateGroupList']),
			// table

			handleDblClickRow(row, event) {				
				openGroupEditDlg({
					data: {
						group: row						
					}
				}).then((data) => {
					// TODO: 拦截close事件,添加参数
					if(data !== undefined){
						this.getGroupList();
					}
				});
			},

			handleSelectionChange(sels) {
				this.sels = sels;
			},
			reset() {
				//this.$refs.searchForm.resetFields();
			},

			addUser(){
				openAddUserDlg({
					
				}).then((data) => {
					if(data) {
						this.$message({
							type: 'success',
							message: '添加组成功!'
						});

						this.getGroupList();
					}
				}).catch(() => {
					this.$message.error('添加组失败!');
				});
			},
			batchRemove(){

				let ids =this.sels.map(item => item.groupId);								
										
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {					
					
					this.listLoading = true;
					AdminAPI.removeGroup(
						ids
					).then(({data}) => {								
						if(data.status === 0){
							this.$message({
								message: '删除成功!',
								type: 'success'
							});
							this.getGroupList();
						}else{
							this.$message({
								message: '删除失败:'+data.message,
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

			getGroupList() {
				this.searchLoading = true;
				AdminAPI.getDeviceGroups().then(({data}) => {
					
					this.groupList = data.data;
					this.total = data.length;

					this.searchLoading = false;
				}).catch(() => {
					this.$message.error(`获取用户组失败!`);
					this.searchLoading = false;
				});
			},
		},

        mounted() {
			 this.getGroupList();
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
