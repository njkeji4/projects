<template>
	<el-dialog title="增加用户" ref="modal" :visible.sync="modalVisible" :close-on-click-modal="false" class="add-device-form">
		<el-form :model="addForm" label-width="7em" :rules="formRules" ref="addForm">
			<el-form-item label="用户名" prop="name">				
					<el-input v-model="addForm.name" size="small" placeholder="请输入用户名" auto-complete="off"></el-input>				
			</el-form-item>			

			<el-form-item label="权限" prop="role">
				<el-select clearable size="small" v-model="addForm.role" placeholder="请选择">
					<el-option v-for="item in roles" :label="item.name" :value="item.role"></el-option>
				</el-select>
			</el-form-item>

			<el-form-item label="管理组" prop="groups">
				<el-select clearable size="small" v-model="addForm.groupName" placeholder="请选择">
					<el-option v-for="item in groups" :label="item.groupName" :value="item.groupName"></el-option>
				</el-select>
			</el-form-item>
			
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button size="small" @click="modalVisible = false">取消</el-button>
			<el-button size="small" type="primary" @click="addUserSubmit" :loading="submitLoading">提交</el-button>
		</div>
	</el-dialog>
</template>

<script>
	import * as _ from 'lodash';
	import { mapGetters, mapActions } from 'vuex';
	import { AdminAPI } from '../../api';

    export default {
		props: {
			initData: {
				default: null
			},
			// formType: (add|copy)|edit
			formType: {
				default: 'add',
			},
		},
        data() {

			return  {// add				
				modalVisible: true,
				submitLoading: false,
				roles: [{"name":"管理员组","role":"ROLE_ADMIN"},
						{"name":"普通用户组","role":"ROLE_USER"},
						],
				formRules: {
					name: [
						{ required: true, message: '请输入用户名', trigger: 'blur' },
						{message: '字母开头,只能输入字母数字',pattern: /^[A-Za-z]([A-Za-z0-9]+$)/,trigger:'blur'}
					],					
					role: [
						{ required: true, message: '请选择一个组', trigger: 'blur' },
					],
				},
				addForm: {
					name:'',					
					role:'',
					groupName:'',
									
				},
			};          
        },
		computed: {		
			
		},		
		methods: {			
			addUserSubmit() {
				this.$refs.addForm.validate((valid) => {
					if (valid) {
						this.submitLoading = true;
						const params = {...this.addForm};
					
						AdminAPI.addUser(params).then(({data}) => {
							if (data.status === 0) {
								this.$emit('data', { result: 'success' });
								this.modalVisible = false;
							} else {
								this.$message.error(`增加账号失败:${data.message}`);
							}
							this.submitLoading = false;
						}).catch(() => {
							this.$message.error(`增加账号失败!`);
							this.submitLoading = false;
						});
					}
				});
			}
		},
		mounted() {
			//this.updateGroupList();				
		}
	}
</script>

<style scoped lang="scss">
</style>
