<template>
	<el-dialog title="增加用户组" ref="modal" :visible.sync="modalVisible" :close-on-click-modal="false" class="add-device-form">
		<el-form :model="addForm" label-width="7em" :rules="formRules" ref="addForm">
			<el-form-item label="用户组名" prop="groupName">				
					<el-input v-model="addForm.groupName" size="small" placeholder="请输入组名" auto-complete="off"></el-input>				
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
				formRules: {
					groupName: [
						{ required: true, message: '请输入用户名', trigger: 'blur' },
						//{message: '字母开头,只能输入字母数字',pattern: /^[A-Za-z]([A-Za-z0-9]+$)/,trigger:'blur'}
					],					
				},
				addForm: {
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
					
						AdminAPI.addGroup(params).then(({data}) => {
							if (data.status === 0) {
								this.$message({
									message: `增加组成功!`,
									type: 'success'
								});

								this.$emit('data', { result: 'success' });
								this.modalVisible = false;
							} else {
								this.$message.error(`增加失败:${data.message}`);
							}
							this.submitLoading = false;
						}).catch(() => {
							this.$message.error(`增加失败!`);
							this.submitLoading = false;
						});
					}
				});
			}
		},
		mounted() {
					
		}
	}
</script>

<style scoped lang="scss">
</style>
