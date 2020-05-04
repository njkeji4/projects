<template>
	<!-- batch config -->
	<el-dialog :title="title" ref="modal"  width="30%" :visible.sync="modalVisible" :close-on-click-modal="false"
         class="edit-device-form">
         
		<el-form :model="batchEditForm" label-width="7em" :rules="batchEditFormRules" ref="batchEditForm">			
			<el-form-item label="设备组名字" prop="groupName">
				<el-input type="text" v-model="batchEditForm.groupName" size="small" ></el-input>
			</el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button size="small" @click="modalVisible = false">取消</el-button>
			<el-button size="small" type="primary" @click="batchEdidtSubmit" :loading="batchConfigLoading">提交</el-button>
		</div>
	</el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { AdminAPI } from '../../api';

export default {
    data: function() {
        return {          
           modalVisible: true,			
           batchConfigLoading: false,           
           title: '增加组',
           batchEditForm:{
               groupName:''
           },
           batchEditFormRules: {
                groupName: [
                    { required: true, message: '输入设备组名字', trigger: 'change' }
                ]
           },
        };
    },
	computed: {
	},
    created() {
    },

    methods: {
		batchEdidtSubmit() {            
			this.$refs.batchEditForm.validate(valid => {
				if(valid) {		                   	
					AdminAPI.addGroup(
                        {groupName:this.batchEditForm.groupName}
                    ).then(({data}) => {
						if(data.status === 0) {
							this.$message({
								message: '添加组成功!'
							});
							this.$emit('data', {});
							this.modalVisible = false;
						} else {
							this.$message.error(data.message);
						}
					}).catch(() => {
						this.$message.error('添加组失败!');
					});
				}
			});
		},
    },
}
</script>

<style scoped lang="scss">
.edit-device-form {
    .el-dialog {
        width: 300px;
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
