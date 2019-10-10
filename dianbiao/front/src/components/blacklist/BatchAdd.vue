<template>
	<el-dialog title="批量上传" ref="modal" :visible.sync="modalVisible" 
		:close-on-click-modal="false"
		class="add-device-form">

		<el-form ref="form" :model="form" :rules="uploadformrules">
			
		<el-form-item class="file-wrap" prop="file">

			<el-upload class='ensure' 
				ref="uploadForm"
				name="uploadFile"
                accept=".xls,.xlsx"
				:data="dataParams"
				:action="uploadUrl"  
				:auto-upload="false"				
				:on-success="uploadSuc"
				:on-change="onchange"
				>
				<div>
				<el-button size="small" type="primary">请选择文件</el-button>
				<div  class="el-upload__tip">
                    文件包含三列，从第二行开始导入数据:
                    姓名， 身份证，性别
                </div>	
				</div>			
	
			</el-upload>
		</el-form-item>

		<el-form-item label="适用组" prop="groups" >
				<el-select clearable v-model="form.groups" multiple placeholder="请选择">
					<el-option v-for="item in groups" :label="item.groupName" :value="item.groupId"></el-option>
				</el-select>
		</el-form-item>
		
		<el-form-item class="subbut">
			<el-button size="small" @click="modalVisible = false">取消</el-button>
			<el-button size="small" type="primary" 
				@click="uploadSubmit">提交</el-button>
		</el-form-item >
		
	</el-form>
	</el-dialog>
</template>

<script>
	/* eslint-disable */
	
	import { mapGetters } from 'vuex';	
	import { AdminAPI } from '../../api';

	export default {
		
		data: function() {
			return { // add				
				form: {                   
                    file: '',
					groups:[],
                },
                dataParams:{groups:[]},               
				uploadUrl:AdminAPI.uploadBlackListUrl,
				modalVisible: true,
				submitLoading: false,
				disabled:true,
				versions:['1','2'],				
				fileList:[],
				uploadformrules: {
						groups:[
							{ required: true, message: '选择适用的设备组', trigger: 'change' },
						]
				},
			}
		},
		computed: {
			
		},
		methods: {		
			
			uploadSuc(res, file, fileList){
				if(res.status === 0){
					this.modalVisible = false;
					console.log('suc',res)
					this.$message({
						message: '上传成功!',
						type: 'success'
					});
					this.$emit('data', {result: 'success'});
				}else{
					this.$message.error('上传文件失败!'+res.msg);
				}
			},
			onchange(file,fileList){				
				this.fileList = fileList;				
			},

			uploadSubmit() {
				 this.$refs.form.validate((valid) => {
					this.dataParams.groups = this.form.groups;
					 if(this.fileList.length > 1){
						this.$message.error('一次上传一个文件');
						return;
					 }
					this.$refs.uploadForm.submit();
					
				 });
			},			
		},
		
		mounted() {
			
		}
	}
</script>

<style scoped lang="scss">
	
	.el-dialog{
		width:400px;
	}

	.el-upload__tip {
		display: inline-block;
		padding-left: 20px;
	}
	.subbut{
		text-align: right;
		margin: 22px 0 0 0;
	}
	.el-input,.el-select{
		width:400px !important;
	}
</style>