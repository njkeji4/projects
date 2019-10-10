<template>
	<!-- batch config -->
	<el-dialog :title="title" ref="modal"  width="60%" :visible.sync="modalVisible" :close-on-click-modal="false"
         class="edit-device-form">
         
		<el-form :model="batchEditForm" label-width="7em" :rules="batchEditFormRules" ref="batchEditForm">
            <el-row :gutter=20 class="toolbar searchparam">
                
                <el-col  :span=24 class="search-action-wrap" style="margin-bottom:10px;">
                    
							<el-time-picker  v-model="actionTime" placeholder="拉合闸时间">
                            </el-time-picker >
                    
                            <el-select clearable size="small" v-model="action" placeholder="拉合闸" style="margin-left:10px;">
                                    <el-option  label="拉闸" value="0"></el-option>
                                    <el-option  label="合闸" value="1"></el-option>
                            </el-select>
                    
                    
                    <el-button style="margin-left:10px;" size="small" type="primary" @click="batchEdidtSubmit" :loading="batchConfigLoading">增加</el-button>
                </el-col>
            </el-row>	
		</el-form>
	
        <section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe  ref="table" 
			  class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">				
				</el-table-column>
				
				<el-table-column  sortable="custom" prop="deviceName" label="设备名称" width="240"></el-table-column>
			
                <el-table-column  sortable="custom" prop="action" label="拉合闸" width="240">
					<template slot-scope="scope">						
						<el-tag :type="scope.row.action == '0' ? 'success' : 'danger'" close-transition>{{scope.row.action == '0'?'拉闸':'合闸'}}</el-tag>					
					</template>
				</el-table-column>

				<el-table-column  sortable="custom" prop="actionTime" label="时间" width="240">
                    <template slot-scope="scope">
						{{scope.row.actionTime | dateFormat}}
					</template>
                </el-table-column>

			</el-table>
		</section>

	</el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { AdminAPI } from '../../api';
import Filters from '../../common/js/filters';

export default {
    data: function() {
        var nameCheck = (rule, value, callback) => { 
                        if(value === this.deviceInfo.name){                            
                            callback(new Error("输入不同的设备名称"));
                        }else{
                            callback();
                        }
                    };
        return {          
           modalVisible: true,			
           batchConfigLoading: false,           
           title: '',
           devices:[],
           action:'',
           actionTime:'',
           batchEditForm:{
               name:'',
               action:'',
               actionTime:''
           },
           batchEditFormRules: {
                actionTime: [
                    { required: true, message: '选择时间', trigger: 'change' },
                    //{validator : nameCheck, trigger: 'blur'}
                ],
                action:[
                    { required: true, message: '选择操作', trigger: 'change' },
                ]
           },
        };
    },
	computed: {
		
	},
    created() {                
        this.title = `设备编号: ${this.deviceInfo.deviceNo}`; 
    },

    filters: {
			dateFormat: Filters.dateFormat,
			scoreFormat:(v,f) => {return parseFloat(v).toFixed(2);}
	},

    mounted(){
        console.log("getsetting.....");
        this.getSetting();
    },
    methods: {

        getSetting(){
             AdminAPI.getDeviceSetting({deviceNo:this.deviceInfo.deviceNo}).then(({
					data: jsonData
				}) => {
					if(jsonData !== null) {
						this.devices = jsonData;
					} else {
						this.$message({
							messsage: `获取设备列表失败:${data.msg}`,
							type: 'error'
						})
					}
				});
        },

		batchEdidtSubmit() {            

			this.$refs.batchEditForm.validate(valid => {
				if(valid) {		
                   
					AdminAPI.addDeviceSetting(
                            {
                                actionTime:Date.parse(this.actionTime), 
                                action:this.action, 
                                deviceNo:this.deviceInfo.deviceNo,
                                deviceName:this.deviceInfo.deviceName}
                    ).then(({data}) => {
						if(data.status === 0) {
							this.$message({
								message: '添加成功!'
							});
							this.$emit('data', {});
							this.modalVisible = false;
						} else {
							this.$message.error(data.message);
						}
						
					}).catch(() => {
						this.$message.error('修改设备信息失败!');
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
