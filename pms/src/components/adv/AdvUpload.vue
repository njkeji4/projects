<template>
	<el-dialog title="上传" ref="modal" :visible.sync="modalVisible" 
		:close-on-click-modal="true"
		class="add-device-form">

		<el-form ref="form" :model="form" :rules="uploadformrules">
			
		<el-form-item class="file-wrap" prop="file">

			<el-upload class='ensure' 
				ref="uploadForm"
				name="uploadFile"
				:data="dataParams"
				:action="uploadUrl"  
				:auto-upload="false"				
				:on-success="uploadSuc"
				:on-change="onchange"
				:before-upload="beforeUploadAdv"
				>
				<el-button size="small" type="primary">请选择文件</el-button>
				<span slot="tip" class="el-upload__tip">可以是图片jpg/png或者可播放视频文件mp4</span>				
	
			</el-upload>
		</el-form-item>

		<el-form-item label="广告名称" prop="title" :label-width='labelwidth'>
			<el-input type="text" v-model="form.title"  ></el-input>
		</el-form-item>

		<el-form-item label="截止日期" prop="expire" :label-width='labelwidth'>
			<el-date-picker
				v-model="form.expire"
				type="date"
				value-format="timestamp"
				placeholder="选择日期">
			</el-date-picker>
		</el-form-item>
		
		<!--el-form-item label="文件类型" prop="adType" :label-width='labelwidth'>  
            <el-select clearable  v-model="form.adType" placeholder="请选择">
				<el-option label="图片" value="1" selected></el-option>
				<el-option label="视频" value="2"></el-option>
			</el-select>
		</el-form-item-->

		<el-form-item label="适用组" prop="groups" :label-width='labelwidth'>
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
					title: '',
					adType: '',
					groups:[],
					expire:'',
                },
                dataParams:{title:'', adType:'',groups:[],expire:''},               
				uploadUrl:AdminAPI.uploadAdvUrl,
				modalVisible: true,
				submitLoading: false,
				disabled:true,
				versions:['1','2'],				
				fileList:[],
				labelwidth:'90px',
				uploadformrules: {
						title: [
							{ required: true, message: '输入广告名称', trigger: 'change' },
						],
						//adType:[
						//	{ required: true, message: '选择广告类型', trigger: 'change' },
						//],
						groups:[
							{ required: true, message: '选择广告适用的设备组', trigger: 'change' },
						],
						expire:[
							{ required: true, message: '设置广告截止日期', trigger: 'change' },
						]

				},
			}
		},
		computed: {
			
		},
		methods: {		
			beforeUploadAdv(file){
				if(['image/jpeg','image/png','video/mp4'].indexOf(file.type) == -1){
					this.$message.error('只能上传 jpg/png, mp4 文件!');
					return false;
				}        		
				if (file.size / 1024 / 1024 > 100) {
					this.$message.error('上传视频大小不能超过100MB');
					return false;
				}

				if(['image/jpeg','image/png'].indexOf(file.type) >= 0){
					this.dataParams.adType = 1;
				}else{
					this.dataParams.adType = 2;
				}
			},
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
				 	this.dataParams.title = this.form.title;
					//this.dataParams.adType = this.form.adType;
					this.dataParams.groups = this.form.groups;
					this.dataParams.expire = this.form.expire;
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