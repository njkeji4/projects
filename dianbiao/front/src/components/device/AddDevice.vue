<template>
	<!-- batch config -->
	<el-dialog :title="title" ref="modal"  width="30%" :visible.sync="modalVisible" :close-on-click-modal="false"
         class="edit-device-form">
         
		<el-form :model="batchEditForm" label-width="7em" :rules="batchEditFormRules" ref="batchEditForm">			
			 <el-form-item label="选择机房" prop="roomName">
                <el-select v-model="batchEditForm.roomId" placeholder="请选择机房" style="width:270px !important;">
                    <el-option
                        v-for="item in rooms"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="设备名字" prop="deviceName">
				<el-input type="text" v-model="batchEditForm.deviceName" size="small"  style="width:270px !important;"></el-input>
			</el-form-item>
            <el-form-item label="设备编号" prop="deviceNo">
				<el-input type="text" v-model="batchEditForm.deviceNo" size="small"  style="width:270px !important;"></el-input>
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
import { AdminAPI,RoomAPI } from '../../api';

export default {
    data: function() {
        return {   
           //	deviceType:1,
			deviceTypes:['直流4路','单相交流','三相交流'], 
           rooms:[],    
           modalVisible: true,			
           batchConfigLoading: false,           
           title: '增加设备',
           batchEditForm:{
               deviceName:'',
               deviceNo:'',
               roomId:''
           },
           batchEditFormRules: {
                deviceName: [
                    { required: true, message: '输入设备名字', trigger: 'change' }
                ],
                deviceNo: [
                    { required: true, message: '输入设备编号', trigger: 'change' }
                ]
           },
        };
    },
	computed: {
	},
    created() {
        this.title += " - " + this.deviceTypes[this.deviceType - 1];
    },
    mounted(){
        this.getAllRooms();
    },
    methods: {
        getAllRooms() {
				var searchParams = {};
				searchParams.page = 0;
				searchParams.size = 10000;
				searchParams.sort="name";
				searchParams.order="asc";				
				RoomAPI.search(searchParams).then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {						
						this.rooms = jsonData.data.content;						
					} else {
						this.$message({
							messsage: `获取机房列表失败:${data.msg}`,
							type: 'error'
						})
					}
				});
		},

		batchEdidtSubmit() {            
			this.$refs.batchEditForm.validate(valid => {
				if(valid) {		                   	
					AdminAPI.addDevice(
                        {
                            deviceName:this.batchEditForm.deviceName,
                            deviceNo:this.batchEditForm.deviceNo,
                            roomId:this.batchEditForm.roomId,
                            deviceType:this.deviceType
                        }
                    ).then(({data}) => {
						if(data.status === 0) {
							this.$message({
								message: '添加设备成功!'
							});
							this.$emit('data', {});
							this.modalVisible = false;
						} else {
							this.$message.error(data.message);
						}
					}).catch(() => {
						this.$message.error('添加设备失败!');
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
