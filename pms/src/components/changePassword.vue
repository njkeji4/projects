<template>
	<el-dialog title="修改密码" ref="modal" :visible.sync="modalVisible" :close-on-click-modal="false" 
        class="edit-device-form">

		<el-form :model="formData" label-width="7em" :rules="formRules" ref="form">
			<el-form-item label="用户名" prop="uname">
				<el-input readonly v-model="formData.username" size="small"></el-input>
			</el-form-item>
			<el-form-item label="当前密码" prop="password">
				<el-input type="password" v-model="formData.password" size="small" placeholder="请输入当前密码" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="新密码" prop="newPassword" :required="true">
				<el-input type="password" v-model="formData.newPassword" size="small" placeholder="请输入新密码" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="确认密码" prop="newPasswordConfirm" :required="true">
				<el-input type="password" v-model="formData.newPasswordConfirm" size="small" placeholder="请两次输入新密码" auto-complete="off"></el-input>
			</el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button size="small" @click="modalVisible = false">取消</el-button>
			<el-button size="small" type="primary" @click="submit" :loading="submitLoading">提交</el-button>
		</div>

	</el-dialog>
</template>

<script>
	import * as _ from 'lodash';
	import { mapGetters, mapActions } from 'vuex';
	import { AdminAPI } from '../api';
	import md5 from 'blueimp-md5';

    export default {
		props: {
			uname: [String],
		},
        data() {
        	// reg1:"/^(?=.*[a-zA-Z0-9].*)(?=.*[a-zA-Z\\W].*)(?=.*[0-9\\W].*).{6,20}$/",
        	//const reg=eval("("+reg1+")");
        	//var reg="";
			return  {// add
			
				modalVisible: true,
				submitLoading: false,
			    regex:null,
				formRules: {
					password: [
						{ required: true, messsage: '请输入当前密码!', trigger: 'change' }
					],
					newPassword: [
						{ validator: (rule, value, callback) => {
							//console.log(value);
							var pattern = new RegExp(this.regex); 
							if(value === '') {
								callback(new Error('请输入新密码!'));
							}else{
                                callback();
                            }
						}, trigger: 'blur' },
					],
					newPasswordConfirm: [
						{ validator: (rule, value, callback) => {
							if (value === '') {
								callback(new Error('请再次输入新密码!'));
							} else if (value !== this.formData.newPassword) {
								callback(new Error('两次输入的新密码不一致!'));
							} else {
								callback();
							}
						}, trigger: 'blur' }
					],
				},
				formData: {
					username:'',
					password: '',
					newPassword: '',
					newPasswordConfirm: '',
				},
			};          
        },
		methods: {
			submit() {
               
				this.$refs.form.validate((valid) => {
					if (valid) {
						this.submitLoading = true;

						AdminAPI.changePassword({
							username:this.formData.username,
							password: md5(this.formData.password),
							newPassword: md5(this.formData.newPassword),
						}).then(({data}) => {
							if (data.status === 0) {
								this.$message({
									message: `密码修改成功!`,
									type: 'success'
								});

								this.$emit('data', { result: 'success' });
								this.modalVisible = false;
							} else {
								this.$message.error(`密码修改失败:${data.message}`);
							}
							this.submitLoading = false;
						}).catch(() => {
							this.$message.error(`密码修改失败!`);
							this.submitLoading = false;
						});
					}
				});
			},
			getPasswordRule(){				
				let param = {};
				AdminAPI.getSystemSetting(param).then(({data}) => {
					const passwordMaxLength=data.data.settings.passwordMaxLength;
					const passwordMinLength=data.data.settings.passwordMinLength;
					const passwordStrategy=data.data.settings.passwordStrategy;
					var	regex="";
					//console.log("201819wf",passwordStrategy);
					if(passwordStrategy==="1"){//字母+数字
						regex= "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{" + passwordMinLength+","+passwordMaxLength+"}$";
					}else if(passwordStrategy==="2"){//字母+特殊符号
						regex= "^(?=.*[_!@#$%^&*])(?=.*[a-zA-Z])[a-zA-Z\W_!@#$%^&*]{" + passwordMinLength+","+passwordMaxLength+"}$";
					}else if(passwordStrategy==="3"){//数字+特殊符号
						regex= "^(?=.*[_!@#$%^&*])(?=.*[0-9])[0-9\W_!@#$%^&*]{" + passwordMinLength+","+passwordMaxLength+"}$";
					}else{
						regex= "^(?=.*[_!@#$%^&*])(?=.*[0-9])(?=.*[a-zA-Z])[0-9A-Za-z_!@#$%^&*]{" + passwordMinLength+","+passwordMaxLength+"}$";
					}
					this.regex = regex;

					//console.log("201814",this.regex);
				
				}).catch((err) => {
					this.$message.error('Error', err);

				});			
			}
		},
		mounted() {
			this.formData.username = this.username;
			//this.getPasswordRule();			
		}
	}
</script>


<style scoped lang="scss">
    .edit-device-form {
        .el-dialog {
            width: 200px;
        }
        .el-dialog__body {
            padding-top: 5px;
        }
        .el-form--inline .el-form-item__content {
            width: 150px;
        }
        .el-tabs__header {
            margin-bottom: 0;
        }
    }
</style>

