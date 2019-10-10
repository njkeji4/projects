<template>
<div class="main-content cmcc-alarm">
	<el-form :model="ipsecForm" :rules="rules" ref="ipsecForm" label-width="120px" class="ipsec-form"
		v-loading="loading">

		<el-form-item label="IP" prop="ip">
			<el-input type="text" v-model="ipsecForm.ip" auto-complete="off" placeholder="IP"></el-input>
		</el-form-item>
		<el-form-item label="端口" prop="ip">
			<el-input type="text" v-model="ipsecForm.port" auto-complete="off" placeholder="端口"></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="submitForm('ipsecForm')">更新</el-button>
			<el-button @click="resetForm('ipsecForm')">重置</el-button>
		</el-form-item>
	</el-form>
</div>
</template>

<script>
	import { AdminAPI } from '../../api';
	import { every } from 'lodash';

	export default {
		data() {
			function validateIpAndPort(input) {
				var parts = input.split(":");
				var ip = parts[0].split(".");
				var port = parts[1];
				return (port === undefined || validateNum(port, 1, 65535)) &&
					ip.length == 4 &&
					every(ip, function (segment) {
						return validateNum(segment, 0, 255);
					});
			}

			function validateNum(input, min, max) {
				var num = +input;
				return num >= min && num <= max && input === num.toString();
			}

			var validaeIp = (rule, value, callback) => {
				if (!value) {
					return callback(new Error('请输入IP地址'));
				}
				if (!validateIpAndPort(value)) {
					callback(new Error('请输入正确格式的ip:port'));
				} else {
					callback();
				}
			};
		
			return {
				initIp: '',
				loading: false,
				ipsecForm: {
					ip: '',
					port:''
				},
				rules: {
					ip: [{
						validator: validaeIp,
						trigger: 'blur'
					}]
				}
			};
		},
		methods: {
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (this.loading) {
						return;
					}
					if (valid) {
						let ip = this.ipsecForm.ip;
						this.loading = true;

						AdminAPI.updateIPSec({ip}).then(({ data }) => {
							if (data.status === 0) {
								this.$message.success(`更新ipsec成功!`);
								this.initIp = ip;
							} else {
								this.$message.error(`更新ipsec失败:${data.msg}!`);
							}
							this.loading = false;
						}).catch(() => {
							this.$message.error(`更新ipsec失败!`);
							this.loading = false;
						});
					}
				});
			},
			resetForm(formName) {
				// this.$refs[formName].resetFields();
				this.ipsecForm.ip = this.initIp;
			},
			loadData() {
				this.loading = true;
				AdminAPI.getIPSec().then(({ data }) => {
					if (data.status === 0) {
						this.ipsecForm.ip = this.initIp = data.data.ip;
					} else {
						this.$message.error(`获取ipsec失败:${data.msg}!`);
					}
					this.loading = false;
				}).catch(() => {
					this.$message.error(`获取ipsec失败!`);
					this.loading = false;
				});
			}
		},
		mounted() {
			
		}
	}
</script>

<style scoped lang="scss">
 .ipsec-form {
	 width: 60%;
	 max-width: 25em;
 }
</style>
