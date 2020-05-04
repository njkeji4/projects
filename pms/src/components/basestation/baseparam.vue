<template>
	<el-row>
		<el-col :span="10">
			<el-form ref="form" :model="form" label-width="14em" :rules="formRules">
			
				
				<el-form-item  size="small" prop="traffic" label="流量少于(MB):">
					<el-input v-model="form.traffic"></el-input>
				</el-form-item>

                <el-form-item  size="small" prop="prbdown" label="prb下行:">
					<el-input v-model="form.prbdown"></el-input>
				</el-form-item>

                 <el-form-item  size="small" prop="prbup" label="prb上行:">
					<el-input v-model="form.prbup"></el-input>
				</el-form-item>

                <el-form-item  size="small" prop="activeUser" label="激活用户数:">
					<el-input v-model="form.activeUser"></el-input>
				</el-form-item>

                <el-form-item  size="small" prop="duration" label="至少连续小时数:">
					<el-input v-model="form.duration"></el-input>
				</el-form-item>

                 <el-form-item  size="small" prop="daysCount" label="满足条件天数:">
					<el-input v-model="form.daysCount"></el-input>
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
					traffic:10,		
                    prbdown:3,
                    prbup:3,
                    activeUser:0.5,
                    duration:2,
                    daysCount:4		
				},	
				
				formRules: {	
					traffic:[
						{ required: true, message: '输入要求的值', trigger: 'blur' },
					],		
					prbdown: [
						{ required: true, message: '输入要求的值', trigger: 'blur' },
					],
                    prbup:[
						{ required: true, message: '输入要求的值', trigger: 'blur' },
					],		
					activeUser: [
						{ required: true, message: '输入要求的值', trigger: 'blur' },
					],				
                    duration:[
						{ required: true, message: '输入要求的值', trigger: 'blur' },
					],		
					daysCount: [
						{ required: true, message: '输入要求的值', trigger: 'blur' },
					],								
				}
			}
		},
		methods: {
			onSubmit() {							
				this.$refs.form.validate(valid => {
					if(valid) {
						AdminAPI.setBaseConf({
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
				AdminAPI.getBaseConf().then(({
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