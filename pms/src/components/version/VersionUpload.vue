<template>
	<el-dialog title="上传" ref="modal" :visible.sync="modalVisible" 
		:close-on-click-modal="true"
		class="add-device-form">
		<el-form ref="form" :model="form" >
			
		<el-form-item class="file-wrap" prop="file">

			<el-upload class='ensure' 
				ref="uploadForm"
				name="uploadFile"
				:data="dataParams"
				:action="uploadUrl"  
				:auto-upload="false"				
				:on-success="uploadSuc"
				:on-change="onchange"
				>
				<el-button size="small" type="primary">请选择文件</el-button>
				<span slot="tip" class="el-upload__tip">文件名格式为：version.tar.gz或者version.zip</span>				
	
			</el-upload>
		</el-form-item>
		
		<el-form-item label="最低适用版本:" prop="minVersion">  
            <el-select clearable size="small" v-model="form.minVersion" placeholder="请选择">
				<el-option v-for="item in versions" :label="item" :value="item"></el-option>
			</el-select>
		</el-form-item>

		<el-form-item label="最高适用版本:" prop="maxVersion">  
            <el-select clearable size="small" v-model="form.maxVersion" placeholder="请选择">
				<el-option v-for="item in versions" :label="item" :value="item"></el-option>
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
	import { mapGetters } from 'vuex';	
	import { AdminAPI } from '../../api';
	

	export default {
		
		data: function() {
			return { // add				
				form: {
                    minVersion: '',
					maxVersion:'',
                    file: '',
					dateTimeRange: [],
					previousVersion:'',
                },
                dataParams:{minVersion:'', maxVersion:''},
                minVersion:'',
				maxVersion:'',
				uploadUrl:AdminAPI.uploadUrl,
				modalVisible: true,
				submitLoading: false,
				disabled:true,
				versions:['1','2'],				
				fileList:[],
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

			getExistedVersions(){
				let param = {};
				AdminAPI.getExistedVersions().then(({data}) => {					
					this.versions = data;
				}).catch((err) => {
					this.$message.error('Error', err);
				});
			},

			uploadSubmit() {
				 this.$refs.form.validate((valid) => {
				 	this.dataParams.minVersion = this.form.minVersion;
					 this.dataParams.maxVersion = this.form.maxVersion;
					 if(this.fileList.length > 1){
						this.$message.error('一次上传一个文件');
						return;
					 }
					this.$refs.uploadForm.submit();
					
				 });
			},			
		},
		
		mounted() {
			this.getExistedVersions();
		}
	}
</script>

<style scoped lang="scss">
	
	.el-upload__tip {
		display: inline-block;
		padding-left: 20px;
	}
	.subbut{
		text-align: right;
		margin: 22px 0 0 0;
	}
</style>