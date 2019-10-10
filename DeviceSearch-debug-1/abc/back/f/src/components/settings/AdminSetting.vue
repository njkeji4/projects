<template>
	<el-row>
		<el-col :span="16">
			<el-form ref="form" :model="form" label-width="14em" :rules="formRules">
			
				
				<el-form-item  size="small" prop="alarmPhone" label="告警手机号码">
					<el-input v-model="form.alarmPhone"></el-input>
				</el-form-item>

				<el-form-item  size="small" prop="errorTimes" label="比对错误次数报警">
					<el-input v-model="form.errorTimes"></el-input>
				</el-form-item>
				
				<el-form-item>
					<el-button type="primary" size="small" @click="onSubmit">保存</el-button>
					<el-button size="small">取消</el-button>
				</el-form-item>
			</el-form>
		</el-col>
	</el-row>
</template>

<script>
	import { AdminAPI } from '../../api';
	export default {
		data() {			
			return {				
				form: {
					alarmPhone:'',
					errorTimes: '',					
				},				
				
				alarmPhone:'',
				errorTimes:'',
				formRules: {	
					alarmPhone:[
						{ required: true, message: '请输入手机号码', trigger: 'blur' },
					],		
					errorTimes: [
						{ required: true, message: '请输入次数', trigger: 'blur' },
					],				
				}
			}
		},
		methods: {
			onSubmit() {							
				this.$refs.form.validate(valid => {
					if(valid) {
						AdminAPI.updateSetting({
							...this.form
						}).then(({
							data
						}) => {
							if(data.status === 0) {
								this.$message({
									type: 'success',
									message: '更新成功!',
								})
							} else {
								this.$message.error(`更新失败:${data.msg}!`);
							}
						}).catch(() => {
							this.$message.error(`更新失败!`);
						});
					}
				})
			},
			loadData() {
				AdminAPI.getSetting().then(({
					data
				}) => {
					if(data.status === 0) {	
						if(data.data !== null)					
							this.form = data.data;				
						
					} else {
						this.$message.error(`获取系统设置失败:${data.msg}!`);
					}
				}).catch(() => {
					this.$message.error(`获取系统设置失败!`);
				});
			}
		},
		mounted() {
			this.loadData();
		}
	}
</script>